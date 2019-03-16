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
   :body   "OK"})

(defn wrap-not-found [handler]
  (fn [request]
    (or (handler request)
        {:status 404})))

(def handler
  (-> (bidi/make-handler
        ["/app" {"/list"  list-widgets
                 "/store" {:post save-widget}}])
      wrap-not-found))
