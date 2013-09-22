(ns server.handlers.api
  (:use [clojure.data.json :as json]))

(def counter (atom 0))
(def data (atom []))

(defn next-id []
  (swap! counter inc))

(defn add [description]
  (swap! data conj {:id (next-id) :description description}))

(defn delete [])
(defn list-all []
  @data)

(add "collect underpants")
(add "...")
(add "profit!")

(defn get-items [req]
  {:time (System/currentTimeMillis)
   :req (merge req {:async-channel nil})
   :items (list-all)
   })

(defn add-item [req]
  (let [jsondata (json/read (clojure.java.io/reader (:body req)))]
    (println jsondata)
    {:time (System/currentTimeMillis)
     :req (assoc (merge req {:async-channel nil}) :body jsondata)
     :items (add (get jsondata "description"))
     }))
