package org.gendut.collection;

public class MonoidMaps {
    static MonoidMap<Object, Object> sameTree = new MonoidMap<Object, Object>() {
        public Object zero() {
            return CatenableArrayTree.emptyTree;
        }

        public Object map(Object a) {
            return a;
        }

        public Object add(Object x, Object y) {
            return CatenableArrayTree.combine(x, y);
        }
    };

    static MonoidMap<Integer, Integer> sumOfInteger = new MonoidMap<Integer, Integer>() {
        public Integer zero() {
            return 0;
        }

        public Integer map(Integer a) {
            return a;
        }

        public Integer add(Integer x, Integer y) {
            return x+y;
        }
    };
}
