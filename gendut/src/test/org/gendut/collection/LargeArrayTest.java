package org.gendut.collection;

import org.gendut.arithmetic.Int;

import junit.framework.TestCase;

public class LargeArrayTest extends TestCase {

    static final int N = 10;

    static public void testCatenation() {
        LargeArray<Integer> a = new LargeArray<Integer>();
        a = a.add(1).add(2).add(3);
        assertEquals("[1, 2, 3]", a.toString());
        LargeArray<Integer> b = a.add(0).add(2).add(8).add(9).add(10).add(12).add(13);
        assertEquals("[1, 2, 3, 1, 2, 3, 0, 2, 8, 9, 10, 12, 13]", a.catenate(b).toString());
        assertEquals("[1, 2, 3, 0, 2, 8, 9, 10, 12, 13, 1, 2, 3]", b.catenate(a).toString());  
    }
    
    static public void testReplaceAt() {
        LargeArray<Integer> a = new LargeArray<Integer>();
        a = a.add(1).add(2).add(3);
        a = a.catenate(a);
        a = a.replaceAt(0, 7);
        assertEquals("[7, 2, 3, 1, 2, 3]", a.toString());
        a = a.replaceAt(5, 9);
        assertEquals("[7, 2, 3, 1, 2, 9]", a.toString());
        a = a.replaceAt(3, 5);
        assertEquals("[7, 2, 3, 5, 2, 9]", a.toString());
    }
    
    static public void testInsertAt() {
        LargeArray<Integer> a = new LargeArray<Integer>();
        a = a.add(1).add(2).add(3);
        a = a.catenate(a);
        a = a.insertAt(0, 7);
        assertEquals("[7, 1, 2, 3, 1, 2, 3]", a.toString());
        a = a.insertAt(7, 0);
        assertEquals("[7, 1, 2, 3, 1, 2, 3, 0]", a.toString());
        a = a.insertAt(3, 8);
        assertEquals("[7, 1, 2, 8, 3, 1, 2, 3, 0]", a.toString());
    }
    
    static public void testFirstOf() {
    	LargeArray<Integer> a = new LargeArray<Integer>();
    	a = a.add(1).add(2).add(3);
    	a = a.catenate(a);
    	a = a.add(7);
    	assertEquals(Int.create(0), a.firstOf(1));
    	assertEquals(Int.create(6), a.firstOf(7));
    	assertEquals(Int.create(-1), a.firstOf(4));
    }
    
    static public void testLastOf() {
      LargeArray<Integer> a = new LargeArray<Integer>();
      a = a.add(1).add(2).add(3);
      a = a.catenate(a);
      a = a.add(7);
      assertEquals(Int.create(6), a.lastOf(7));
      assertEquals(Int.create(-1), a.lastOf(4));
      assertEquals(Int.create(3), a.lastOf(1));
    }
    
    static public void testaddMap() {
      LargeArray<Integer> a = new LargeArray<Integer>();
      a = a.add(1).add(2).add(1);
     
      
      Integer n = 4;
      for (int i = 0; i < 20; i++) {
        a = a.catenate(a);
        n = n + n;
      }
      a = a.addMap(MonoidMaps.sumOfInteger);
      assertEquals(n, a.getImage(MonoidMaps.sumOfInteger));  
    }
}

