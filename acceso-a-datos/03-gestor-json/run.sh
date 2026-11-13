#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
CP="lib/jdom2-2.0.6.1.jar:lib/jackson-databind-2.18.2.jar:lib/jackson-core-2.18.2.jar:lib/jackson-annotations-2.18.2.jar"
javac -encoding UTF-8 -Xlint:all -cp "$CP" -d out src/*.java
java -cp "out:$CP" Main