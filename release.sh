#!/bin/bash

# @param property The property to find the value for.
function jsonval {
  KEY=$1
  awk -F"[,:}]" '{for(i=1;i<=NF;i++){if($i~/'$KEY'\042/){print $(i+1)}}}' | tr -d '"' | sed -n 1p
}

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
printf '{"tag_name": "%s","target_commitish": "master","name": "%s","body": "%s","draft": false,"prerelease": false}' $tag $name "$changelog" > .tmp.json
ACCESS_TOKEN=$(grep github_token= gradle.properties | sed s/github_token=//)

creationresp=$(curl -d@.tmp.json https://api.github.com/repos/rubensworks/EvilCraft/releases?access_token=$ACCESS_TOKEN)
RELEASEID=$(echo $creationresp | jsonval "id")
curl -i -F filedata=@build/libs/$name.jar https://api.github.com/repos/rubensworks/EvilCraft/releases/$RELEASEID/?access_token=$ACCESS_TOKEN

rm .tmp.json