#!/bin/bash

# Java 1.8 does not seem to work well yet...
export JAVA_HOME=$(/usr/libexec/java_home -v 1.7)

RELEASE=1
export RELEASE

./gradlew build
./gradlew uploadArchives
./gradlew curse
