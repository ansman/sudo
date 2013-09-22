(ns server.database
  (:require [org.httpkit.dbcp :as db]))

(defn add [description]
  (db/insert-record :items {:description description}))

(defn list-all []
  (db/query "select * from items"))

(defn delete [id]
  (db/delete-rows :items ["id = ?" id]))

(defn delete-all []
  (db/do-commands "truncate items"))

(defn initialize []
  (def db-name (System/getProperty "db.name" "sudo_dev"))
  (def db-string (str "jdbc:mysql://localhost/" db-name))
  (println "Using db-string:" db-string)

  (db/use-database! db-string "wrapptest" "wrapptest")

  (println "Deleting data" (delete-all))
  (add "collect underpants")
  (add "...")
  (add "profit!"))
