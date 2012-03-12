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
  (util/with_exception_handler view/exception_handler
    (next_gen view (builder/from_string blinker cell/new_cell))
  )
))

(defn append_content [dom & string]
  (set! (.-innerHTML dom) (apply str (.-innerHTML dom) string)))

(defn add_test [out name testfn]
  (util/with_exception_handler
    #(append_content out "<span style='color: red;'>Exception: " %1 "</span>")
      (do (append_content out " * " name " -> ")
        (append_content out (
          if (testfn) "<span style='color: green;'>ok</span>"
              "<span style='color: red;'>nok</span>"))))
  (append_content out "</br>"))

(defn test_zipmap []
  (= {"a" 1, "b" 2, "c" 3, "d" 4, "e" 5, "f" 6, "g" 7, "h" 8, "i" 9, "j" 10, "k" 11}
    (zipmap
      ["a" "b" "c" "d" "e" "f" "g" "h" "i" "j" "k"]
      [1 2 3 4 5 6 7 8 9 10 11])))

(defn ^:export test []
  (doto (domh/get-element "ascii")
    (add_test "test zipmap" test_zipmap)
    (add_test "cell survives" cell/test_survives)
    (add_test "cell invigorates" cell/test_invigorate)
    (add_test "next cell action" cell/test_next_state)
    (add_test "world for when" world/test_for_when)
    (add_test "world for when 2d" world/test_for_when_2d)
    (add_test "world for beta" world/test_for_beta)
    (add_test "world double eq" world/test_double_eq)
    (add_test "world double eq all" world/test_double_eq_all)
    (add_test "world test add" world/test_add)
    (add_test "world test add multi" world/test_add_multi)
    (add_test "world test alive kill" world/test_alive_kill)
    (add_test "world test map eq" world/test_vector_eq)
    (add_test "world test nb not self" world/test_neighbours_not_self)
    (add_test "world test nb keys" world/test_neighbours_keys)
    (add_test "world test nb none" world/test_neighbours_none)
    (add_test "world test nb count" world/test_neighbours_count)
    (add_test "world test nb list empty" world/test_neighbours_list_empty)
    (add_test "world test nb iteration" world/test_neighbour_iteration)
    (add_test "world test all nb iteration" world/test_all_neighbour_iteration)
  ))

