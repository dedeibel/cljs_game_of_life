
CLJ_SRC = src/clj
CLJS_SRC = src
CLJ_DEPS = $(shell find "${CLJ_SRC}" -name "*.clj")
CLJS_DEPS = $(shell find "${CLJS_SRC}" -name "*.cljs")
DEPS = ${CLJS_DEPS} ${CLJ_DEPS}

MAIN_JS = game_of_life.js
OUTPUT_DIR = cljs-out
NOTIFY_COMMAND = growlnotify -m compile-done cljs-watch
ADDITIONAL_CLASSPATH = ${PWD}/${CLJ_SRC}

# CLJS_ARGS = :optimizations :simple :pretty-print true 
CLJS_ARGS = 
ifdef PROD 
  CLJS_ARGS = :optimizations :advanced 
endif

all: ${MAIN_JS}

# cljsc -cp requires a modified version of cljsc, see dedeibel@github
${MAIN_JS}: ${DEPS}
	@export CLASSPATH=${CLASSPATH}:${ADDITIONAL_CLASSPATH}; cljsc "${CLJS_SRC}" '{${CLJS_ARGS}:output-dir "${OUTPUT_DIR}" :output-to "$@"}'

.PHONY: watch
watch: ${DEPS}
	@export CLASSPATH=${CLASSPATH}:${ADDITIONAL_CLASSPATH}; cljs-watch -s "${CLJS_SRC}" -c '${NOTIFY_COMMAND}' '{${CLJS_ARGS}:output-dir "${OUTPUT_DIR}" :output-to "${MAIN_JS}"}'

.PHONY: clean
clean:
	rm -f "${MAIN_JS}"
	rm -fr "${OUTPUT_DIR}"
