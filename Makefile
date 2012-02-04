
all: game_of_life.js

DEPS = $(shell find src -name "*.cljs")

OUTPUT_DIR = cljs-out

CLJS_ARGS = :optimizations :simple :pretty-print true 
ifdef PROD 
  CLJS_ARGS = :optimizations :advanced 
endif

game_of_life.js: ${DEPS}
	cljsc src '{${CLJS_ARGS}:output-dir "${OUTPUT_DIR}"}' > $@

.PHONY clean:
	rm -f game_of_life.js
	rm -fr "${OUTPUT_DIR}"
