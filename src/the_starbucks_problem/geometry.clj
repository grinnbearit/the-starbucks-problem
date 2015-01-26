(ns the-starbucks-problem.geometry
  (:require [swissknife.math.vectors :as v]))

;;; 1 degree in either direction is approximately 111.2 Km

(defn passes?
  "Whether the path (given by 2 latlong vectors) passes the point"
  [path point & {:keys [distance]
                 :or {distance 20/111200}}] ; 20 meters
  (let [[[from-lat from-lng] [to-lat to-lng]] path
        [point-lat point-lng] point
        path-vector (v/subtract (v/components->vector to-lng to-lat)
                                (v/components->vector from-lng from-lat))
        point-vector (v/components->vector point-lng point-lat)
        theta (- (:theta point-vector) (:theta path-vector))]
    (and (< (Math/abs (* (:m point-vector) (Math/cos theta)))
            (Math/abs (:m path-vector)))
         (< (Math/abs (* (:m point-vector) (Math/sin theta)))
            distance))))
