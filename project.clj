(defproject tic_tac_toe "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [compojure "1.6.1"]
                 [ring/ring-defaults "0.3.2"]
                 [danlentz/clj-uuid "0.1.9"]
                 [org.clojure/data.json "1.0.0"]
                 [metosin/ring-http-response "0.9.1"]
                 [ring-logger "1.0.1"]
                 [ring/ring-defaults "0.3.2"]
                 [http-kit "2.5.0"]
                 [codax "1.3.1"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler tic-tac-toe.handler/app}
  :main ^:skip-aot tic-tac-toe.cli
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
