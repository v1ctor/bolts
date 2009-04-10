#!/bin/sh -e

ec() {
    echo "$@" >&2
    "$@"
}

cd $(dirname $0)/..
ver=$(hg log --limit 1 --template '{date|rfc3339date}' | sed -e 's,+.*,,; s,[T:-],,g')
ec ant -Djar.suffix=-$ver jar
ec ant -f yandex/build.xml -Divy.deliver.revision=$ver publish

# vim: set ts=4 sw=4 et:
