package org.gendut.collection;

import java.util.Comparator;
import java.util.Random;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.gendut.arithmetic.Int;
import org.gendut.arithmetic.Rational;
import org.gendut.iterator.ForwardIterator;
import org.gendut.seq.Seq;

public class SortedSetTest extends TestCase {

    public void testConstruction() {
        SortedSet<Int> tree = SortedSet.create(Rational.naturallOrder);
        assertEquals(0, tree.size());
        tree = tree.add(Int.create(100));
        assertTrue(tree.size() == 1);
    }

    public void testInsertion() {

        /*
         * Attention: Raising N leads to much higher number of actual iterations
         * in the loop!
         */
        int N = 1700;

        Random rnd = new Random();

        SortedSet<Int> set = SortedSet.create(Rational.naturallOrder);

        set = set.add(Int.create(3));
        set = set.add(Int.create(2));
        set = set.add(Int.create(4));
        assertEquals("[2, 3, 4]", set.toString());
        assertTrue(set.contains(Int.create(4)));
        set = set.add(Int.create(1));
        assertEquals("[1, 2, 3, 4]", set.toString());
        assertTrue(set.contains(Int.create(2)));

        int iterCnt = 0;

        while (set.size() < N) {
            Int x = Int.create(rnd.nextInt(N));
            if (!set.contains(x)) {
                set = set.add(x);
                assertTrue(set.contains(x));
            }
            iterCnt = iterCnt + 1;
        }

        Seq<Int> seq = set.seq();

        for (int i = 0; i < N; i++) {
            assertEquals(Int.create(i), seq.first());
            seq = seq.rest();
        }
        assertTrue(seq.isEmpty());
        
        for (int i = 0; i < 15; i++) {
            int start = rnd.nextInt(N);
            ForwardIterator<Int> iter = set.iterator(start);
            for(int k = start; k < N; k++) {
                assertTrue(iter.hasNext());
                assertEquals(k, iter.next().intValue());
            }
            assertFalse(iter.hasNext());
        }
    }
    
    public void testSortedInsertion() {

        int N = 100;

        SortedSet<Int> set = SortedSet.create(Rational.naturallOrder);

        for(int i = 0; i < N; i++) {
            set = set.add(Int.create(i));
        }
         
        for(int i = 0; i < N; i++) {
            assertTrue(set.contains(Int.create(i)));
        }
    }
    
    public void testCreatePemutation() {
      int N = 7000;

      int[] perm = new int[N]; 
      SortedSet<Int> set = SortedSet.create(Rational.naturallOrder);

      for (int i = 0; i < N; i++) {
        set = set.add(Int.create(i));
        perm[i] = -1;
      }
              
      Random rnd = new Random();
      for (int i = N-1; i >= 0; i--) {
        assertEquals(i+1, set.size());
        Int next = set.get(rnd.nextInt((int)set.size()));
        assertTrue(set.contains(next));
        set = set.remove(next);
        assertEquals(i, set.size());
        assertFalse(set.contains(next));
        assertEquals(perm[next.intValue()], -1);
        perm[next.intValue()] = i;
      }
      
      for (int i = 0; i < N; i++) {
        assertTrue(perm[i] >= 0);
      }
    }
    

    Comparator<Integer> cmp = new Comparator<Integer>() {
      public int compare(Integer a, Integer b) {
        return a.compareTo(b);
      }
    };

   
    public void testTimingInsertJavaTreeSet() {
      int N = 400;

      TreeSet<Integer> set = new TreeSet<Integer>();
      
      for (int i = 0; i < N; i++) {
        set.add(i);
      }
      
      for (int i = 0; i < N; i++) {
        set.remove(i);
      }
    }
    
    public void testTimingInsert() {
      int N = 400;

      SortedSet<Integer> set = SortedSet.create(cmp);
      
      for (int i = 0; i < N; i++) {
        set = set.add(i);
      }
      
      for (int i = 0; i < N; i++) {
        set = set.remove(i);
      }
    }
    
    public void testReplace() {
        int N = 400;

        SortedSet<Integer> set = SortedSet.create(cmp);
        
        for (int i = 0; i < N; i++) {
          set = set.add(i*2);
        }
        assertEquals(N, set.size());
        
        int pos = N/3;
        assertEquals((Integer) (2*pos), set.get(pos));
        set = set.replaceAt(pos, 2*pos+1);
        assertEquals(N, set.size());
        assertEquals((Integer) (2*pos+1), set.get(pos));
        for (int i = 0; i < N; i++) {
            Integer expected = i == pos ? 2*i+1 : 2*i;
            assertEquals(expected, set.get(i));
          }
    }
    
    
}
