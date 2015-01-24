(defproject the_starbucks_problem "0.1.0"
  :description "Solution to the Starbucks problem (more in the readme)"
  :url "http://github.com/grinnbearit/the-starbucks-problem"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [http-kit "2.1.18"]
                 [cheshire "5.4.0"]]
  :profiles {:dev {:plugins [[lein-midje "3.1.3"]]
                   :dependencies [[midje "1.6.3"]]}})
