package org.gendut.collection;

import java.util.Comparator;
import java.util.Random;


import junit.framework.TestCase;

public class SortedMapTest extends TestCase {

  static Comparator<Integer> cmp = new Comparator<Integer>() {
    public int compare(Integer a, Integer b) {
      return a.compareTo(b);
    }
  };

  public void testConstruction() {
    SortedMap<Integer, String> map = SortedMap.create(cmp);
    map = map.put(1, "A");
    map = map.put(2, "B");
    assertEquals("[(1 . A), (2 . B)]", map.toString());
    assertEquals("A", map.get(1));
    assertEquals("B", map.get(2));
  }

  public void testOverwrite() {
    UpdatableMap<Integer, Integer, ?> map = SortedMap.create(cmp);
    map = map.put(1, 1);
    map = map.put(2, 2);
    assertEquals(Integer.valueOf(1), map.get(1));
    assertEquals(Integer.valueOf(2), map.get(2));
    map = map.put(2, 3);
    assertEquals(2, map.size());
    assertEquals(Integer.valueOf(1), map.get(1));
    assertEquals(Integer.valueOf(3), map.get(2));

    int N = 1000;

    for (int i = 0; i < N; i++)
      map = map.put(i, i);

    assertEquals(N, map.size());
    for (int i = 0; i < N; i++)
      assertEquals(Integer.valueOf(i), map.get(i));

    for (int i = 0; i < 2 * N; i++)
      map = map.put(i, i + 777);

    assertEquals(2 * N, map.size());
    for (int i = 0; i < 2 * N; i++)
      assertEquals(Integer.valueOf(i + 777), map.get(i));
  }

  public void testRemove() {
    UpdatableMap<Integer, Integer, ?> map = SortedMap.create(cmp);
    map = map.put(1, 1);
    map = map.put(2, 2);
    assertEquals(Integer.valueOf(1), map.get(1));
    assertEquals(Integer.valueOf(2), map.get(2));
    map = map.remove(2);
    map.toString();
    assertEquals(1, map.size());
    assertEquals(Integer.valueOf(1), map.get(1));
  }

  public void testHashVersusSorted() {
    int N = 10000;
    int K = 90;
    UpdatableMap<Integer, Integer, ?> mapSorted = SortedMap.create(cmp);
    UpdatableMap<Integer, Integer, ?> mapHash = ImmutableHashMap
        .create();
    Random rnd = new Random();
    for (int i = 0; i <= N; i++) {
      int key = rnd.nextInt(K);
      assertEquals(mapHash.get(key), mapSorted.get(key));
      boolean insert = rnd.nextBoolean();
      if (insert) {
        int val = rnd.nextInt(K);
        mapSorted = mapSorted.put(key, val);
        mapHash = mapHash.put(key, val);
      } else {
        mapSorted = mapSorted.remove(key);
        mapHash = mapHash.remove(key);
      }
      assertEquals(mapHash, mapSorted);
    }
  }
}