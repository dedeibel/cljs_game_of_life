(ns name.benjaminpeter.util)

(defmacro filter-map [bindings pred m]
  `(select-keys ~m
    (for [~bindings ~m
      :when ~pred]
      ~(first bindings)
    )
  )
)

; Only usefull for clojurescript! try catch without class type.
(defmacro with_exception_handler [handler function]
  `(try ~function (catch ex# (~handler ex#)))
)

;(defmacro case [value & statements]
;  (for [[condition action] (partition 2 statements)]
;    ;`(prn ~condition " ac " ~action))) ; test
;    `(if-not action
;      condition
;      (if condition action ~(case value (rest (rest statements 2)))))))

