package org.gendut.collection;

import org.gendut.iterator.ForwardIterator;
import org.gendut.seq.Seq;

import junit.framework.TestCase;

public class HashIteratorTest extends TestCase {

    static int N = 200000;
    
    public void testConstruction() {
        UpdatableSet<Integer, ?> set = ImmutableHashSet.create();
        for (int i = 0; i < N; i++)
            set = set.add(i);
    }
    
    public void testConstructionAndIteration() {
        UpdatableSet<Integer, ?> set = ImmutableHashSet.create();
        boolean[] inSet = new boolean[N];
        for (int i = 0; i < N; i++) {
            set = set.add(i);
            inSet[i] = true;
        }
        
        ForwardIterator<Integer> it = set.iterator();

        for (int i = 0; i < N; i++) {
            assertTrue(it.hasNext());
            int v = (int)it.next();
            assertTrue(inSet[v]);
            inSet[v] = false;
        }
        assertFalse(it.hasNext());
    }
    
    public void testConstructionAndISequencing() {
        UpdatableSet<Integer, ?> set = ImmutableHashSet.create();
        
        boolean[] inSet = new boolean[N];
        for (int i = 0; i < N; i++) {
            set = set.add(i);
            inSet[i] = true;
        }
        
        Seq<Integer> seq = set;

        for (int i = 0; i < N; i++) {
            assertFalse(seq.isEmpty());
            int v = seq.first();
            assertTrue(inSet[v]);
            inSet[v] = false;
            seq = seq.rest();
        }
        assertTrue(seq.isEmpty());
    }
}
