(ns myapp.server.servlet.app
  (:require [ring.util.servlet :as ring-servlet]
            [myapp.server.app :as app])
  (:gen-class :extends javax.servlet.http.HttpServlet))

;; By wrapping this in a handler fn it means app/handler can be reloaded in a REPL session...
(defn handler [request] (app/handler request))

(ring-servlet/defservice handler)