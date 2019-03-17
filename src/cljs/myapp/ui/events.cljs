(ns myapp.ui.events
  (:require
    [ajax.core :as ajax]
    [re-frame.core :as re-frame]
    [day8.re-frame.http-fx]
    [myapp.ui.db :as db]))

(re-frame/reg-event-db ::initialize-db
                       (fn [_ _] db/default-db))

(re-frame/reg-event-fx ::fetch-widgets
                       (fn [_ _]
                         {:http-xhrio {:method          :get
                                       :uri             "http://localhost:8080/api/list"
                                       :timeout         8000
                                       :response-format (ajax/json-response-format)
                                       :on-success      [::on-widget-fetch]
                                       :on-failure      [::bad-http-result]}}))

(re-frame/reg-event-fx ::add-widget
                       (fn [{db :db} _]
                         {:db         (assoc db :label-edit "")
                          :http-xhrio {:method          :post
                                       :uri             "http://localhost:8080/api/store"
                                       :params          {:label (:label-edit db)}
                                       :timeout         8000
                                       :format          (ajax/json-request-format)
                                       :response-format (ajax/json-response-format)
                                       :on-success      [::fetch-widgets]
                                       :on-failure      [::bad-http-result]}}))

(re-frame/reg-event-fx ::delete-widget
                       (fn [_ [_event id]]
                         {:http-xhrio {:method          :post
                                       :uri             "http://localhost:8080/api/delete"
                                       :params          {:id id}
                                       :timeout         8000
                                       :format          (ajax/json-request-format)
                                       :response-format (ajax/json-response-format)
                                       :on-success      [::fetch-widgets]
                                       :on-failure      [::bad-http-result]}}))

(re-frame/reg-event-db ::edit-label
                       (fn [db [_event data]]
                         (assoc db :label-edit data)))

(re-frame/reg-event-db ::on-widget-fetch
                       (fn [db [_event data]]
                         (assoc db :widgets data)))

(re-frame/reg-event-fx ::bad-http-result
                       (fn [_ result]
                         (println "Http Failed:" result)))
