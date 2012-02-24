(ns game_of_life.world
  (:require [game_of_life.cell :as cell])
  (:require-macros [name.benjaminpeter.util :as util])
)

(defprotocol World
  (neighbours_of [this cell] [this x y]
    (str "Returns a map of all neighbour cells of the given cell. "
         "The map consists of the compass direction of the cell "
         "like :n and :se and the cell object for living cells and "
         "nil for dead cels."
    )
  )
  (all_neighbours_of [this cell create_cell] [this x y create_cell]
    (str "Similar to neighbours_of but uses create_cell to create "
         "living and dead cells into a returning map."
    )
  )
  (living_cells [this] "Returns all living cells as lazy seq.")
  (invigorate [this cell] "Adds cell as living cell to the world.")
  (alive [this x y] "Tests if the cell at x y is alive.")
  (kill [this cell] [this x y] "Kill the cell at x y.")
  (retrieve [this x y] "Returns the cell at x y. Dead cells just return nil.")
)

(defrecord WorldKey [x y])

(defn- make_key [cell]
  (WorldKey. (:x cell) (:y cell))
)

(defn- neighbour_keywords []
  [:nw :n :ne :w :e :sw :s :se]
)

(defrecord SimpleWorld [
    grid
  ]

  World
  (living_cells [this] (vals grid))
  (invigorate [this cell]
    (SimpleWorld. (assoc grid (make_key cell) cell))
  )
  (alive [this x y]
    (if-let [cell (get grid (WorldKey. x y))]
      (:alive cell)
      false
    )
  )
  (kill [this cell]
    (SimpleWorld. (dissoc grid (make_key cell)))
  )
  (kill [this x y]
    (kill this (WorldKey. x y))
  )
  (retrieve [this x y]
    (get grid (WorldKey. x y))
  )
  (all_neighbours_of [this cell create_cell]
  ; bpeter todo macro the zipmap thing?
    (zipmap
      (neighbour_keywords)
      (for [
          xOffset [-1 0 1] yOffset [-1 0 1]
          :when (not (= 0 xOffset yOffset))
        ]
        (let [cur_x (+ (:x cell) xOffset)
              cur_y (+ (:y cell) yOffset)]
          (if-let [living_cell (retrieve this cur_x cur_y)]
            living_cell
            (create_cell cur_x cur_y false)
          )
        )
      )
    )
  )
  (all_neighbours_of [this x y create_cell]
    (all_neighbours_of this (WorldKey. x y) create_cell)
  )
  (neighbours_of [this cell]
    (zipmap
      (neighbour_keywords)
      (for [
          xOffset [-1 0 1] yOffset [-1 0 1]
          :when (not (= 0 xOffset yOffset))
        ]
        (let [cur_x (+ (:x cell) xOffset)
              cur_y (+ (:y cell) yOffset)]
          (retrieve this cur_x cur_y)
        )
      )
    )
  )
  (neighbours_of [this x y]
    (neighbours_of this (WorldKey. x y))
  )
)

(defn new_world
  ([] (SimpleWorld. {}))
)

(defn number_of [neighbours]
  "Returns the number of entries in a neighbours map."
  (count (vals neighbours))
)

(defn living [neighbours]
  "Returns a map of neighbours only containing living cells."
  (util/filter-map [key val] (:alive val) neighbours)
)

(defn dead [neighbours]
  "Returns a map of neighbours only containing dead cells."
  (util/filter-map [key val] (not (:alive val)) neighbours)
)

