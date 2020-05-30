package org.gendut.collection;

import java.util.TreeMap;

import junit.framework.TestCase;

import org.gendut.collection.mutable.MutableHashMap;
import org.gendut.iterator.ForwardIterator;

public class HashTest extends TestCase {

    public void testDefinition() {
        ImmutableHashMap<String, String> ce = ImmutableHashMap.create();
        ce = ce.put("color", "blue");
        ce = ce.put("weight", "0.7kg");
        ce = ce.put("pages", "967");
        assertEquals("[(color . blue), (pages . 967), (weight . 0.7kg)]", ce.toString());
    }

    /*
     * N is number of created entries, K is number of read cycles
     */
    static final int N = 12000;

    static final int K = 1;

    static public void testFixedSizeHashSet() {
        FixedSizeHashSet<Integer> set = new FixedSizeHashSet<Integer>(N * 2);
        set = set.add(1);
        set = set.add(2);
        set = set.add(3);
        set = set.add(4);
        assertEquals(4, set.size());
        assertTrue(set.contains(1));
        assertTrue(set.contains(2));
        assertTrue(set.contains(3));
        assertTrue(set.contains(4));
        assertFalse(set.contains(0));

        for (int i = 0; i < N; i++) {
            Integer item = i;
            if (i > 4)
                assertFalse(set.contains(item));
            set = set.add(i);
            assertTrue(set.contains(item));
        }
        assertEquals(N, set.size());

        for (int i = 9 * N / 10; i < N; i++) {
            Integer item = i;
            assertTrue(set.contains(item));
            set = set.add(item);
            assertTrue(set.contains(item));
        }
        assertEquals(N, set.size());

        for (int i = N - 1; i >= 0; i--) {
            Integer item = i;
            assertTrue(set.contains(item));
            assertEquals(i + 1, set.size());
            set = set.remove(item);
            assertFalse(set.contains(item));
        }
        assertEquals(0, set.size());
        set = set.add(1);
        set = set.add(2);
        set = set.add(3);
        set = set.add(4);
        assertEquals(4, set.size());
        assertTrue(set.contains(1));
        assertTrue(set.contains(2));
        assertTrue(set.contains(3));
        assertTrue(set.contains(4));
        assertFalse(set.contains(8));
    }
    
    static class NullHash<T> {
        final T v;

        public NullHash(T v) {
            super();
            this.v = v;
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(Object other) {
            return v.equals(((NullHash<T>) other).v);
        }

        @Override
        public int hashCode() {
            return 0;
        }

    }

    public void testWithSameHash() {
        NullHash<Integer> one = new NullHash<Integer>(1);
        NullHash<Integer> two = new NullHash<Integer>(2);
        FixedSizeHashSet<NullHash<Integer>> set = FixedSizeHashSet.create(1000);
        set = set.add(one);
        set = set.add(two);
        assertEquals(2, set.size());
        ForwardIterator<NullHash<Integer>> iter = set.iterator();
        int cnt = 0;
        while(iter.hasNext()) {
            iter.next();
            cnt++;
        }
        assertEquals(2, cnt);
    }

    static public void testImmutableHashSet() {
        ImmutableHashSet<Integer> set = ImmutableHashSet.create();
        set = set.add(1);
        set = set.add(2);
        set = set.add(3);
        set = set.add(4);
        assertEquals(4, set.size());
        assertTrue(set.contains(1));
        assertTrue(set.contains(2));
        assertTrue(set.contains(3));
        assertTrue(set.contains(4));
        assertFalse(set.contains(0));

        for (int i = 0; i < N; i++) {
            Integer item = i;
            if (i > 4)
                assertFalse(set.contains(item));
            set = set.add(i);
            assertTrue(set.contains(item));
        }
        assertEquals(N, set.size());

        for (int i = 9 * N / 10; i < N; i++) {
            Integer item = i;
            assertTrue(set.contains(item));
            set = set.add(item);
            assertTrue(set.contains(item));
        }
        assertEquals(N, set.size());

        for (int i = N - 1; i >= 0; i--) {
            Integer item = i;
            assertTrue(set.contains(item));
            assertEquals(i + 1, set.size());
            set = set.remove(item);
            assertFalse(set.contains(item));
        }
        assertEquals(0, set.size());
        set = set.add(1);
        set = set.add(2);
        set = set.add(3);
        set = set.add(4);
        assertEquals(4, set.size());
        assertTrue(set.contains(1));
        assertTrue(set.contains(2));
        assertTrue(set.contains(3));
        assertTrue(set.contains(4));
        assertFalse(set.contains(8));
    }

    static public void testSmallImmutableHashMap() {
        ImmutableHashMap<Integer, String> map = ImmutableHashMap.create();
        map = map.put(1, "A");
        assertEquals(map.get(1), "A");
        map = map.put(2, "B");
        map = map.put(3, "C");
        assertEquals(3, map.size());
        assertEquals(map.get(1), "A");
        assertEquals(map.get(2), "B");
        assertEquals(map.get(3), "C");
        map = map.put(2, "A");
        assertEquals(map.get(2), "A");
        assertEquals(3, map.size());
        map = map.remove(2);
        assertEquals(2, map.size());
    }

    static public void testImmutableHashMapIterator() {
        ImmutableHashMap<String, Integer> map = ImmutableHashMap.create();
        for (int i = 0; i < N; i++) {
            String key = Integer.toString(i);
            assertFalse(map.containsKey(key));
            Integer val = i;
            map = map.put(key, val);
            assertTrue(map.containsKey(key));
            assertEquals(val, map.get(key));
        }

        assertEquals(N, map.size());
        for (int k = 0; k < K; k++) {
            for (int i = 0; i < N; i++) {
                String item = Integer.toString(i);
                assertTrue(map.containsKey(item));
                assertEquals((Integer) i, map.get(item));
            }
        }
        ForwardIterator<String> keys = map.keyIterator();

        while (keys.hasNext()) {
            String key = keys.next();
            Integer val = Integer.valueOf(key);
            assertTrue(map.containsKey(key));
            assertEquals(val, map.get(key));
            map = map.remove(key);
            assertFalse(map.containsKey(key));
        }
        assertEquals(0, map.size());
    }

    static public void testImmutableHashMap() {
        ImmutableHashMap<Integer, Integer> map = ImmutableHashMap.create();
        for (int i = 0; i < N; i++) {
            Integer key = i;
            assertFalse(map.containsKey(key));
            Integer val = key;
            map = map.put(key, val);
            assertTrue(map.containsKey(key));
            assertEquals(val, map.get(key));
        }
        assertEquals(N, map.size());
        for (int k = 0; k < K; k++) {
            for (int i = 0; i < N; i++) {
                Integer item = i;
                assertTrue(map.containsKey(item));
                assertEquals(item, map.get(item));
            }
        }
        for (int i = 0; i < N; i++) {
            Integer key = i;
            Integer val = key;
            assertTrue(map.containsKey(key));
            assertEquals(val, map.get(key));
            map = map.remove(key);
            assertFalse(map.containsKey(key));
        }
        assertEquals(0, map.size());
    }

    static public void testFixedSizeHashMap() {
        FixedSizeHashMap<Integer, Integer> map = FixedSizeHashMap.create(N * 2);
        for (int i = 0; i < N; i++) {
            Integer key = i;
            assertFalse(map.containsKey(key));
            Integer val = key;
            map = map.put(key, val);
            assertTrue(map.containsKey(key));
            assertEquals(val, map.get(key));
        }
        assertEquals(N, map.size());
        for (int k = 0; k < K; k++) {
            for (int i = 0; i < N; i++) {
                Integer item = i;
                assertTrue(map.containsKey(item));
                assertEquals(item, map.get(item));
            }
        }
        for (int i = 0; i < N; i++) {
            Integer key = i;
            Integer val = key;
            assertTrue(map.containsKey(key));
            assertEquals(val, map.get(key));
            map = map.remove(key);
            assertFalse(map.containsKey(key));
        }
        assertEquals(0, map.size());
    }

    static public void testImmutableHashMapWithString() {
        ImmutableHashMap<String, Integer> map = ImmutableHashMap.create();
        for (int i = 0; i < N; i++) {
            String key = Integer.toString(i);
            assertFalse(map.containsKey(key));
            Integer val = i;
            map = map.put(key, val);
            assertTrue(map.containsKey(key));
            assertEquals(val, map.get(key));
        }
        assertEquals(N, map.size());
        for (int k = 0; k < K; k++) {
            for (int i = 0; i < N; i++) {
                String item = Integer.toString(i);
                assertTrue(map.containsKey(item));
                assertEquals((Integer) i, map.get(item));
            }
        }
        for (int i = 0; i < N; i++) {
            String key = Integer.toString(i);
            Integer val = i;
            assertTrue(map.containsKey(key));
            assertEquals(val, map.get(key));
            map = map.remove(key);
            assertFalse(map.containsKey(key));
        }
        assertEquals(0, map.size());
    }

    static public void testMutableHashMap() {
        MutableHashMap<Integer, Integer> map = new MutableHashMap<Integer, Integer>();
        for (int i = 0; i < N; i++) {
            Integer key = i;
            assertFalse(map.containsKey(key));
            Integer val = key;
            map.put(key, val);
            assertTrue(map.containsKey(key));
            assertEquals(val, map.get(key));
        }
        assertEquals(N, map.size());
        for (int k = 0; k < K; k++) {
            for (int i = 0; i < N; i++) {
                Integer item = i;
                assertTrue(map.containsKey(item));
                assertEquals(item, map.get(item));
            }
        }
        for (int i = 0; i < N; i++) {
            Integer key = i;
            Integer val = key;
            assertTrue(map.containsKey(key));
            assertEquals(val, map.get(key));
            map.remove(key);
            assertFalse(map.containsKey(key));
        }
        assertEquals(0, map.size());
    }

    static public void testMutableHashMapWithString() {
        MutableHashMap<String, Integer> map = new MutableHashMap<String, Integer>();
        for (int i = 0; i < N; i++) {
            String key = Integer.toString(i);
            assertFalse(map.containsKey(key));
            Integer val = i;
            map.put(key, val);
            assertTrue(map.containsKey(key));
            assertEquals(val, map.get(key));
        }
        assertEquals(N, map.size());
        for (int k = 0; k < K; k++) {
            for (int i = 0; i < N; i++) {
                String item = Integer.toString(i);
                assertTrue(map.containsKey(item));
                assertEquals((Integer) i, map.get(item));
            }
        }
        for (int i = 0; i < N; i++) {
            String key = Integer.toString(i);
            Integer val = i;
            assertTrue(map.containsKey(key));
            assertEquals(val, map.get(key));
            map.remove(key);
            assertFalse(map.containsKey(key));
        }
        assertEquals(0, map.size());
    }

    static public void testJavaUtilTreeMap() {
        TreeMap<Integer, Integer> map = new TreeMap<Integer, Integer>();
        for (int i = 0; i < N; i++) {
            Integer key = i;
            assertFalse(map.containsKey(key));
            Integer val = key;
            map.put(key, val);
            assertTrue(map.containsKey(key));
            assertEquals(val, map.get(key));
        }
        assertEquals(N, map.size());
        for (int k = 0; k < K; k++) {
            for (int i = 0; i < N; i++) {
                Integer item = i;
                assertTrue(map.containsKey(item));
                assertEquals(item, map.get(item));
            }
        }
        for (int i = 0; i < N; i++) {
            Integer key = i;
            Integer val = key;
            assertTrue(map.containsKey(key));
            assertEquals(val, map.get(key));
            map.remove(key);
            assertFalse(map.containsKey(key));
        }
        assertEquals(0, map.size());
    }

    static public void testJavaUtilTreeWithString() {
        TreeMap<String, Integer> map = new TreeMap<String, Integer>();
        for (int i = 0; i < N; i++) {
            String key = Integer.toString(i);
            assertFalse(map.containsKey(key));
            Integer val = i;
            map.put(key, val);
            assertTrue(map.containsKey(key));
            assertEquals(val, map.get(key));
        }
        assertEquals(N, map.size());
        for (int k = 0; k < K; k++) {
            for (int i = 0; i < N; i++) {
                String item = Integer.toString(i);
                assertTrue(map.containsKey(item));
                assertEquals((Integer) i, map.get(item));
            }
        }
        for (int i = 0; i < N; i++) {
            String key = Integer.toString(i);
            Integer val = i;
            assertTrue(map.containsKey(key));
            assertEquals(val, map.get(key));
            map.remove(key);
            assertFalse(map.containsKey(key));
        }
        assertEquals(0, map.size());
    }

    static public void testJavaUtilHashMap() {
        java.util.HashMap<Integer, Integer> map = new java.util.HashMap<Integer, Integer>();
        for (int i = 0; i < N; i++) {
            Integer key = i;
            assertFalse(map.containsKey(key));
            Integer val = key;
            map.put(key, val);
            assertTrue(map.containsKey(key));
            assertEquals(val, map.get(key));
        }
        assertEquals(N, map.size());
        for (int k = 0; k < K; k++) {
            for (int i = 0; i < N; i++) {
                Integer item = i;
                assertTrue(map.containsKey(item));
                assertEquals(item, map.get(item));
            }
        }
        for (int i = 0; i < N; i++) {
            Integer key = i;
            Integer val = key;
            assertTrue(map.containsKey(key));
            assertEquals(val, map.get(key));
            map.remove(key);
            assertFalse(map.containsKey(key));
        }
        assertEquals(0, map.size());
    }

    static public void testJavaUtilHashMapWithString() {
        java.util.HashMap<String, Integer> map = new java.util.HashMap<String, Integer>();
        for (int i = 0; i < N; i++) {
            String key = Integer.toString(i);
            assertFalse(map.containsKey(key));
            Integer val = i;
            map.put(key, val);
            assertTrue(map.containsKey(key));
            assertEquals(val, map.get(key));
        }
        assertEquals(N, map.size());
        for (int k = 0; k < K; k++) {
            for (int i = 0; i < N; i++) {
                String item = Integer.toString(i);
                assertTrue(map.containsKey(item));
                assertEquals((Integer) i, map.get(item));
            }
        }
        for (int i = 0; i < N; i++) {
            String key = Integer.toString(i);
            Integer val = i;
            assertTrue(map.containsKey(key));
            assertEquals(val, map.get(key));
            map.remove(key);
            assertFalse(map.containsKey(key));
        }
        assertEquals(0, map.size());
    }
}
