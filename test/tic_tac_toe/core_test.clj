(ns tic-tac-toe.core-test
  (:require [clojure.test :refer :all]
            [tic-tac-toe.core :refer :all :as ttt.core]
            [tic-tac-toe.spec :as ttt.spec]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [clojure.spec.gen.alpha :as gen]
            [clojure.pprint :as pp]))

(s/fdef ttt.core/win-lines
  :args (s/cat :board ::ttt.spec/board)
  :ret ::ttt.spec/win-lines)

(s/fdef ttt.core/make-move
  :args (s/cat :player ::ttt.spec/player
               :position ::ttt.spec/position 
               :board ::ttt.spec/board)
  :ret ::ttt.spec/board)

(s/fdef ttt.core/can-move?
  :args (s/cat :position ::ttt.spec/position
               :board ::ttt.spec/board)
  :ret boolean?)

(s/fdef ttt.core/winner?
  :args (s/cat :board ::ttt.spec/board)
  :ret (s/nilable ::ttt.spec/player))

(s/fdef ttt.core/draw?
  :args (s/cat :board ::ttt.spec/board)
  :ret boolean?)

(def ^:private function-specifications
  [`ttt.core/win-lines
   `ttt.core/make-move
   `ttt.core/can-move?
   `ttt.core/winner?
   `ttt.core/draw?])

(defn instrument-all-functions []
  (stest/instrument function-specifications))

(defn unstrument-all-functions []
  (stest/unstrument function-specifications))

(gen/generate (s/gen ::ttt.spec/position))

(defn run-generative-tests []
  (instrument-all-functions)
  (stest/check `ttt.core/win-lines)
  (stest/check `ttt.core/make-move)
  (stest/check `ttt.core/can-move?)
  (stest/check `ttt.core/winner?)
  (stest/check `ttt.core/draw?)
  (unstrument-all-functions))

(run-generative-tests)
