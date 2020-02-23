#!/usr/bin/env bash

CRDIR="$(cd "`dirname "$0"`"; pwd)"

"$CRDIR"/test-install.sh -DskipTests "$@"