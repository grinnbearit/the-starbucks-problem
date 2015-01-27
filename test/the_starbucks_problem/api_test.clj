(ns the-starbucks-problem.api-test
  (:require [midje.sweet :refer :all]
            [the-starbucks-problem.api :refer :all]))


(facts
 (directions [0 0] [1 1])
 => [{:bounds {:top-left [1 2]
               :bottom-right [3 4]}
      :distance 10
      :duration 20
      :steps [{:start [5 6]
               :end [7 8]
               :distance 30
               :duration 40
               :directions "Go <b>West</b>"}]}]

 (provided
  (#'the-starbucks-problem.api/directions-request [0 0] [1 1])
  => {"routes" [{"bounds" {"northeast" {"lat" 1 "lng" 4}
                           "southwest" {"lat" 3 "lng" 2}}
                 "legs" [{"distance" {"value" 10}
                          "duration" {"value" 20}
                          "steps" [{"start_location" {"lat" 5 "lng" 6}
                                    "end_location" {"lat" 7 "lng" 8}
                                    "distance" {"value" 30}
                                    "duration" {"value" 40}
                                    "html_instructions" "Go <b>West</b>"}]}]}]}))


(facts
 (starbucks [0 0] [1 1])
 => (for [x (range 10)]
      {:location [x (inc x)]
       :address "address"
       :region "region"})

 (provided
  (#'the-starbucks-problem.api/starbucks-count-request [0 0] [1 1])
  => {"response" {"data" {"country" {"US" 10}}}}

  (#'the-starbucks-problem.api/starbucks-list-request [0 0] [1 1] :offset 0)
  => {"response" {"data" (for [x (range 10)]
                           {"latitude" x "longitude" (inc x)
                            "address" "address" "region" "region"})}})


 (starbucks [0 0] [1 1])
 => (for [x (range 70)]
      {:location [x (inc x)]
       :address "address"
       :region "region"})

 (provided
  (#'the-starbucks-problem.api/starbucks-count-request [0 0] [1 1])
  => {"response" {"data" {"country" {"US" 30 "CA" 40}}}}

  (#'the-starbucks-problem.api/starbucks-list-request [0 0] [1 1] :offset 0)
  => {"response" {"data" (for [x (range 50)]
                           {"latitude" x "longitude" (inc x)
                            "address" "address" "region" "region"})}}

  (#'the-starbucks-problem.api/starbucks-list-request [0 0] [1 1] :offset 50)
  => {"response" {"data" (for [x (range 50 70)]
                           {"latitude" x "longitude" (inc x)
                            "address" "address" "region" "region"})}}))
