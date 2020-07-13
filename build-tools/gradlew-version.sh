#!/usr/bin/env bash

FWDIR="$(
  cd "$(dirname "$0")"/.. || exit
  pwd
)"

cd ${FWDIR} || exit

./gradlew wrapper --gradle-version=6.5.1
