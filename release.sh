#!/bin/bash

if [ ! -f build_number.properties ]; then
    echo "0" > build_number.properties
else
    b=$(cat build_number.properties)
    echo "$b+1" | bc > build_number.properties
fi

# Java 1.8 does not seem to work well yet...
export JAVA_HOME=$(/usr/libexec/java_home -v 1.7)

#BUILD_NUMBER=$(cat build_number.properties)
#export BUILD_NUMBER
RELEASE=1
export RELEASE

gradle build
