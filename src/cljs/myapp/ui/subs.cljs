(ns myapp.ui.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub ::name
                  (fn [db] (:name db)))

(re-frame/reg-sub ::label-edit
                  (fn [db] (:label-edit db)))

(re-frame/reg-sub ::widgets
                  (fn [db] (:widgets db)))
