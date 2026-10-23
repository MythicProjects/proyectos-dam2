#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"

javac -encoding UTF-8 -Xlint:all -cp lib/jdom2-2.0.6.1.jar -d out src/*.java
java -cp out:lib/jdom2-2.0.6.1.jar Main
