(ns server.routes
    (:use [compojure.core :only [defroutes GET POST DELETE OPTIONS ANY context]]
          (ring.middleware [keyword-params :only [wrap-keyword-params]]
                           [params :only [wrap-params]]
                           [session :only [wrap-session]])
          [server.middleware :only [wrap-failsafe wrap-request-logging-in-dev
                                              wrap-reload-in-dev JGET JPUT JPOST JDELETE]])
    (:require [server.handlers.app :as app]
              [server.handlers.api :as api]
              [compojure.route :as route]))

;; define mapping here
(defroutes server-routes*
  (GET "/" [] app/show-landing)
  (OPTIONS "*" [] api/show-options)
  (context "/api" []
           ;; JGET returns json encoding of the response
           (JGET "/items" [] api/get-items)
           (JPOST "/items" [] api/add-item)
           (JDELETE "/items/:id" [id] api/delete-item))
  ;; static files under ./public folder, prefix /static
  ;; like /static/css/style.css
  (route/files "/static")
  ;; 404, modify for a better 404 page
  (route/not-found "<p>Page not found.</p>" ))

(defn app [] (-> #'server-routes*
                 wrap-session
                 wrap-keyword-params
                 wrap-params
                 wrap-request-logging-in-dev
                 wrap-reload-in-dev
                 wrap-failsafe))
