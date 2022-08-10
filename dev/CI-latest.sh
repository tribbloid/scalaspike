#!/usr/bin/env bash

CRDIR="$(
  cd "$(dirname "$0")" || exit
  pwd
)"

exec "${CRDIR}"/.CI.sh "${ARGS[@]}"