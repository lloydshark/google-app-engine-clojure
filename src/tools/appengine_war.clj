(ns appengine-war
  (:require [clojure.java.io :as io]
            [clojure.tools.deps.alpha :as deps]
            [clojure.tools.deps.alpha.reader :as deps-reader]
            [file-util :as file-util])
  (:import (java.io File)))

(defn clj-source-files-filter [^File a-file]
  (.endsWith (.getName a-file) ".clj"))

(defn create-war-directory-structure [target-env-dir]
  (file-util/create-directory target-env-dir)
  (file-util/create-directory (str target-env-dir "/war"))
  (file-util/create-directory (str target-env-dir "/war/WEB-INF"))
  (file-util/create-directory (str target-env-dir "/war/WEB-INF/classes")))

(defn add-appengine-web-xml [src-appengine-web-xml target-appengine-web-xml]
  (file-util/copy-file src-appengine-web-xml target-appengine-web-xml))

(defn insert-nrepl-servlet [web-xml-content]
  (let [end-tag-index (clojure.string/index-of web-xml-content "</web-app>")]
    (str (subs web-xml-content 0 end-tag-index)
         (str "\n  <!-- Just for Dev -->\n"
              "  <listener><listener-class>dev_server_nrepl</listener-class></listener>\n\n")
         (subs web-xml-content end-tag-index))))

(defn add-web-xml [src-web-xml target-web-xml dev?]
  (spit target-web-xml
        (cond-> (slurp src-web-xml)
                dev? (insert-nrepl-servlet))))

(defn add-servlets [target-war-dir target-war-classes-dir servlets nrepl-port dev?]
  (binding [*compile-path* target-war-classes-dir]
    (doseq [servlet (cond-> servlets
                            dev? (conj 'dev-server-nrepl))]
      (println "compiling" servlet)
      (compile servlet)))
  (when dev?
    (spit (str target-war-dir "/WEB-INF/dev-server-nrepl.properties")
          (str "port=" nrepl-port))))

(defn add-clj-sources [src-clj-sources-dir target-war-classes-dir]
  (file-util/copy-subdirectories src-clj-sources-dir target-war-classes-dir clj-source-files-filter))

(defn add-dependency-libs [deps-edn-file target-war-lib-dir]
  (println "Resolve and Package the War libs...")
  (let [deps-map (deps-reader/slurp-deps deps-edn-file)
        deps     (deps/resolve-deps deps-map
                                    (select-keys (get-in deps-map [:aliases :server])
                                                 [:extra-deps]))]
    (doseq [[_ {paths :paths}] deps]
      (let [lib-file    (first paths)
            target-file (str target-war-lib-dir "/" (.getName (io/file lib-file)))]
        (file-util/copy-file lib-file target-file)))))

(defn add-public-resources [src-resources-dir target-dir]
  (file-util/copy-subdirectories src-resources-dir target-dir))

(defn -main [& args]

  (let [environment              (or (first args) "dev")
        dev?                     (= "dev" environment)
        config                   (clojure.edn/read-string (slurp "app-engine.edn"))

        src-appengine-web-xml    "resources/appengine-web.xml"
        src-clj-sources-dir      "src/clj"
        src-deps-edn-file        "deps.edn"
        src-public-resources-dir "resources/public"
        src-web-xml              "resources/web.xml"

        servlets                 (:servlets config)
        nrepl-port               (:dev-server-nrepl-port config)

        target-dir               "target"
        target-env-dir           (str target-dir "/" environment)
        target-war-dir           (str target-env-dir "/war")
        target-appengine-web-xml (str target-war-dir "/WEB-INF/appengine-web.xml")
        target-web-xml           (str target-war-dir "/WEB-INF/web.xml")
        target-war-classes-dir   (str target-war-dir "/WEB-INF/classes")
        target-war-lib-dir       (str target-war-dir "/WEB-INF/lib")]

    (create-war-directory-structure target-env-dir)
    (add-appengine-web-xml src-appengine-web-xml target-appengine-web-xml)
    (add-web-xml src-web-xml target-web-xml dev?)
    (add-servlets target-war-dir target-war-classes-dir servlets nrepl-port dev?)
    (add-clj-sources src-clj-sources-dir target-war-classes-dir)
    (add-dependency-libs src-deps-edn-file target-war-lib-dir)
    (add-public-resources src-public-resources-dir target-war-dir)))