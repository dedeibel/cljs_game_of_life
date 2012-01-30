(ns game_of_life.core
  (:require [game_of_life.cell :as cell])
  (:require [game_of_life.world_printer :as printer])
  (:require [game_of_life.world_builder :as builder])
  (:require [game_of_life.game :as game])
)

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

(defn -main []
  (next_gen (builder/from_string term54 cell/new_cell))
)

