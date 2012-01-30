(ns game_of_life.test.game
  (:use [game_of_life.world])
  (:use [game_of_life.cell])
)

(def stable (-> (new_world)   (invigorate (new_cell 1 0))
  (invigorate (new_cell 0 1))                            (invigorate (new_cell 2 1))
                              (invigorate (new_cell 1 2)))
)

(def blinker (-> (new_world) 
  (invigorate (new_cell 0 1)) (invigorate (new_cell 1 1)) (invigorate (new_cell 2 1))
))

