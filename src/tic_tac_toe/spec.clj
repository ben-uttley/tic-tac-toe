;; Spec schemas
(ns tic-tac-toe.spec
  (:require [clojure.spec.alpha :as s]))

(def players? #{:o :x})
(def positions? (set (range 1 10)))
(s/def ::player players?)
(s/def ::position positions?)
(s/def ::space (s/or :empty ::position
                     :taken ::player))
(s/def ::row1 (s/tuple (s/or :taken ::player :empty #{1})
                       (s/or :taken ::player :empty #{2})
                       (s/or :taken ::player :empty #{3})))
(s/def ::row2 (s/tuple (s/or :taken ::player :empty #{4})
                       (s/or :taken ::player :empty #{5})
                       (s/or :taken ::player :empty #{6})))
(s/def ::row3 (s/tuple (s/or :taken ::player :empty #{7})
                       (s/or :taken ::player :empty #{8})
                       (s/or :taken ::player :empty #{9})))
(s/def ::row (s/or :row1 ::row1 :row2 ::row2 :row3 ::row3))
(s/def ::board (s/tuple ::row1 ::row2 ::row3))
(s/def ::win-line (s/coll-of ::space :count 3 :into ()))
(s/def ::win-lines (s/every ::win-line :count 8 :into ()))

;; TODO define specs for db game and player game
