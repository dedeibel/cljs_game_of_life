(ns game_of_life.cell)

(defprotocol Cell
  (next_cell_state [cell number_of_neighbours])
  (next_cell_action [cell number_of_neighbours])
  (revive [cell])
)

(defn- survives [number_of_neighbours]
  (cond
    (= 2 number_of_neighbours) true
    (= 3 number_of_neighbours) true
    true false
  )
)

(defn- comes_to_life [number_of_neighbours]
    (= number_of_neighbours 3)
)

(defrecord BasicCell [x y alive]
  Cell
  (next_cell_state [this number_of_neighbours]
    (if (alive)
  		(survives number_of_neighbours)
  		(comes_to_life number_of_neighbours)
    )
  )
  (next_cell_action [this number_of_neighbours]
    (cond
      (and      alive  (not (survives number_of_neighbours))) :kill
      (and (not alive) (comes_to_life number_of_neighbours))  :revive
      alive       :keep-alive
      (not alive) :stay-dead
    )
  )
)

(defn revive [cell]
  "Creates a cell at the same position of this one but in living state."
  (BasicCell. (:x cell) (:y cell) :alive)
)

(defn new_cell 
  "New cell constructor. A cell has a 2D position and living / dead state"
  ([x y]        (BasicCell. x y true))
  ([x y alive]  (BasicCell. x y alive))
)

