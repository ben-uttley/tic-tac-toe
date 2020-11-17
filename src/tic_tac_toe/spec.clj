;; Spec schemas
(ns tic-tac-toe.spec
  (:require [clojure.spec.alpha :as s]))

(def players? #{:x :o})
(s/def ::player players?)
(def valid-position-regex #"^[1-9]{1}$")
(s/def ::position (s/and number? #(<= 1 % 9)))
(s/def ::space (s/or :empty ::position
                     :taken ::player))
(s/def ::row (s/tuple ::space ::space ::space))
(s/def ::board (s/coll-of ::row :kind vector? :count 3 :into []))
(s/def ::win-line (s/coll-of ::space :kind coll? :count 3 :into ()))
(s/def ::win-lines (s/coll-of ::win-line :kind coll? :count 8 :into ()))
