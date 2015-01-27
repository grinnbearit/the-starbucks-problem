(ns the-starbucks-problem.core
  (:require [the-starbucks-problem.api :as api]
            [the-starbucks-problem.geometry :refer [passes?]]))


(def FACTUAL [33.8790167 -118.2339481])
(def LAX [33.9425 -118.4081])


(defn starbucks-step
  [step locations]
  (filter #(passes? [(:start step) (:end step)] (:location %)) locations))


(defn starbucks-route
  [route]
  (let [locations (api/starbucks (get-in route [:bounds :top-left])
                                 (get-in route [:bounds :bottom-right]))]
    (for [step (:steps route)
          :let [passed-bucks (starbucks-step step locations)]
          :when (seq passed-bucks)]
      (assoc step :starbucks passed-bucks))))


(defn starbucks-probability
  [origin destination minutes]
  (let [all-routes (api/directions origin destination)
        valid-routes (for [route all-routes
                           :when (<= (:duration route) (* minutes 60))
                           :let [steps (starbucks-route route)]
                           :when (seq steps)]
                       (assoc route :starbucks steps))]
    {:probability (/ (count valid-routes) (count all-routes))
     :valid-routes valid-routes}))
