(ns the-starbucks-problem.core-test
  (:require [midje.sweet :refer :all]
            [the-starbucks-problem.core :refer :all]
            [the-starbucks-problem.geometry :as g]
            [the-starbucks-problem.api :as api]))


(facts
 (starbucks-step {:start [0 0] :end [10 0]}
                 [{:location [2 0.5]}
                  {:location [2 -10]}])
 => [{:location [2 0.5]}]

 (provided
  (g/passes? [[0 0] [10 0]] [2 0.5]) => true
  (g/passes? [[0 0] [10 0]] [2 -10]) => false))


(facts
 (starbucks-route {:bounds {:top-left [0 10]
                            :bottom-right [10 0]}
                   :steps [{:start [0 0] :end [10 0]}
                           {:start [10 0] :end [10 10]}]})
 => [{:start [0 0]
      :end [10 0]
      :starbucks [{:location [2 0.5]}]}]

 (provided
  (api/starbucks [0 10] [10 0])
  => [{:location [2 0.5]}
      {:location [2 -10]}]

  (starbucks-step {:start [0 0] :end [10 0]}
                  [{:location [2 0.5]}
                   {:location [2 -10]}])
  => [{:location [2 0.5]}]

  (starbucks-step {:start [10 0] :end [10 10]}
                  [{:location [2 0.5]}
                   {:location [2 -10]}])
  => []))


(facts
 (starbucks-probability [0 0] [10 10] 5)
 => {:probability 1/2
     :valid-routes [{:bounds {:bottom-right [10 0]
                              :top-left [0 10]}
                     :duration 180
                     :steps [{:start [0 0] :end [10 10]}]
                     :starbucks [{:start [0 0] :end [10 0]
                                  :starbucks [{:location [5 4]}]}]}]}

 (provided
  (api/directions [0 0] [10 10])
  => [{:bounds {:top-left [0 10]
                :bottom-right [10 0]}
       :duration 180
       :steps [{:start [0 0] :end [10 10]}]}
      {:bounds {:top-left [0 10]
                :bottom-right [10 0]}
       :duration 420
       :steps [{:start [0 0] :end [0 10]}
               {:start [0 10] :end [10 10]}]}]

  (starbucks-route {:bounds {:top-left [0 10]
                             :bottom-right [10 0]}
                    :duration 180
                    :steps [{:start [0 0] :end [10 10]}]})
  => [{:start [0 0] :end [10 0]
       :starbucks [{:location [5 4]}]}]))
