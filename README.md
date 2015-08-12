## Collections utilities used in various Yandex projects.

* Package contains utilities that are
  * unrelated to Yandex
  * generic enough so that they are useful in the most project
  * functional

## Install
```xml
<dependency>
  <groupId>ru.yandex</groupId>
  <artifactId>bolts</artifactId>
  <version>1.0.6</version>
</dependency>
```
## Usage
### Cf
```java
Cf.map();
Cf.hashMap();
Cf.list();
Cf.arrayList();
Cf.linkedList();
Cf.set();
Cf.hashSet();
```
### Option
```java
Option<Integer> some = Option.some(1);
assert some.isDefined() == true;
assert some.isEmpty() == false;
assert some.get() == 1;

Option<Integer> none = Option.none();
assert none.isDefined() == false;
assert some.isEmpty() == true;
```
### ListF
```java
Cf.list(1, 2, 3, 16).map(i -> i*i);
[1, 4, 9, 16]
Cf.list(1, 2, 3, 4).filter(i -> i % 2 == 0);
[2, 4]
```
### SetF
### MapF
### CollectionF
### Tuple2
### Either



