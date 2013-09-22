(ns server.handlers.api
  (:use [clojure.data.json :as json]))

(def counter (atom 0))
(def data (atom []))

(defn next-id []
  (swap! counter inc))

(defn add [description]
  (swap! data conj {:id (next-id) :description description}))

(defn delete [id]
  (let [pred (fn [x] (not= (:id x) id))
        filterfunc (fn [data] (vec (filter pred data)))]
    (swap! data filterfunc)))

(defn list-all []
  @data)

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
  (let [jsondata (json/read (clojure.java.io/reader (:body req)))]
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
