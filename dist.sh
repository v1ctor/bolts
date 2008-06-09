#!/bin/sh -e

mkdir -p target/bolts
mkdir -p target/bolts/target/

cp -r src target/bolts/
find target/bolts -name '.svn' | xargs rm -rf

# javadoc -classpath lib/commons-lang-2.3.jar -d target/javadoc src/main/java/**/*.java

cp build.xml LICENSE target/bolts/
(
    cd target
    cp *.jar bolts/target/
    cp -r javadoc bolts/target/
)

(
    cd target
    jar cMf bolts.zip bolts
)

r=$(svn info | sed -n -e 's/Last Changed Rev: //p')

mkdir -p target/site
cat doc/index.html | sed "s,@rev@,$r," > target/site/index.html
cp target/bolts.zip target/site/
cp target/yandex-bolts.jar target/site/bolts.jar
cp target/yandex-bolts-sources.jar target/site/bolts-sources.jar
cp -r target/javadoc target/site/

# vim: set ts=4 sw=4 et:
