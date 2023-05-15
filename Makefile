SRC_FILES:=$(wildcard src/**/*)
TEST_FILES:=$(wildcard test/**/*)

build: target/ghpr-standalone.jar
static: target/ghpr-standalone
test: target/test.done

target/ghpr-standalone.jar: $(shell TARGET=build scripts/build-cache ${SRC_FILES})
	clojure -T:build uber

target/test.done: $(shell TARGET=test scripts/build-cache ${TEST_FILES} ${SRC_FILES})
	clojure -Xtest
	mkdir -p target
	touch target/test.done

target/ghpr-standalone: target/ghpr-standalone.jar
	docker run \
		-v "$(shell pwd)/target:/build" -w /build --rm \
		ghcr.io/graalvm/native-image:22.3.1 \
		-jar ghpr-standalone.jar --no-fallback --static --initialize-at-build-time --diagnostics-mode

build-in-docker:
	docker run -v "$(shell pwd):/src" -w /src --rm clojure -T:build uber
	$(MAKE) target/ghpr-standalone

gh-extention: target/ghpr-standalone
	mkdir -p target/gh-mr
	mv target/ghpr-standalone target/gh-mr/gh-mr

clean:
	rm -rf .state
	rm -rf target

