(ns the-starbucks-problem.api
  (:require [org.httpkit.client :as http]
            [cheshire.core :as json]
            [clojure.string :as str]
            [the-starbucks-problem.settings :refer [FACTUAL_KEY]]))


(defn- request
  [url params]
  (->> @(http/get url {:query-params params})
       :body
       json/parse-string))


(defn- directions-request
  [origin destination]
  (request "http://maps.googleapis.com/maps/api/directions/json"
           {"origin" (str/join "," origin)
            "destination" (str/join "," destination)
            "alternatives" "true"
            "units" "metric"}))


(defn- starbucks-list-request
  [top-left bottom-right & {:keys [limit offset]
                            :or {limit 50 offset 0}}]
  (request "http://api.v3.factual.com/t/places"
           {"geo" (json/generate-string {"$within" {"$rect" [top-left bottom-right]}})
            "filters" (json/generate-string {"name" {"$eq" "starbucks"}})
            "select" "latitude,longitude"
            "limit" limit
            "offset" offset
            "KEY" FACTUAL_KEY}))


(defn- starbucks-count-request
  [top-left bottom-right]
  (request "http://api.v3.factual.com/t/places/facets"
           {"select" "country"
            "geo" (json/generate-string {"$within" {"$rect" [top-left bottom-right]}})
            "filters" (json/generate-string {"name" {"$eq" "starbucks"}})
            "KEY" FACTUAL_KEY}))


(defn directions
  "returns a list of driving routes between origin and destination

  origin and destination should be 2 element vectors containing numeric latitude and longitude
  for more info, https://developers.google.com/maps/documentation/directions/"
  [origin destination]
  (let [response (directions-request origin destination)]
    (for [route (response "routes")]
      {:bounds {:top-left [(get-in route ["bounds" "northeast" "lat"])
                           (get-in route ["bounds" "southwest" "lng"])]
                :bottom-right [(get-in route ["bounds" "southwest" "lat"])
                               (get-in route ["bounds" "northeast" "lng"])]}
       :distance (get-in route ["legs" 0 "distance" "value"])
       :duration (get-in route ["legs" 0 "duration" "value"])
       :steps (for [step (get-in route ["legs" 0 "steps"])]
                {:start [(get-in step ["start_location" "lat"])
                         (get-in step ["start_location" "lng"])]
                 :end [(get-in step ["end_location" "lat"])
                       (get-in step ["end_location" "lng"])]
                 :distance (get-in step ["distance" "value"])
                 :duration (get-in step ["duration" "value"])})})))


(defn starbucks
  "returns a list of starbucks coordinates in the bounding box given by top-left and bottom-right

  top-left and bottom-right should be 2 element vectors containing numeric latitude and longitude
  for more info, http://developer.factual.com/api-docs/#Read"
  [top-left bottom-right]
  (let [country-count (get-in (starbucks-count-request top-left bottom-right)
                              ["response" "data" "country"])
        row-count (reduce + (vals country-count))]
    (for [offset (range 0 row-count 50)
          :let [response (starbucks-list-request top-left bottom-right :offset offset)]
          shop (get-in response ["response" "data"])]
      [(shop "latitude") (shop "longitude")])))
