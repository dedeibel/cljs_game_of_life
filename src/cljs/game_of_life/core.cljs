(ns game_of_life.core
  (:require 
            [twitterbuzz.dom-helpers :as domh]
            [game_of_life.cell :as cell]
            [game_of_life.world_printer :as printer]
            [game_of_life.world_builder :as builder]
            [game_of_life.game :as game]
            [game_of_life.view :as view]
            [game_of_life.world :as world]
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

(defn call_after [ms callback]
  (js/setTimeout callback ms))

(defn next_gen [view world]
  (.log js/console "paint")
  (view/clear view)
  (doseq [living (world/living_cells world)]
    (view/draw_living view (+ 10 (:x living)) (+ 10 (:y living))))
  (call_after 2000 #(next_gen view (game/next_generation world))))

; Zwei zeichen Versionen
; - locked view, statischer Ausschnitt
; - all view, linker oberer rand immer so dass alles zu sehen ist
; - skalieren vorsehen

(defn ^:export init []
    (let [view (-> (view/new_world (view/ViewOptions. 100 100 5 5 outofrange/ignore) (domh/get-element "graphics")))]
;  (util/with_exception_handler view/exception_handler
    (next_gen view (builder/from_string oscyl cell/new_cell))
;  )
  )
)

(defn -main []
)

