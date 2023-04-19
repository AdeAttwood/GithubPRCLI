SRC_FILES:=$(wildcard src/**/*)
TEST_FILES:=$(wildcard test/**/*)

default: target/ghpr-1.1.1-standalone.jar
test: target/test.done

target/ghpr-1.1.1-standalone.jar: $(shell TARGET=build scripts/build-cache ${SRC_FILES})
	clojure -T:build uber

target/test.done: $(shell TARGET=test scripts/build-cache ${TEST_FILES} ${SRC_FILES})
	clojure -Xtest
	mkdir -p target
	touch target/test.done

clean:
	rm -rf .state
	rm -rf target

