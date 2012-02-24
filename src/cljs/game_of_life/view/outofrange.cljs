(ns game_of_life.view.outofrange)

(defn ignore [x y]
  (.log js/console (str "out of range, ignore " x "x" y))
  [x y]
)

(defn exception [x y]
  (.log js/console (str "out of range, exception " x "x" y))
  (throw (js/RangeError. (str "Illegal Argument given x: " x " y: " y)))
)
