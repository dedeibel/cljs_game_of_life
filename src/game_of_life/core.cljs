(ns game_of_life.core
  (:require 
            [twitterbuzz.dom-helpers :as domh]
            [game_of_life.cell :as cell]
            [game_of_life.world_printer :as printer]
            [game_of_life.world_builder :as builder]
            [game_of_life.game :as game]
            [goog.graphics :as graphics])
)

(def DEFAULT-WIDTH 440)

(def DEFAULT-HEIGHT 440)

(def stroke-black (graphics/Stroke. 1 "#000"))
(def stroke-white (graphics/Stroke. 1 "#FFF"))

(def fill-white (graphics/SolidFill. "#fff"))
(def fill-black (graphics/SolidFill. "#000"))

(def g (doto (graphics/createGraphics 
              (str DEFAULT-WIDTH) (str DEFAULT-HEIGHT))
             (.render (domh/get-element "graphics"))))

(def blinker
" x 
, x
, x"
)

(def bipole
"xx 
,xx
,  xx
,  xx"
)

(def oscyl
"   xx
,  x  x
, x    x
,x      x
,x      x
, x    x
,  x  x
,   xx"
)

(def term54
"xxx
,x x
,x x
,    
,x x
,x x
,xxx"
)

(defn next_gen [world]
  (str "Calculates and displays the next generation of the world. "
       "After each generation it waits for the user to press enter.")
  (println (printer/to_string world))
  (read-line)
  (recur (game/next_generation world))
)

(defn ^:export init []
  (.clear g true)
  (.drawRect g  10 10 100 200 stroke-black fill-white)
  (.drawCircle g  20 20 2 stroke-black fill-black)
)

(defn -main []
  (next_gen (builder/from_string term54 cell/new_cell))
)

