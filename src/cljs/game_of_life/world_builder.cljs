(ns game_of_life.world_builder
  (:require [game_of_life.world :as world])
  (:require-macros [name.benjaminpeter.util :as util])
)

(defprotocol CellBuilder
  "Builds a 2D world, one cell at a time from top left to bottom right."
  (living    [this] "Adds a living cell at the current position.")
  (dead      [this] "Adds a dead cell at the current position.")
  (linebreak [this] "Moves on to the next line of cells.")
  (world     [this] "Returns the built world.")
)

(defrecord CellBuilderCursor [x y world new_cell]
  CellBuilder
  (linebreak [this] (CellBuilderCursor. 0 (inc y) world new_cell))
  (dead      [this] (CellBuilderCursor. (inc x) y world new_cell))
  (living    [this] (CellBuilderCursor. (inc x) y
                  (world/invigorate world (new_cell x y)) new_cell))
  (world     [this] world)
)

(defn- parse_character [builder world_desc]
  (str "Parses one character at the time from world_desc string, "
       "stopping when none are left.")
  (if-let [desc (seq world_desc)]
    (recur 
      (condp = (first desc)
        \X (living builder)
        \x (living builder)
        \. (dead builder)
        \space (dead builder)
        \newline (linebreak builder)
        \, builder
        (throw (IllegalArgumentException.
          (str "Invalid input char" (first desc))))
      )
      (rest desc)
    )
    (world builder)
  )
)

(defn- new_builder [world new_cell]
  (CellBuilderCursor. 0 0  world new_cell)
)

(defn from_string [world_desc new_cell]
  (str "Creates a 2D game of life world from a string. The string "
       " has the following structure.\newline"
       "space or . m    - dead cell\newline"
       "x or X          - living cell\newline"
       "newline         - define next line of world\newline"
       ",               - is ignored\newline"
  )
  (parse_character (new_builder (world/new_world) new_cell) world_desc)
)

