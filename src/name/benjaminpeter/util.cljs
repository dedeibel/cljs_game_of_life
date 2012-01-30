(ns name.benjaminpeter.util)

(defmacro filter-map [bindings pred m]
  `(select-keys ~m
    (for [~bindings ~m
      :when ~pred]
      ~(first bindings)
    )
  )
)

