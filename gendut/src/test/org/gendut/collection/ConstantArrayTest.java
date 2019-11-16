package org.gendut.collection;


import junit.framework.TestCase;

public class ConstantArrayTest extends TestCase {
  
  static final int N = 100;
  
  static public void testByteArray() {
	  Byte a = 12;
	  Byte b = 7;
	  ConstantArray<Byte> array = ConstantArray.pair(a, b);
	  byte[] bytes = ConstantArray.bytes(array);
	  assertEquals(2, bytes.length);
	  assertEquals((byte)a, bytes[0]);
	  assertEquals((byte)b, bytes[1]);
  }
  
}
