#!/bin/sh -e

rsync --rsh=ssh -av target/site/ nga@people.yandex-team.ru:stepancheg/bolts

# vim: set ts=4 sw=4 et:
