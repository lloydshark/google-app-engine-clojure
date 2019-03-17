(ns myapp.ui.events
  (:require
    [ajax.core :as ajax]
    [re-frame.core :as re-frame]
    [day8.re-frame.http-fx]
    [myapp.ui.db :as db]))

(re-frame/reg-event-db ::initialize-db
                       (fn [_ _] db/initial-db))

(re-frame/reg-event-fx ::fetch-widgets
                       (fn [{origin :origin} _]
                         {:http-xhrio {:method          :get
                                       :uri             (str origin "/api/list")
                                       :timeout         8000
                                       :response-format (ajax/json-response-format)
                                       :on-success      [::on-widget-fetch]
                                       :on-failure      [::bad-http-result]}}))

(re-frame/reg-event-fx ::add-widget
                       (fn [{origin :origin
                             db     :db} _]
                         {:db         (assoc db :label-edit "")
                          :http-xhrio {:method          :post
                                       :uri             (str origin "/api/store")
                                       :params          {:label (:label-edit db)}
                                       :timeout         8000
                                       :format          (ajax/json-request-format)
                                       :response-format (ajax/json-response-format)
                                       :on-success      [::fetch-widgets]
                                       :on-failure      [::bad-http-result]}}))

(re-frame/reg-event-fx ::delete-widget
                       (fn [{origin :origin} [_event id]]
                         {:http-xhrio {:method          :post
                                       :uri             (str origin "/api/delete")
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
