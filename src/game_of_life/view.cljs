(ns game_of_life.view
  (:require 
            [goog.graphics :as graphics])
)

(defprotocol World
  (draw_living [this x y])
  (draw_dead   [this x y])
  (clear       [this])
)

(def BLACK "#000")
(def WHITE "#FFF")

(def cell_width 5)
(def cell_height cell_width)

; Returns: {:x :y :w :h}
(defn- cell_geometry [x y]
  {:x (* x cell_width) :y (* y cell_height) :w cell_width :h cell_height}
)

; Returns: RectElement
(defn- draw_cell [canvas x y color]
  (let [g (cell_geometry x y)]
    (.drawRect canvas (:x g) (:y g) (:w g) (:h g)
      (graphics/Stroke. color) (graphics/SolidFill. color))
  )
)

(defn- draw_grit [world]
  world
)

(defn- make_key [x y]
  [x y]
)

(defrecord BasicWorld [canvas_graphics cells]
  World
  (draw_living [this x y]
    (let [cell_key (make_key x y)]
      (if-not (cells cell_key)
        (BasicWorld.
          canvas_graphics 
          (assoc cells cell_key
            (draw_cell canvas_graphics x y BLACK)))
         this
      )
    )
  )
  (draw_dead [this x y]
    (let [cell_key (make_key x y) cell (cells cell_key)]
      (if cell
        (do
          (.removeElement canvas_graphics cell)
          (BasicWorld. canvas_graphics (dissoc cells cell_key))
        )
        (throw (IllegalArgumentException. (str "No such living cell found " x "," y)))
      )
    )
  )
  (clear [this x y]
    (do
      (.clear canvas_graphics)
      (draw_grit (BasicWorld. canvas_graphics {}))
    )
  )
)

(defn new_world [dom_element width height]
  (draw_grit (BasicWorld.
    (doto (graphics/createGraphics
            (* cell_width width) (* cell_height height))
          (.render dom_element))
    {}
  ))
)

