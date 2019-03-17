(ns myapp.ui.core
  (:require
   [reagent.core :as reagent]
   [re-frame.core :as re-frame]
   [myapp.ui.events :as events]
   [myapp.ui.views :as views]
   [myapp.ui.config :as config]
   ))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/home]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (re-frame/dispatch [::events/fetch-widgets])
  (dev-setup)
  (mount-root))

;; specify reload hook with ^;after-load metadata
(defn ^:after-load on-reload []
  (mount-root))
