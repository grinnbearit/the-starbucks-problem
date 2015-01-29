(ns the-starbucks-problem.core
  (:require [the-starbucks-problem.api :as api]
            [the-starbucks-problem.geometry :refer [passes?]]))


(def FACTUAL [33.8790167 -118.2339481])
(def LAX [33.9425 -118.4081])


(defn- starbucks-step
  [step points]
  (letfn [(step-passes? [point]
            (passes? [(:start step) (:end step)] (:location point)))]

    (first (filter step-passes? points))))


(defn- starbucks-route
  [route]
  (let [points (api/starbucks (get-in route [:bounds :top-left])
                              (get-in route [:bounds :bottom-right]))]
    (some #(starbucks-step % points) (:steps route))))


(defn starbucks-probability
  "Given an origin and destination ([lat, lng]), returns a probability
  of passing a starbucks along with the first (if any?) starbucks passed
  for each route"
  [origin destination minutes]
  (let [all-routes (api/directions origin destination)
        passed-bucks (for [route all-routes
                           :when (<= (:duration route) (* minutes 60))
                           :let [bucks (starbucks-route route)]
                           :when bucks]
                       bucks)]
    {:probability (/ (count passed-bucks) (count all-routes))
     :starbucks passed-bucks}))
