(ns game_of_life.view
  (:require 
            [goog.graphics  :as graphics]
            [goog.ui.Dialog :as Dialog]
            [goog.ui.Dialog.ButtonSet :as ButtonSet]
            [goog.events    :as events])
)

(def BLACK "#000")
(def GRAY  "#CCC")
(def WHITE "#FFF")

(defprotocol World
  (draw_living [this x y])
  (draw_dead   [this x y])
  (clear       [this])
)

(defprotocol ViewOptionsProtocol
    (columns       [this])
    (rows          [this])
    (cell_width    [this])
    (cell_height   [this])
    (width         [this])
    (height        [this])
    ; Returns: {:x :y :w :h}
    (cell_geometry [this x y])
    (living_color  [this])
    (grid_color    [this])
)

; Returns: RectElement
(defn- draw_cell [canvas geometry color]
  (.drawRect canvas (:x geometry) (:y geometry) (:w geometry) (:h geometry)
    (graphics/Stroke. color) (graphics/SolidFill. color))
)

(defn- draw_grit [world]
  world
)

(defn- make_key [x y]
  [x y]
)

(defrecord ViewOptions [columns rows cell_width cell_height out_of_rangefn]
  ViewOptionsProtocol
    (columns      [this] columns)
    (rows         [this] rows)
    (cell_width   [this] cell_width)
    (cell_height  [this] cell_height)
    (width        [this] (* columns cell_width))
    (height       [this] (* rows    cell_height))
    (living_color [this] BLACK)
    (grid_color   [this] GRAY)
    (cell_geometry [this x y]
      {:x (* x cell_width) :y (* y cell_height) :w cell_width :h cell_height}
    )
)

(defn- check_range [view_settings x y]
  (.log js/console (str "check range " x "x" y))
  (if (or (< x 0) (< y 0)
          (> x (columns view_settings)) (> y (rows view_settings)))
    ((:out_of_rangefn view_settings) x y)
    [x y]
  )
)

(defrecord BasicWorld [view_settings canvas_graphics cells]
  World
  (draw_living [this x y]
    (let [[x y] (check_range view_settings x y)
          cell_key (make_key x y)]
      (if-not (cells cell_key)
        (assoc this :cells
          (assoc cells cell_key
            (draw_cell canvas_graphics 
              (cell_geometry view_settings x y) (living_color view_settings))))
         this
      )
    )
  )
  (draw_dead [this x y]
    (let [[x y] (check_range this x y)
          cell_key (make_key x y) cell (cells cell_key)]
      (if cell
        (do
          (.removeElement canvas_graphics cell)
          (assoc this :cells (dissoc cells cell_key))
        )
        (throw (IllegalArgumentException. (str "No such living cell found " x "," y)))
      )
    )
  )
  (clear [this x y]
    (do
      (.clear canvas_graphics)
      (draw_grit (assoc this :cells {}))
    )
  )
)

(defn new_world [view_settings dom_element]
  (draw_grit (BasicWorld.
    view_settings
    (doto (graphics/createGraphics (width view_settings) (height view_settings))
          (.render dom_element))
    {}
  ))
)

(defn exception_handler
  "Displays the exception using a google closure ui Dialog"
  [exception]
  (doto (goog.ui.Dialog. nil true)
    (.setContent (str exception))
    (.setTitle "Exception")
    (.setButtonSet (.createOk goog.ui.Dialog.ButtonSet))
    (.setDraggable true)
    (.setModal false)
    (.setVisible true)
  )
  nil
)

