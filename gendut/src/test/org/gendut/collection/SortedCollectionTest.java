package org.gendut.collection;

import org.gendut.algorithm.Comparator;

import org.gendut.arithmetic.Int;
import org.gendut.collection.SortedCollection;

import junit.framework.TestCase;

public class SortedCollectionTest extends TestCase {

    static final int N = 10;

    Comparator<Integer> cmp = new Comparator<Integer>() {
        public int compare(Integer a, Integer b) {
          return a.compareTo(b);
        }
      };

      
    public void testTime() {
        Object t0 = null;
        for (int i = 0; i < N; i++) {
            t0 = SortedCollection.combine(null, null);
            assertEquals(2, SortedCollection.height(t0));
        }
    }

    public void testCombine() {
        Object t1 = SortedCollection.combine(null, null);
        assertEquals(2, SortedCollection.height(t1));
        assertEquals(2, SortedCollection._count(t1));
        Object t2 = SortedCollection.combine(null, null);
        Object t3 = SortedCollection.combine(t1, t2);
        assertEquals(3, SortedCollection.height(t3));
        assertEquals(4, SortedCollection._count(t3));
        Object t4 = SortedCollection.combine(null, t2);
        assertEquals(3, SortedCollection.height(t4));
        assertEquals(3, SortedCollection._count(t4));
        try {
            SortedCollection.combine(t4, null);
            fail("Exception expected");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testConstruction() {
        SortedCollection<Int> tree = SortedCollection.create(Comparator.RationalNatural);
        assertEquals(0, tree.size());
        tree = tree.add(Int.create(100));
        assertTrue(tree.size() == 1);
    }

    public void testToString() {
        Object t1 = SortedCollection.combine(10, 12);
        Object unbalanced = SortedCollection.combine(8, t1);
        assertEquals("(8 8 (10 10 12))", SortedCollection.toString(unbalanced));
    }

    public void testSimpleInsert() {
        SortedCollection<Int> collection = SortedCollection.create(Comparator.RationalNatural);
        assertEquals(SortedCollection.emptyTree, collection.root);
        assertEquals(Comparator.RationalNatural, collection.cmp);
        assertEquals(0, collection.size());
        assertEquals(".", SortedCollection.toString(collection.root));
        collection = collection.add(Int.create(1));
        assertEquals(1, collection.size());
        assertEquals("1", SortedCollection.toString(collection.root));
        collection = collection.add(Int.create(2));
        assertEquals(2, collection.size());
        assertEquals("(1 1 2)", SortedCollection.toString(collection.root));
        collection = collection.add(Int.create(0));
        assertEquals(3, collection.size());
        assertEquals("((0 0 1) 0 2)", SortedCollection.toString(collection.root));
    }
    
    public void testInsertAndFind() {
        SortedCollection<Int> collection = SortedCollection.create(Comparator.RationalNatural);
        assertEquals(0, collection.size());
        assertFalse(collection.contains(Int.ONE));
        collection = collection.add(Int.ONE);
        assertTrue(collection.contains(Int.ONE));
        assertFalse(collection.contains(Int.TWO));
        collection = collection.add(Int.TWO);
        assertTrue(collection.contains(Int.ONE));
        assertTrue(collection.contains(Int.TWO));
    }

    static public void testConstructionTime() {

        int N = 100;
        int K = 10;

        Object[] trees = new Object[K];

        for (int i = 0; i < N; i++) {

            SortedCollection<Int> collection = SortedCollection.create(Comparator.RationalNatural);
            collection = collection.add(Int.create(2));
            collection = collection.add(Int.create(4));
            collection = collection.add(Int.create(0));
            collection = collection.add(Int.create(1));
            collection = collection.add(Int.create(3));
            assertEquals(5, collection.size());
            trees[i % K] = collection;

        }
    }
    
    public void testReplace() {
        int N = 400;

        SortedCollection<Integer> collection = SortedCollection.create(cmp);
        
        for (int i = 0; i < N; i++) {
          collection = collection.add(i*2);
        }
        assertEquals(N, collection.size());
        
        int pos = N/3;
        assertEquals((Integer) (2*pos), collection.get(pos));
        collection = collection.replaceAt(pos, 2*pos+1);
        assertEquals(N, collection.size());
        assertEquals((Integer) (2*pos+1), collection.get(pos));
        for (int i = 0; i < N; i++) {
            Integer expected = i == pos ? 2*i+1 : 2*i;
            assertEquals(expected, collection.get(i));
          }
    }
    
    public void testInsertWithHomomorphism() {
    	SortedCollection<Integer> collection = SortedCollection.create(cmp);
        int N = 400;
        for (int i = N; i > 0; i--) {
            collection = collection.add(i);
          }
          assertEquals(N, collection.size());
          for (int i = 0; i < N; i++) {
              collection = collection.add(i*2);
          }
          assertEquals(2*N, collection.size());
          
          for (int i = 0; i < N; i++) {
              collection = collection.remove(i*2);
          }
          assertEquals(N, collection.size());
          collection = collection.addMap(MonoidMaps.sumOfInteger);
          Integer sum = collection.getImage(MonoidMaps.sumOfInteger);
          assertEquals(Integer.valueOf(N*(N+1)/2), sum);
          //TODO test fails, when fixed, continue with add and remove
//          for (int i = 0; i < N; i++) {
//            int x = i*2;
//            collection = collection.add(x);
//            sum = sum + x;
//            assertEquals(sum, collection.getImage(MonoidMaps.sumOfInteger));
//          }
    }

}
