#!/bin/sh -e

cd $(dirname $0)

s3cmd "$@" --acl-public sync target/javadoc/ s3://bolts/javadoc/

# vim: set ts=4 sw=4 et:
