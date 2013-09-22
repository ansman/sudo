(ns server.handlers.api
  )

(defn get-items [req]
  {:time (System/currentTimeMillis)
   :req (merge req {:async-channel nil})
   :items `({:id 1 :description "collect underpants"}
            {:id 2 :description "..."}
            {:id 3 :description "profit!"})
   })
