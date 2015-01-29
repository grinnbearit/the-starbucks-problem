(ns the-starbucks-problem.core-test
  (:require [midje.sweet :refer :all]
            [the-starbucks-problem.core :refer :all]
            [the-starbucks-problem.geometry :as g]
            [the-starbucks-problem.api :as api]))


(facts
 (let [starbucks-step #'the-starbucks-problem.core/starbucks-step]

   (starbucks-step {:start [0 0] :end [10 0]} [{:location [2 0.5]}
                                               {:location [2 -10]}])
   => {:location [2 0.5]}

   (provided
    (g/passes? [[0 0] [10 0]] [2 0.5]) => true
    (g/passes? [[0 0] [10 0]] [2 -10]) => false)


   (starbucks-step {:start [0 0] :end [10 0]} [{:location [2 -10]}])
   => nil

   (provided
    (g/passes? [[0 0] [10 0]] [2 -10]) => false)))


(facts
 (let [starbucks-route #'the-starbucks-problem.core/starbucks-route]

   (starbucks-route {:bounds {:top-left [0 10]
                              :bottom-right [10 0]}
                     :steps [{:start [0 0] :end [10 0]}
                             {:start [10 0] :end [10 10]}]})
   => {:location [2 0.5]}

   (provided
    (api/starbucks [0 10] [10 0])
    => [{:location [2 0.5]}
        {:location [2 -10]}]

    (#'the-starbucks-problem.core/starbucks-step {:start [0 0] :end [10 0]}
                                                 [{:location [2 0.5]}
                                                  {:location [2 -10]}])
    => {:location [2 0.5]})


   (starbucks-route {:bounds {:top-left [0 10]
                              :bottom-right [10 0]}
                     :steps [{:start [0 0] :end [10 0]}]})
   => nil

   (provided
    (api/starbucks [0 10] [10 0])
    => [{:location [2 -10]}]

    (#'the-starbucks-problem.core/starbucks-step {:start [0 0] :end [10 0]}
                                                 [{:location [2 -10]}])
    => nil)))


(facts
 (starbucks-probability [0 0] [10 10] 5)
 => {:probability 1/3
     :starbucks [{:location [5 4]}]}

 (provided
  (api/directions [0 0] [10 10])
  => [{:bounds {:top-left [0 10]
                :bottom-right [10 0]}
       :duration 180
       :steps [{:start [0 0] :end [10 10]}]}
      {:bounds {:top-left [0 10]
                :bottom-right [10 0]}
       :duration 200
       :steps [{:start [0 0] :end [10 0]}
               {:start [10 0] :end [10 10]}]}
      {:bounds {:top-left [0 10]
                :bottom-right [10 0]}
       :duration 420
       :steps [{:start [0 0] :end [0 10]}
               {:start [0 10] :end [10 10]}]}]

  (#'the-starbucks-problem.core/starbucks-route {:bounds {:top-left [0 10]
                                                          :bottom-right [10 0]}
                                                 :duration 180
                                                 :steps [{:start [0 0] :end [10 10]}]})
  => {:location [5 4]}

  (#'the-starbucks-problem.core/starbucks-route {:bounds {:top-left [0 10]
                                                          :bottom-right [10 0]}
                                                 :duration 200
                                                 :steps [{:start [0 0] :end [10 0]}
                                                         {:start [10 0] :end [10 10]}]})
  => nil))
