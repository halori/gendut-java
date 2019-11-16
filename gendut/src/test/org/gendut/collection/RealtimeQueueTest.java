package org.gendut.collection;

import org.gendut.collection.RealtimeQueue;
import org.gendut.collection.Stack;

import junit.framework.TestCase;

public class RealtimeQueueTest extends TestCase {
  
  public void testInterface() {
    RealtimeQueue<String> q = RealtimeQueue.create();
    assertEquals(0, q.size());
    q = q.append("1");
    assertEquals("1", q.first());
    assertEquals(1, q.size());
    assertEquals(0, q.rest().size());
    q = q.append("2");
    q = q.append("3");
    assertEquals(3, q.size());
    assertEquals("1", q.first());
    assertEquals("2", q.rest().first());
    assertEquals("3", q.rest().rest().first());
    q = q.rest();
    q = q.append("4");
    q = q.append("5");
    q = q.append("6");
    assertEquals("2", q.first());
    assertEquals("3", q.rest().first());
    assertEquals("4", q.rest().rest().first());
    assertEquals("5", q.rest().rest().rest().first());
    assertEquals("6", q.rest().rest().rest().rest().first());
  }
  
  public void testWithMany() {
    final int N = 20000;
    RealtimeQueue<Integer> q = RealtimeQueue.create();
    Stack<Integer> s = Stack.create();
    
    for (int i = 0; i < 2*N; i++) {
      q = q.append(i);
      s = s.push(2*N - i -1);
    }
    
    assertEquals(s, q);
    assertEquals(s.hashCode(), q.hashCode());
    
    for (int i = 0; i < N; i++) {
      assertEquals(Integer.valueOf(i), q.first());
      q = q.rest();
    }
    for (int i = 0; i < N; i++) {
      q = q.append(i+2*N);
    }
    for (int i = 0; i < 2*N; i++) {
      assertEquals(Integer.valueOf(i+N), q.first());
      q = q.rest();
    }
    assertEquals(0, q.size());
  }
}
