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
### SetF
### MapF
### Cf
### CollectionF



