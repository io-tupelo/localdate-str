(ns demo.core
  (:use tupelo.core)
  (:require
    [camel-snake-kebab.core :as csk]
    [clojure.java.io :as io]
    [clojure.walk :as walk]
    [schema.core :as s]
    [tupelo.string :as str]
    [tupelo.schema :as tsk]
    )
  (:import
    [java.time LocalDate]
    [java.time.format DateTimeFormatter]
    ))

; will output dates like `1/2/1999` and `11/12/1999`
(def date-time-formatter-compact (DateTimeFormatter/ofPattern "M/d/yyyy"))

(s/defn mdy-str->LocalDate :- LocalDate
  "Parse a sloppy date string like `M/D/YYYY` or `M-D-YYYY` into a LocalDate"
  [date-str :- s/Str]
  ; #todo add more error checking for invalid strings
  (let [parts  (str/split date-str #"/|-")
        month  (str/pad-left (nth parts 0) 2 \0)
        day    (str/pad-left (nth parts 1) 2 \0)
        year   (nth parts 2)
        ldstr  (format "%s-%s-%s" year month day)
        result (LocalDate/parse ldstr)]
    result))

(s/defn format-LocalDate-compact :- s/Str
  "Formats a LocalDate into a compact format like  `1/2/1999` and `11/12/1999`"
  [localdate :- LocalDate]
  (.format date-time-formatter-compact localdate))

; helper functions for testing
(s/defn LocalDate->tagstr :- s/Str
  [arg] (str "<LocalDate " arg ">"))

(defn walk-LocalDate->str
  [data]
  (walk/postwalk (fn [item]
                   (cond-it-> item
                     (instance? LocalDate it) (LocalDate->tagstr it)))
    data))

(defn walk-format-LocalDate-compact
  [data]
  (walk/postwalk (fn [item]
                   (cond-it-> item
                     (instance? LocalDate it) (format-LocalDate-compact it)))
    data))

