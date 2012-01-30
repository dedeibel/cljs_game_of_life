
all: game_of_life.js

DEPS = $(shell find src -name "*.clj")

game_of_life.js: ${DEPS}
	cljsc src '{:optimizations :advanced :output-dir "cljs-out"}' > $@

