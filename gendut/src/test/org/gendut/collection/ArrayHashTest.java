package org.gendut.collection;


import org.gendut.collection.FixedSizeHashSet;
import org.gendut.collection.ImmutableArray;
import org.gendut.collection.ImmutableHashMap;

import junit.framework.TestCase;

public class ArrayHashTest 
extends TestCase {
    static int N = 100000;
       
    static public void testOnlyFillTupleSequentialTiming() {
     
      ImmutableArray<Integer> tuple = ImmutableArray.create(N);
      assertEquals(N, tuple.size());
      for (int i = 0; i < N; i++) {
        tuple = tuple.set(i,i);
      }
    }
  
    static public void testFillTupleSequential() {
      
      ImmutableArray<Integer> tuple = ImmutableArray.create(N);
      assertEquals(N, tuple.size());
      for (int i = 0; i < N; i++) {
        tuple = tuple.set(i,i);
      }
     
      for (int i = 0; i < N; i++) {
        assertEquals(i, (int) tuple.get(i));
      }    
    }

    static public void testOnlyFillHashSetSequentialTiming() {
      
      FixedSizeHashSet<Integer> tuple = new FixedSizeHashSet<Integer>(2*N);
      for (int i = 0; i < N; i++) {
        tuple = tuple.add(i);
      }    
    }
    
    static public void testFillHashSetSequential() {
      
      FixedSizeHashSet<Integer> tuple = new FixedSizeHashSet<Integer>(2*N);
      for (int i = 0; i < N; i++) {
        tuple = tuple.add(i);
      }
     
      for (int i = 0; i < N; i++) {
        assertEquals(Integer.valueOf(i), tuple.find(i));
      }    
    }
    
    static public void testOnlyFillHashMapSequentialTiming() {
      
      ImmutableHashMap<Integer,Integer> tuple = ImmutableHashMap.create();
      for (int i = 0; i < N; i++) {
        tuple = tuple.put(i,i);
      }
  
    }

    static public void testFillHashMapSequential() {
      
      ImmutableHashMap<Integer,Integer> tuple = ImmutableHashMap.create();
      for (int i = 0; i < N; i++) {
        tuple = tuple.put(i,i);
      }
     
      for (int i = 0; i < N; i++) {
        assertEquals(Integer.valueOf(i), tuple.get(i));
      }    
    }
}
