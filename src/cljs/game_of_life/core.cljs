(ns game_of_life.core
  (:require 
            [twitterbuzz.dom-helpers :as domh]
            [game_of_life.cell :as cell]
            [game_of_life.world_printer :as printer]
            [game_of_life.world_builder :as builder]
            [game_of_life.game :as game]
            [game_of_life.view :as view]
            [game_of_life.view.outofrange :as outofrange]
            [goog.graphics :as graphics])
  (:require-macros [name.benjaminpeter.util :as util])
)

(def DEFAULT-WIDTH 440)

(def DEFAULT-HEIGHT 440)

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

; Zwei zeichen Versionen
; - locked view, statischer Ausschnitt
; - all view, linker oberer rand immer so dass alles zu sehen ist
; - skalieren vorsehen

(defn ^:export init []
;  (util/add_exception_handler view/exception_handler
  (try
    (-> (view/new_world (view/ViewOptions. 100 100 5 5 outofrange/exception) (domh/get-element "graphics"))
      (view/draw_living  0  0)
      (view/draw_living 10 10)
      (view/draw_living 20 20)
      (view/draw_living -30 30)
    )
    (catch js/Object ex (view/exception_handler ex))
  )
)

(defn -main []
  (next_gen (builder/from_string term54 cell/new_cell))
)

