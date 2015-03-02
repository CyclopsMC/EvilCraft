#!/bin/bash

# Java 1.8 does not seem to work well yet...
export JAVA_HOME=$(/usr/libexec/java_home -v 1.7)

RELEASE=1
export RELEASE

./gradlew build
./gradlew uploadArchives
./gradlew curse

# Github release
minecraft_version=$(grep minecraft_version= build.properties | sed s/minecraft_version=//)
tag=$(grep mod_version= build.properties | sed s/mod_version=//)
name="EvilCraft-"$minecraft_version"-"$tag
changelog=$(cat changelog.txt)
API_JSON=$(printf '{"tag_name": "%s","target_commitish": "master","name": "%s","body": "%s","draft": false,"prerelease": false}' $tag $name "$changelog")
ACCESS_TOKEN=$(grep github_token= gradle.properties | sed s/github_token=//)
curl --data "$API_JSON" https://api.github.com/repos/rubensworks/EvilCraft/releases?access_token=$ACCESS_TOKEN
