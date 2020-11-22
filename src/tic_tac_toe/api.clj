; JSON APIs
(ns tic-tac-toe.api
  (:require [compojure.core :refer :all]
            [tic-tac-toe.core :as ttt.core]
            [tic-tac-toe.store :as ttt.store]
            [tic-tac-toe.spec :as ttt.spec]
            [clojure.data.json :as json]
            [clj-uuid :as uuid]
            [clojure.spec.alpha :as s]))

(defn create-game []
  "Create a tic-tac-toe game"
  (let [game_id (ttt.store/get-next-game-id)
        turn (first ((vec ttt.spec/players?) 0))
        game {:game {:id game_id :board ttt.core/clear-board :turn turn}}
        player {:id (str (uuid/v1)) :display turn}]
    (ttt.store/set-game game_id (assoc-in game [:players] [player]))
    (json/write-str (assoc-in game [:player] player))))

(defn join-game [game_id]
  "Join a tic-tac-toe-game"
  (let [game_db (ttt.store/get-game game_id)
        game_player {:game (game_db :game)}
        turn (second ((vec ttt.spec/players?) 1))
        player {:id (str (uuid/v1)) :display turn}]
    (ttt.store/set-game game_id (assoc-in game_db [:players 1] player))
    (json/write-str (assoc-in game_player [:player] player))))

(defn make-move [game_id player_id position]
  "Make a move on a given tic-tac-toe game for a given player"
  (let [game (ttt.store/get-game game_id)
        turn ((game :game) :turn)
        player (ttt.store/get-player game_id player_id)
        board ((game :game) :board)
        position position]
    (cond
        (not= turn player) {:status "bad" :content (json/write-str "It's not your turn")}
        (ttt.core/can-move? position board) (let [board (ttt.core/make-move player position board)]
                                              (ttt.store/set-game game_id (assoc-in (assoc-in game [:game :board] board) [:game :turn] (if (= ((vec ttt.spec/players?) 0) turn) ((vec ttt.spec/players?) 1) ((vec ttt.spec/players?) 0))))
                                              {:status "ok" :content (json/write-str board)})
        :else {:status "bad" :content (json/write-str "Move not available")})))

