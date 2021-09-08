(ns tst.demo.core
  (:use demo.core tupelo.core tupelo.test)
  (:require
    [tupelo.string :as str])
  (:import
    [java.time LocalDate]
    ))

(dotest
  (let [ldate-1  (LocalDate/parse "1999-01-02")
        orig     [1 {:b ldate-1}]
        expected [1 {:b "<LocalDate 1999-01-02>"}]]
    (isnt (instance? LocalDate "1999-01-02"))
    (is (instance? LocalDate ldate-1))
    (is= "<LocalDate 1999-01-02>" (LocalDate->tagstr ldate-1)) ; #todo need inverse tagstr->LocalDate
    (is= expected (walk-LocalDate->str orig))))

;---------------------------------------------------------------------------------------------------
(dotest
  ; verify pad-left
  (is= "02" (str/pad-left "2" 2 \0))
  (is= "23" (str/pad-left "23" 2 \0))

  ; verify can parse sloppy date format
  ; can accept 1 or 2 digit month/day, and either slash or hyphen separators
  (is= (LocalDate/parse "1999-01-02")
    (mdy-str->LocalDate "1/2/1999")
    (mdy-str->LocalDate "1-2-1999")
    (mdy-str->LocalDate "01/2/1999")
    (mdy-str->LocalDate "1-02-1999"))

  ; verify DateTimeFormatter
  (is= (.format date-time-formatter-compact (mdy-str->LocalDate "1/2/1999")) "1/2/1999")
  (is= (.format date-time-formatter-compact (mdy-str->LocalDate "11/12/1999")) "11/12/1999")

  ; verify LocalDate values sort OK via standard comparator from clojure
  (let [d1 (LocalDate/parse "1999-01-01")
        d2 (LocalDate/parse "1999-01-02")]
    (is= (mapv str (sort [d2 d1]))
      ["1999-01-01"
       "1999-01-02"])))

