#!/bin/bash

if [ ! -f build_number.properties ]; then
    echo "0" > build_number.properties
else
    b=$(cat build_number.properties)
    echo "$b+1" | bc > build_number.properties
fi

BUILD_NUMBER=$(cat build_number.properties)
export BUILD_NUMBER

gradle build
