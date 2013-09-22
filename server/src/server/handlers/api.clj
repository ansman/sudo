(ns server.handlers.api
  (:require [server.database :as db]))

(defn show-options [req]
  {:status 200
   :headers {"Access-Control-Allow-Methods" "POST, GET, PUT, OPTIONS, DELETE"
             "Access-Control-Allow-Origin" "*"}})

(defn get-items [req]
  {:time (System/currentTimeMillis)
   :req (merge req {:async-channel nil})
   :items (db/list-all)
   })

(defn add-item [req]
  (let [jsondata (:body req)]
    {:time (System/currentTimeMillis)
     :req (assoc (merge req {:async-channel nil}) :body jsondata)
     :items (db/add (get jsondata "description"))
     }))

(defn delete-item [req]
  (let [id (:id (:route-params req))]
    {:time (System/currentTimeMillis)
     :req (merge req {:async-channel nil})
     :items (db/delete (Integer. id))
     }))
