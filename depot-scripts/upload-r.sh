#!/bin/sh -e

errExit() {
    echo "$@" >&2
    exit 1
}

lib=$1

test -n "$lib" || errExit "library name not specified"

echo "Uploading $lib to svn"

dir="tmp/upload-repo/$lib"

mkdir -p $dir

r=$(svn info | sed -n -e 's/Last Changed Rev: //p')
version="r$r"

test -n "$r" || errExit "cannot get last changed rev"

if svn info https://svn.yandex.ru/stuff/java/depot/repo/$lib/$version 2>/dev/null | grep -q "Last Changed Rev:"; then
    echo "repository already contains $lib/$version"
    exit 0
fi

svn co -N https://svn.yandex.ru/stuff/java/depot/repo/$lib $dir

mkdir -p $dir/$version
svn add $dir/$version

if [ -d target/jars ]; then
    cp target/jars/$lib-$version*.jar $dir/$version/
else
    cp target/$lib-$version*.jar $dir/$version/
fi

svn add $dir/$version/$lib-$version*.jar

echo "$version" > $dir/latest-version.txt
svn add $dir/latest-version.txt

svn commit -m "uploading $lib-$version" $dir/$version $dir/latest-version.txt
# vim: set ts=4 sw=4 et:
