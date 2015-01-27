(ns the-starbucks-problem.geometry
  (:require [swissknife.math.geometry :as g]))

;;; 1 degree in either direction is approximately 111.2 Km

(defn passes?
  "Whether the path (given by 2 latlong vectors) passes the location"
  [[origin destination] location & {:keys [distance]
                                    :or {distance 20/11800}}] ; 20 meters
  (let [org (g/point (first origin) (second origin))
        dst (g/point (first destination) (second destination))
        loc (g/point (first location) (second location))
        pth-line (g/points->line org dst)
        loc-line (g/point->line (g/invert-slope (:m pth-line)) loc)
        int-point (g/intersects-at loc-line pth-line)]
    (and (<= (g/distance-sqr loc int-point) (* distance distance))
         (or (<= (:x org) (:x loc) (:x dst))
             (>= (:x org) (:x loc) (:x dst))
             (<= (:y org) (:y loc) (:y dst))
             (>= (:y org) (:y loc) (:y dst))))))
