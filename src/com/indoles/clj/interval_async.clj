(ns com.indoles.clj.interval-async
  (:require [clojure.core.async :as async]))

(defn create
  ^{:doc "execute f repeatetly, stop after receving a message on the returned channel"}
  ([initial-interval interval f]
     (let [stop-ch (async/chan)]
       (async/go-loop [stop-chan stop-ch
                       timeout-ch (async/timeout initial-interval)]
         (let [[val port] (async/alts! [stop-chan timeout-ch])]
           (when (= port timeout-ch)
             (f)
             (recur stop-chan (async/timeout interval)))))
       stop-ch))
  ([interval f] (create 0 interval f)))

(defn stop
  [stop-ch]
  (async/>!! stop-ch true))

