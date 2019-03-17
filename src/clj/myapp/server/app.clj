(ns myapp.server.app
  (:require [bidi.ring :as bidi]
            [cheshire.core :as json]
            [myapp.server.datastore :as datastore]))

(defn list-widgets [_]
  {:status  200
   :headers {"Content-Type" "application/json"}
   :body    (json/generate-string (datastore/fetch "widget"))})

(defn save-widget [request]
  (datastore/save {:kind       "widget"
                   :properties (-> (slurp (:body request))
                                   (json/parse-string true))})
  {:status 201
   :body   (json/generate-string {:status "OK"})})

(defn delete-widget [request]
  (datastore/delete "widget" (-> (slurp (:body request))
                                 (json/parse-string true)
                                 (:id)))
  {:status 201
   :body   (json/generate-string {:status "OK"})})

(defn wrap-not-found [handler]
  (fn [request]
    (or (handler request)
        {:status 404})))

(def handler
  (-> (bidi/make-handler
        ["/api" {"/list"  list-widgets
                 "/store" save-widget
                 "/delete" delete-widget}])
      wrap-not-found))
