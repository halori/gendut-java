package org.gendut.collection;

import java.math.BigInteger;

import junit.framework.TestCase;


public class StackTest extends TestCase {
  static public void testPushPop() {
    Stack<Integer> stack = Stack.create();
    stack = stack.push(2);
    assertEquals(Integer.valueOf(2), stack.top());
    assertEquals(0, stack.pop().size());
    assertEquals(1, stack.size());
    assertEquals(2, stack.push(1).size());
    
    
    stack = Stack.create();
    final int N = 30000;
    assertEquals(0,stack.size());
    for (int i = 0; i < N ; i++) {
      stack = stack.push(i);
      assertEquals(i+1,stack.size());
    }
    for (int i = N - 1; i >= 0 ; i--) {
      assertEquals(Integer.valueOf(i), stack.top());
      assertEquals(i+1,stack.size());
      stack = stack.pop();
    }
    assertEquals(0,stack.size());
  }
  
  static public void testCreation() {
    Stack<BigInteger> A = Stack.create();
    A = A.push(BigInteger.valueOf(1));
    A = A.push(BigInteger.valueOf(2));
    A = A.push(BigInteger.valueOf(3));
    A = A.push(BigInteger.valueOf(4));
    A = A.push(BigInteger.valueOf(5));
    A = A.push(BigInteger.valueOf(6));
    A = A.push(BigInteger.valueOf(7));
    Stack<BigInteger> B = Stack.fromCollection(A);
    assertEquals("[7, 6, 5, 4, 3, 2, 1]", B.toString());
    assertEquals(A, B);
    ConstantArray<BigInteger> C = ConstantArray.fromCollection(B);
    assertEquals(A, B);
    assertEquals(A, C);
    assertEquals(A.hashCode(), C.hashCode());
    B = B.pop();
    assertEquals("[6, 5, 4, 3, 2, 1]", B.toString());
    assertNotSame(A, B);
    assertNotSame(C, B);
    B = B.push(BigInteger.valueOf(7));
    assertEquals("[7, 6, 5, 4, 3, 2, 1]", B.toString());
    assertEquals(A, B);   
  }
}
