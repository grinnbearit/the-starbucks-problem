(ns the-starbucks-problem.geometry-test
  (:require [midje.sweet :refer :all]
            [the-starbucks-problem.geometry :refer :all]))


(facts
 ;; Ist quadrant
 (passes? [[0 0] [10 10]] [5 4] :distance 2)
 => true

 (passes? [[0 0] [10 10]] [5 0] :distance 2)
 => false

 (passes? [[0 0] [10 10]] [5 6] :distance 2)
 => true

 (passes? [[0 0] [10 10]] [5 10] :distance 2)
 => false

 ;; IInd quadrant
 (passes? [[0 0] [-10 10]] [-5 4] :distance 2)
 => true

 (passes? [[0 0] [-10 10]] [-5 0] :distance 2)
 => false

 (passes? [[0 0] [-10 10]] [-5 6] :distance 2)
 => true

 (passes? [[0 0] [-10 10]] [5 10] :distance 2)
 => false

 ;; IIIrd quadrant
 (passes? [[0 0] [-10 -10]] [-5 -4] :distance 2)
 => true

 (passes? [[0 0] [-10 -10]] [-5 0] :distance 2)
 => false

 (passes? [[0 0] [-10 -10]] [-5 -6] :distance 2)
 => true

 (passes? [[0 0] [-10 -10]] [-5 -10] :distance 2)
 => false

 ;; IVth quadrant
 (passes? [[0 0] [10 -10]] [5 -4] :distance 2)
 => true

 (passes? [[0 0] [10 -10]] [5 0] :distance 2)
 => false

 (passes? [[0 0] [10 -10]] [5 -6] :distance 2)
 => true

 (passes? [[0 0] [10 -10]] [5 -10] :distance 2)
 => false)
