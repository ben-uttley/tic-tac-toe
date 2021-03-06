;; Core tic-tac-toe functionality
(ns tic-tac-toe.core
  (:require [clojure.spec.alpha :as s]
            [tic-tac-toe.spec :as ttt.spec]))

(def clear-board
  "The tic-tac-toe board"
  [[1 2 3]
   [4 5 6]
   [7 8 9]])

(defn construct-row [row]
  "Draws a row"
  (let [row (s/assert ::ttt.spec/row row)]
    (print "|"
           (let [space (first row)]
             (if (number? space) (str space) (clojure.string/upper-case (name space))))
           "|"
           (let [space (second row)]
             (if (number? space) (str space) (clojure.string/upper-case (name space))))
           "|"
           (let [space (last row)]
             (if (number? space) (str space) (clojure.string/upper-case (name space))))
           "|")))

(defn draw-board [board]
  "Draws the board"
  (let [board (s/assert ::ttt.spec/board board)]
    (println "")
    (println "Current board:")
    (println "+---+---+---+")
    (println (str (construct-row (first board))))
    (println "+---+---+---+")
    (println (str (construct-row (second board))))
    (println "+---+---+---+")
    (println (str (construct-row (last board))))
    (println "+---+---+---+")
    (println "")))

(defn win-lines [board]
  "Returns all the possible win lines"
  (let [board board]
    (conj ()
            (seq (first board))
            (seq (second board))
            (seq (last board))
            (seq (for [row board]
                   (first row)))
            (seq (for [row board]
                   (second row)))
            (seq (for [row board]
                   (last row)))
            (take-nth 4 (flatten board))
            (take-nth 2 (drop-last 2 (drop 2 (flatten board)))))))

(defn make-move [player position board]
  "Places the player in position of the board and returns the updated board"
  (let [player player
        position position
        board board]
    (cond 
         (= 0 (quot (dec position) 3)) [(replace {position player} (nth board 0)) 
                                        (nth board 1) 
                                        (nth board 2)]
         (= 1 (quot (dec position) 3)) [(nth board 0) 
                                        (replace {position player} (nth board 1)) 
                                        (nth board 2)]
         (= 2 (quot (dec position) 3)) [(nth board 0) 
                                        (nth board 1) 
                                        (replace {position player} (nth board 2))])))

(defn can-move? [position board]
  "Checks if a move in position of the board can be made and returns a boolean"
  (let [position position
        board board]
    (cond
      (= 0 (quot (dec position) 3)) (number? (nth (nth board 0) (dec position)))
      (= 1 (quot (dec position) 3)) (number? (nth (nth board 1) (- position 4)))
      (= 2 (quot (dec position) 3)) (number? (nth (nth board 2) (- position 7))))))

(defn winner? [board]
  "Checks the board to see if the game has been won"
  (let [board board
        player1 ((vec ttt.spec/players?) 0)
        player2 ((vec ttt.spec/players?) 1)]
    (cond
      (= (repeat 3 player1) (flatten (filter #(every? #{player1} %) (win-lines board)))) player1
      (= (repeat 3 player2) (flatten (filter #(every? #{player2} %) (win-lines board)))) player2)))

(defn draw? [board]
  "Is the board a draw (full)?"
  (let [board board]
    (empty? (filter number? (flatten board)))))

(defn turns [i]
  "Lazy seq of :x :o turns"
  (take i (cycle (vec ttt.spec/players?))))

