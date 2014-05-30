(ns swissknife.math)


(defn factorial
  [n]
  (reduce * (bigint 1) (range 1 (inc n))))


(defn P
  [n k]
  (if (= n k)
    (factorial n)
    (reduce * (bigint 1) (range (- (inc  n) k) (inc n)))))


(defn C
  [n k]
  (/ (P n k) (factorial k)))


(defn mean
  [xs]
  (/ (reduce + xs) (count xs)))


(defn variance
  [xs & {:keys [mu sample?]}]
  (letfn [(square [x] (* x x))]
    (/ (->> (repeat (or mu (mean xs)))
            (map (comp square -) xs)
            (reduce +))
       (if sample?
         (dec (count xs))
         (count xs)))))


(defn standard-deviation
  [xs & {:keys [mu sigma sample?]}]
  (Math/sqrt (or sigma (variance xs :mu mu :sample? sample?))))
