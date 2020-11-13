(ns tic-tac-toe.core
  (:gen-class))

(def clear-board
  "The tic-tac-toe board"
  [[1 2 3]
   [4 5 6]
   [7 8 9]])

(defn construct-row [row]
  "Draws a row"
  (print "|"
         (let [space (first row)]
           (if (number? space) (str space) (clojure.string/upper-case (name space))))
         "|"
         (let [space (second row)]
           (if (number? space) (str space) (clojure.string/upper-case (name space))))
         "|"
         (let [space (last row)]
           (if (number? space) (str space) (clojure.string/upper-case (name space))))
         "|"))

(defn draw-board [board]
  "Draws the board"
  (println "")
  (println "Current board:")
  (println "+---+---+---+")
  (println (str (construct-row (first board))))
  (println "+---+---+---+")
  (println (str (construct-row (second board))))
  (println "+---+---+---+")
  (println (str (construct-row (last board))))
  (println "+---+---+---+")
  (println ""))

(defn win-lines [board]
  "Returns all the possible win lines"
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
   (take-nth 2 (drop-last 2 (drop 2 (flatten board))))))

(defn make-move [player position board]
  "Places the player in position of the board and returns the updated board"
  (cond 
    (= 0 (quot (dec position) 3)) [(replace {position player} (nth board 0)) (nth board 1) (nth board 2)]
    (= 1 (quot (dec position) 3)) [(nth board 0) (replace {position player} (nth board 1)) (nth board 2)]
    (= 2 (quot (dec position) 3)) [(nth board 0) (nth board 1) (replace {position player} (nth board 2))]))

(defn can-move? [position board]
  "Checks if a move in position of the board can be made and returns a boolean"
  (cond
    (= 0 (quot (dec position) 3)) (number? (nth (nth board 0) (dec position)))
    (= 1 (quot (dec position) 3)) (number? (nth (nth board 1) (- position 4)))
    (= 2 (quot (dec position) 3)) (number? (nth (nth board 2) (- position 7)))))

(defn winner? [board]
  "Checks the board to see if the game has been won"
  (cond
    (= (seq '((:x :x :x))) (filter #(every? #{:x} %) (win-lines board))) :x
    (= (seq '((:o :o :o))) (filter #(every? #{:o} %) (win-lines board))) :o))

(defn draw? [board]
  "Is the board a draw (full)?"
  (empty? (filter number? (flatten board))))

(defn get-position-input [player]
  "Get the position a player wishes to move"
  (let [position (Integer/parseInt (read-line))]
    (when (<= 1 position 9) position)))

(defn take-turn [player board]
  "Take a turn from keyboard input"
  (println (str (clojure.string/upper-case (name player)) "'s turn: ") "Enter a number between 1-9")
  (loop [position (get-position-input player)]
    (if (and position (can-move? position board))
        (make-move player position board)
        (do
          (println "The chosen move is not available, please chose again")
          (recur (get-position-input player))))))

(defn play-game [starting-board turns]
  "Main game loop"
  (loop [board starting-board
         turns turns]
    (draw-board board)
    (let [winner (winner? board)]
      (cond
        winner (println (str (clojure.string/upper-case (name winner)) " wins!"))
        (draw? board) (println "It's a draw!")
        :else
        (recur
         (take-turn (first turns) board)
         (rest turns))))))

(def turns
  "Lazy seq of :x :o turns"
  (cycle [:x :o]))

(defn -main [& args]
  "Program entry point"
  (play-game clear-board turns))
