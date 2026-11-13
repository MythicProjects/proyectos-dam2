#!/usr/bin/env bash
# Compiles everything and runs a main class.
# Usage: ./run.sh                       -> runs main.Main
#        ./run.sh model.SomeCheck       -> runs that class
set -euo pipefail

cd "$(dirname "$0")"

MAIN_CLASS="${1:-main.Main}"
OUT_DIR="out"
LIBS="lib/*"

rm -rf "$OUT_DIR"
javac -cp "$LIBS" -d "$OUT_DIR" $(find src/main/java -name '*.java')
java -cp "$OUT_DIR:src/main/resources:$LIBS" "$MAIN_CLASS"