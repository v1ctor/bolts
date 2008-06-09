#!/bin/sh

if [ -n "`svn st -q --ignore-externals`" -a "$1" != "ignore-svn" ]; then
    echo "must be committed before build" >&2
    exit 1
fi

svn --non-interactive up

r=$(svn info | sed -n -e 's/Last Changed Rev: //p')

ant -Djar.suffix=-r$r build

# vim: set ts=4 sw=4 et:
