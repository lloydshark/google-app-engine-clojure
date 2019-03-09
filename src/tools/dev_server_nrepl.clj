(ns dev-server-nrepl
  (:require [nrepl.server :as nrepl])
  (:gen-class :implements [javax.servlet.ServletContextListener])
  (:import (java.util Properties)))

(defn load-properties [properties-file]
  (let [properties (Properties.)]
    (.load properties (clojure.java.io/input-stream properties-file))
    properties))

(defn -contextInitialized [this _]
  (println "Starting nRepl...")
  (let [properties (load-properties "WEB-INF/dev-server-nrepl.properties")
        port       (Integer/parseInt (.getProperty properties "port"))]
    (defonce nrepl-server (nrepl/start-server :port port))
    (println (format "Starting nRepl... [OK] on port %s" port))))

(defn -contextDestroyed [_ _]
  (println "Bye..."))