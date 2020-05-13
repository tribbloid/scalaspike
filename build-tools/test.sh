#!/usr/bin/env bash

FWDIR="$(cd "`dirname "$0"`"/..; pwd)"

export DATE=$(date --iso-8601=second)

mkdir -p logs
mvn dependency:tree --batch-mode -f "$FWDIR"/pom.xml "$@" > "$FWDIR"/logs/mvnTree_"$DATE".log
mvn clean install -f "$FWDIR"/pom.xml "$@"
