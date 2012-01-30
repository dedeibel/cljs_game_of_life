(ns game_of_life.world_printer
  (:require [game_of_life.world :as world])
)

(defprotocol WorldPrinter
  "Prints out one cell at a time, top left to bottom right. Acts as 
 strategy pattern callback."
  (linebreak    [this])
  (dead_cell    [this])
  (living_cell  [this])
  (to_s         [this] "Returns the resulting ascii art")
)

(defrecord CliWorldPrinter [world_string]
  WorldPrinter
  (linebreak   [this] (CliWorldPrinter. (str world_string \newline)))
  (dead_cell   [this] (CliWorldPrinter. (str world_string \space)))
  (living_cell [this] (CliWorldPrinter. (str world_string \X)))
  (to_s        [this] world_string)
)

(defn- new_printer []
  (CliWorldPrinter. "")
)

(defn- step [p minx curx cury cells]
  ; X und Y werden zunehmend erhoeht
  ; Wenn Y nicht uebereinstimmt, wird linebreak ausgegeben
  ; Wenn X nicht uebereinstimmt, wird leerstelle ausgegeben
  ; Wenn X und Y mit first cells ubereinstimmt wird 'X' ausgegeben
  (if-let [current (first cells)]
    (if (not= cury (:y current))
      (recur (linebreak p) minx minx (inc cury) cells)
      (if (not= curx (:x current))
        (recur (dead_cell   p) minx (inc curx) cury cells)
        (recur (living_cell p) minx (inc curx) cury (rest cells))
      )
    )
    p
  )
)

(defn- minx [cells]
  "Find the minimum x coordinate"
  (:x (first (sort-by #(:x %) cells)))
)

(defn- out [printer cells]
  "Initializes the output recursion and creates an output printer"
  (let [
      miny (:y (first cells))
      cell_minx (minx cells)
      minx (if (< cell_minx 0)
             cell_minx
             0
           )]
    (step printer minx minx miny cells)
  )
)

(defn- sort-lo-ru [cells]
  (sort-by #(vector (:y %) (:x %)) cells)
)

(defn to_string [world]
  "Prints out the world as ascii art."
  (-> (new_printer) 
      (out
        (sort-lo-ru (world/living_cells world)))
      (to_s)
  )
)

