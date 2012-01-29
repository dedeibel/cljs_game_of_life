
all: hello.js

hello.js: hello.cljs
	cljsc hello.cljs '{:optimizations :advanced}' > hello.js
