(ns name.benjaminpeter.util)

(defmacro filter-map [bindings pred m]
  `(select-keys ~m
    (for [~bindings ~m
      :when ~pred]
      ~(first bindings)
    )
  )
)

(defmacro add_exception_handler [handler function]
  `(try ~function (catch js/Object ex# (~handler ex#)))
)

