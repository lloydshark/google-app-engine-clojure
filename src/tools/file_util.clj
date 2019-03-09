(ns file-util
  (:require [clojure.java.io :as io])
  (:import (java.io File)))

(defn create-directory [^String directory]
  (println "create-directory" directory)
  (.mkdir (File. directory)))

(def all-files-filter (constantly true))

(defn copy-file [^String from ^String to]
  (println "copy-file" from to)
  (io/make-parents to)
  (io/copy (io/file from) (io/file to)))

(defn copy-subdirectories
  ([a-dir another-dir]
   (copy-subdirectories a-dir another-dir all-files-filter))
  ([a-dir another-dir filter]
   (println "copy-subdirectories" a-dir another-dir)
   (doseq [^File a-file (file-seq (io/file a-dir))]
     (when (and (not (.isDirectory a-file))
                (filter a-file))
       (let [from (.getPath a-file)
             to   (str another-dir (subs from (count a-dir)))]
         (copy-file from to))))))