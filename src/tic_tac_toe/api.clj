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
        game {:game {:id game_id :board ttt.core/clear-board}}
        player {:id (str (uuid/v1)) :display (first (ttt.core/turns 1))}]
    (ttt.store/set-game game_id (assoc-in game [:players] [player]))
    (json/write-str (assoc-in game [:player] player))))

(defn join-game [game_id]
  "Join a tic-tac-toe-game"
  (let [game_db (ttt.store/get-game game_id)
        game_player (game_db :game)
        player {:id (str (uuid/v1)) :display (second (ttt.core/turns 2))}]
    (ttt.store/set-game game_id (assoc-in game_db [:players 1] player))
    (json/write-str (assoc-in game_player [:player] player))))

(defn make-move [game_id player_id position]
  "Make a move on a given tic-tac-toe game for a given player"
  (let [game (ttt.store/get-game game_id)
        player (s/assert ::ttt.spec/player (ttt.store/get-player game_id player_id))
        board (s/assert ::ttt.spec/board ((game :game) :board))
        position (s/assert ::ttt.spec/position position)]
    (when (ttt.core/can-move? position board)
      (let [board (s/assert ::ttt.spec/board (ttt.core/make-move player position board))]
        (ttt.store/set-game game_id (assoc-in game [:game :board] board))
        (json/write-str board)))))

