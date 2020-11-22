;; CLI tic-tac-toe functions
(ns tic-tac-toe.cli
  (:require [clojure.spec.alpha :as s]
            [tic-tac-toe.core :as ttt.core]
            [tic-tac-toe.spec :as ttt.spec]))

(defn get-position-input [player]
  "Get the position a player wishes to move"
  (let [position (Integer/parseInt (read-line))]
    (when (s/valid? ::ttt.spec/position position) position)))

(defn take-turn [player board]
  "Take a turn from keyboard input"
  (let [player player
        board board]
    (println (str (clojure.string/upper-case (name player)) "'s turn: ") "Enter a number between 1-9")
    (loop [position (get-position-input player)]
      (if (and position (ttt.core/can-move? position board))
        (ttt.core/make-move player position board)
        (do
          (println "The chosen move is not available, please chose again")
          (recur (get-position-input player)))))))

(defn play-game [starting-board turns]
  "Main game loop"
  (let [starting-board starting-board
        turns turns])
  (loop [board starting-board
         turns turns]
    (ttt.core/draw-board board)
    (let [winner (ttt.core/winner? board)]
      (cond
        winner (println (str (clojure.string/upper-case (name winner)) " wins!"))
        (ttt.core/draw? board) (println "It's a draw!")
        :else
        (recur
         (take-turn (first turns) board)
         (rest turns))))))

(defn -main [& args]
  "CLI entry point"
  (play-game ttt.core/clear-board (ttt.core/turns 9)))

