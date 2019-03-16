(ns myapp.server.datastore
  (:import (com.google.appengine.api.datastore DatastoreServiceFactory
                                               DatastoreService
                                               Entity
                                               Query
                                               FetchOptions$Builder)))

(defn ->Entity
  "Take a Clojure map - and produce an Entity that works with the datastore."
  [entity]
  (let [datastore-entity (Entity. ^String (:kind entity))]
    (doseq [[property-name property-value] (:properties entity)]
      (.setProperty datastore-entity (name property-name) property-value))
    datastore-entity))

(defn <-Entity
  "Take a datastore Entity and produce a Clojure map."
  [^Entity Entity]
  (reduce (fn [entity [name value]]
            (assoc-in entity [:properties (keyword name)] value))
          {:kind       (.getKind Entity)
           :properties {}}
          (.getProperties Entity)))

(defn save
  [entity]
  (let [^DatastoreService datastore-service (DatastoreServiceFactory/getDatastoreService)]
    (.put datastore-service ^Entity (->Entity entity))))

(defn fetch [kind]
  (let [^DatastoreService datastore-service (DatastoreServiceFactory/getDatastoreService)
        ^Query query                        (Query. ^String kind)]
    (into []
          (map <-Entity)
          (.asList (.prepare datastore-service query) (FetchOptions$Builder/withDefaults)))))