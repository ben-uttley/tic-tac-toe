;; File store
(ns tic-tac-toe.store
  (:require [clojure.data.json :as json]
            [clj-uuid :as uuid]
            [codax.core :as c]))

(def db (c/open-database! "data/tic-tac-toe.db"))

(when (nil? (c/get-at! db [:game_tally])) (c/assoc-at! db [:game_tally] 1))

(defn get-next-game-id []
  (c/assoc-at! db [:game_tally] (inc (c/get-at! db [:game_tally]))))

(defn set-game [game_id game]
  (c/assoc-at! db [:games game_id] game))

(defn get-game [game_id]
  (c/get-at! db [:games game_id]))

(defn get-board [game_id]
  (c/get-at! db [:games game_id :game :board]))

(defn get-player [game_id player_id]
  ((first (filter #(= (% :id) player_id) ((get-game game_id) :players))) :display))
