#!/bin/sh -e

err_exit() {
    echo "$@" >&2
    exit 1
}

cd $(dirname $0)

ver="$1"

test -n "$ver" || err_exit "usage: $0 <ver>"

for j in bolts.jar bolts-sources.jar; do
    s3cmd --acl-public put target/$j s3://bolts/dist/$ver/$j
done

echo "done"

# vim: set ts=4 sw=4 et:
