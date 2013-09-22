(ns server.handlers.api
  (:require [org.httpkit.dbcp :as db]))

(def db-name (System/getProperty "db.name" "sudo_dev"))
(def db-string (str "jdbc:mysql://localhost/" db-name))
(println "db-string:" db-string)

(db/use-database! db-string "wrapptest" "wrapptest")

(defn add [description]
  (db/insert-record :items {:description description}))

(defn list-all []
  (db/query "select * from items"))

(defn delete [id]
  (db/delete-rows :items ["id = ?" id]))

(defn delete-all []
  (db/delete-rows :items ["true"]))


(println "Deleting data" (delete-all))
(add "collect underpants")
(add "...")
(add "profit!")


(defn show-options [req]
  {:status 200
   :headers {"Access-Control-Allow-Methods" "POST, GET, PUT, OPTIONS, DELETE"
             "Access-Control-Allow-Origin" "*"}})

(defn get-items [req]
  {:time (System/currentTimeMillis)
   :req (merge req {:async-channel nil})
   :items (list-all)
   })

(defn add-item [req]
  (let [jsondata (:body req)]
    {:time (System/currentTimeMillis)
     :req (assoc (merge req {:async-channel nil}) :body jsondata)
     :items (add (get jsondata "description"))
     }))

(defn delete-item [req]
  (let [id (:id (:route-params req))]
    {:time (System/currentTimeMillis)
     :req (merge req {:async-channel nil})
     :items (delete (Integer. id))
     }))
