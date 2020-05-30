#!/usr/bin/env bash

FWDIR="$(
  cd "$(dirname "$0")"/..
  pwd
)"
DATE=$(date --iso-8601=second)

mkdir -p ${FWDIR}/logs

${FWDIR}/gradlew -q dependencies "${@}" >${FWDIR}/logs/dependencyTree_"$DATE".txt

${FWDIR}/gradlew clean build -x test "${@}"
