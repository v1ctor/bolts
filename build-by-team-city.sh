#!/bin/sh -e

echo "first line of code of build-by-team-city.sh"

#env > env.dump

HOME=/root

./depot-scripts/build-r.sh
./depot-scripts/upload-r.sh yandex-bolts

# vim: set ts=4 sw=4 et:
