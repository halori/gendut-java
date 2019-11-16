package org.gendut.collection;

import org.gendut.collection.mutable.MutableHashMap;
import org.gendut.collection.mutable.MutableHashSet;

import junit.framework.TestCase;


public class MutableHashTest extends TestCase
{
    static final int N = 100000;
    static final int K = 1;

    static public void testMutapleHashSet()
    {
        MutableHashSet<Integer> set = new MutableHashSet<Integer>();
        MutableHashSet<Integer> set2 = new MutableHashSet<Integer>();
        for (int i = 0; i < N; i++)
        {
            set.add(i);
            set2.add(i);
        }
        for (int i = 0; i < N; i++)
        {
            assertTrue(set.contains(i));
        }
        assertFalse(set.contains(N));
        assertTrue(set.asConstant().equals(set2.asConstant()));
        assertTrue(Collections.equalsForSets(set.asConstant(), set2.asConstant()));
        assertTrue(set.contains(4));
        set.remove(4);
        assertFalse(set.contains(4));
        assertFalse(set.asConstant().equals(set2.asConstant()));
        assertFalse(Collections.equalsForSets(set.asConstant(), set2.asConstant()));
        set.add(N);
        assertFalse(set.equals(set2));
        assertFalse(Collections.equalsForSets(set.asConstant(), set2.asConstant()));
        set2.remove(4);
        assertFalse(set2.contains(4));
        assertFalse(set.equals(set2));
        assertFalse(Collections.equalsForSets(set.asConstant(), set2.asConstant()));
        set2.add(N);
        assertTrue(set.asConstant().equals(set2.asConstant()));
        assertTrue(Collections.equalsForSets(set.asConstant(), set2.asConstant()));
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
}
