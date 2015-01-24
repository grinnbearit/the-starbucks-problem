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


(defn directions
  "returns a list of driving routes between origin and destination

  origin and destination should be 2 element vectors containing numeric latitude and longitude
  for more info, https://developers.google.com/maps/documentation/directions/"
  [origin destination]
  (let [response (request "http://maps.googleapis.com/maps/api/directions/json"
                          {"origin" (str/join "," origin)
                           "destination" (str/join "," destination)
                           "alternatives" "true"
                           "units" "metric"})]
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
  (let [box (json/generate-string {"$within" {"$rect" [top-left bottom-right]}})
        filters (json/generate-string {"name" {"$eq" "starbucks"}})]

    (letfn [(fetch [& {:keys [limit offset row-count?]
                       :or {limit 50 offset 0 row-count? false}}]
              (request "http://api.v3.factual.com/t/places"
                       {"geo" box
                        "filters" filters
                        "include_count" row-count?
                        "select" "latitude,longitude"
                        "limit" limit
                        "offset" offset
                        "KEY" FACTUAL_KEY}))]

      (let [row-count (get-in (fetch :limit 0 :row-count? true)
                              ["response" "total_row_count"])]
        (for [offset (range 0 row-count 50)
              :let [response (fetch :offset offset)]
              shop (get-in response ["response" "data"])]
          [(shop "latitude") (shop "longitude")])))))
