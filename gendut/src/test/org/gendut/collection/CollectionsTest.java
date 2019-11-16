package org.gendut.collection;


import java.util.Arrays;
import java.util.Random;

import junit.framework.TestCase;

import org.gendut.algorithm.Comparator;
import org.gendut.arithmetic.Int;
import org.gendut.collection.mutable.ExtendibleArray;
import org.gendut.iterator.ForwardIterator;
import org.gendut.seq.Seq;

public class CollectionsTest extends TestCase {
  
  static final int N = 100000;
  
 
  
  static public void testCreateSet() {
    ExtendibleArray<Integer> numbers = new ExtendibleArray<Integer>();
    numbers.add(1);
    numbers.add(2);
    numbers.add(3);
    numbers.add(1);
    numbers.add(2);
    assertEquals(5, numbers.size());
    ImmutableSet<Integer> set = Collections.createSet(numbers.asConstant());
    assertEquals(3, set.size());
    assertTrue(set.contains(1));
    assertTrue(set.contains(2));
    assertTrue(set.contains(3));
  }
  
  static public void testSort() {
    Stack<Int> S = Stack.create();
    S = S.push(Int.create(2));
    S = S.push(Int.create(1));
    S = S.push(Int.create(4));
    S = S.push(Int.create(5));
    S = S.push(Int.create(2));
    Array<Int> L = Collections.sort(S, Comparator.RationalNatural, 7);
    assertNotNull(L);
    assertEquals("[1, 2, 2, 4, 5]", L.toString());
    
    Random rnd = new Random();
    
    for(int k = 2; k < 130000; k = 2 * k) {
      S = Stack.create();
      for (int i = 0; i < k; i++)
        S = S.push(Int.create(rnd.nextInt(60000)));
      
      L = Collections.sort(S, Comparator.RationalNatural, 34000);
      /*
       * turn L into a tuple for faster access:
       */
      assertEquals(S.size(), L.size());
      L = ImmutableArray.fromCollection(L);
      assertEquals(S.size(), L.size());
      
      /*
       * compare with result from java util's array sort:
       */
      
      ConstantArray<Int> A = ConstantArray.fromCollection(S);
      Object[] arr = A.asMutableArray();
      Arrays.sort(arr, new java.util.Comparator<Object>(){
        public int compare(Object o1, Object o2) {
          return ((Int)o1).compareTo((Int) o2);
        }});
      assertEquals(arr.length, L.size());
      for (int i = 0; i < k; i++)
        assertTrue(arr[i] == L.get(i));
    }
  }
  
  static public void testMerge() {
    Stack<Int> S1 = Stack.create();
    S1 = S1.push(Int.create(8));
    S1 = S1.push(Int.create(4));
    S1 = S1.push(Int.create(3));
    S1 = S1.push(Int.create(3));
    S1 = S1.push(Int.create(1));
    
    Stack<Int> S2 = Stack.create();
    S2 = S2.push(Int.create(7));
    S2 = S2.push(Int.create(5));
    S2 = S2.push(Int.create(4));
    S2 = S2.push(Int.create(3));
    S2 = S2.push(Int.create(2));
    
    
    Array<Int> L = Collections.merge(S1, S2, Comparator.RationalNatural);
    assertNotNull(L);
    assertEquals(10, L.size());
    assertEquals("[1, 2, 3, 3, 3, 4, 4, 5, 7, 8]", L.toString());

    L = Collections.merge(S2, S1, Comparator.RationalNatural);
    assertNotNull(L);
    assertEquals(10, L.size());
    assertEquals("[1, 2, 3, 3, 3, 4, 4, 5, 7, 8]", L.toString());
  }
  
  static public void testDisjoint() {
      ImmutableHashSet<Integer> set1 = ImmutableHashSet.create();
      ImmutableHashSet<Integer> set2 = ImmutableHashSet.create();
      assertTrue(Collections.isDisjoint(set1, set2));
      set1 = set1.add(1);
      assertTrue(Collections.isDisjoint(set1, set2));
      assertFalse(Collections.isDisjoint(set1, set1));
      set2 = set2.add(2);
      assertTrue(Collections.isDisjoint(set1, set2));
      set2 = set2.add(1);
      assertFalse(Collections.isDisjoint(set1, set2));
      set1 = set1.add(3);
      assertFalse(Collections.isDisjoint(set1, set2));
      set1 = set1.remove(1);
      assertTrue(Collections.isDisjoint(set1, set2));
  }
  
  static public void testSingleton() {
      ImmutableSet<Integer> s = Collections.singleton(2);
      assertFalse(s.contains(1));
      assertTrue(s.contains(2));
      assertEquals(Integer.valueOf(2), s.find(2));
      assertNull(s.find(1));
      assertEquals("[2]",s.toString());
      
      ForwardIterator<Integer> it = s.iterator();
      assertTrue(it.hasNext());
      assertEquals(Integer.valueOf(2), it.next());
      assertFalse(it.hasNext());
      
      Seq<Integer> seq = s;
      assertFalse(seq.isEmpty());
      assertEquals(Integer.valueOf(2), seq.first());
      assertTrue(seq.rest().isEmpty());
  }
}
