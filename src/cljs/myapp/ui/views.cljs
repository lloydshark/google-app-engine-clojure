(ns myapp.ui.views
  (:require
    [re-frame.core :as re-frame]
    [soda-ash.core :as sa]
    [myapp.ui.events :as events]
    [myapp.ui.subs :as subs]
    ))

(defn main-panel [label-edit widgets]
  [sa/Container
   [sa/SegmentGroup
    [sa/Segment
     [:img {:width "40px"
            :src   "/img/logo.png"}]]
    [sa/Segment
     [sa/Header "Google App Engine Clojure!!!"]]]
   [sa/Container
    [sa/Input {:type        "text"
               :value       (or @label-edit "")
               :placeholder "label"
               :on-change   #(re-frame/dispatch [::events/edit-label (-> % .-target .-value)])}]
    [sa/Input {:type     "button"
               :value    "Add Widget"
               :disabled (= "" @label-edit)
               :on-click #(re-frame/dispatch [::events/add-widget])}]]
   [sa/Table
    [sa/TableHeader
     [sa/TableRow
      [sa/TableHeaderCell "ID"]
      [sa/TableHeaderCell "Label"]
      [sa/TableHeaderCell "Action"]]]
    [sa/TableBody
     (for [widget @widgets]
       (let [id (get widget "id")]
         ^{:key id}
         [sa/TableRow
          [sa/TableCell id]
          [sa/TableCell (get-in widget ["properties" "label"])]
          [sa/TableCell [sa/Input {:type     "button"
                                   :value    "Delete"
                                   :on-click #(re-frame/dispatch [::events/delete-widget id])}]]]))]]])

(defn home []
  (let [label-edit (re-frame/subscribe [::subs/label-edit])
        widgets    (re-frame/subscribe [::subs/widgets])]
    (main-panel label-edit widgets)))


