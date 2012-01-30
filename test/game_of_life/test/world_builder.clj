(ns game_of_life.test.world_builder
  (:use [game_of_life.world :only (new_world invigorate)])
  (:use [game_of_life.cell  :only (new_cell)])
  (:use [game_of_life.test.util :only (with-private-fns)])
  (:use [game_of_life.world_builder])
  (:use [clojure.test])
)

(with-private-fns [game_of_life.world_builder [new_builder dead living linebreak]]
  (deftest test_cursor
    (is (=
      (-> (new_world)
        ,,, (invigorate (new_cell 1 0)) (invigorate (new_cell 2 0))
        (invigorate (new_cell 0 1)) ,,, (invigorate (new_cell 2 1)))
      (:world (-> (new_builder (new_world) new_cell)
        (dead) (living) (living) (linebreak)
        (living) (dead) (living)))
    ))
  )
)

(deftest test_from_string
  (is (=
    (-> (new_world)
      ,,, (invigorate (new_cell 1 0)) (invigorate (new_cell 2 0))
      (invigorate (new_cell 0 1)) ,,, (invigorate (new_cell 2 1)))
    (from_string 
" XX
,X X"
      new_cell
    )
  ))
  (is (=
    (-> (new_world)
 ,,, (invigorate (new_cell 1 0)) ,,,
 (invigorate (new_cell 0 1)) (invigorate (new_cell 1 1)) (invigorate (new_cell 2 1))
 ,,, (invigorate (new_cell 1 2)) ,,,
    )
    (from_string 
" X 
,XXX
, X "
      new_cell
    )
  ))
)
