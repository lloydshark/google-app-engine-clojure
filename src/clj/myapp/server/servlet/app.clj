(ns myapp.server.servlet.app
  (:require [ring.util.servlet :as ring-servlet]
            [myapp.server.app :as app])
  (:gen-class :extends javax.servlet.http.HttpServlet))

(ring-servlet/defservice app/handler)


