#!/bin/zsh -e

find src/main/java -name '*.java' -not -name '*Test.java' | xargs javadoc -link http://java.sun.com/j2se/1.5.0/docs/api/ -d target/javadoc

# vim: set ts=4 sw=4 et:
