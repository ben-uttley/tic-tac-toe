;; Web entry, routes and handlers
(ns tic-tac-toe.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [tic-tac-toe.api :as ttt.api]
            [clojure.data.json :as json]
            [clj-uuid :as uuid]
            [ring.util.http-response :as response]
            [ring.middleware.defaults :refer [api-defaults wrap-defaults]]
            [ring.logger :as logger]
            [org.httpkit.server :as server]))

(defn handle-create-game []
  (response/ok (ttt.api/create-game))
)

(defn handle-join-game [id]
  (response/ok (ttt.api/join-game (Integer/parseInt id))))

(defn handle-make-move [id position player]
  (let [result (ttt.api/make-move (Integer/parseInt id) player (Integer/parseInt position))]
    (case 
        (= (result :status) "bad") (response/bad-request (result :content))
        (= (result :status) "ok") (response/ok (result :content)))))

(defroutes app-routes
  (POST "/game" [] (handle-create-game))
  (PUT "/game/:id{[0-9]+}" [id] (handle-join-game id))
  (PUT "/game/:id/make-move/:position{[1-9]{1}}" [id position player] 
       (handle-make-move id position player))
  (route/not-found "<h1>Page not found</h1>"))

(def app
  (-> (var app-routes)
      (wrap-defaults api-defaults)
      (logger/wrap-with-logger)))

(defn start-server []
  (server/run-server app {:port 3010}))
