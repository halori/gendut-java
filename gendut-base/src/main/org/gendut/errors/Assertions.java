package org.gendut.errors;

public class Assertions {
	public static void assertion(String message, boolean cond) {
		if (!cond)
			throw new IllegalStateException("Assertion failed: " + message);
	}

	public static void assertEquals(String message, long x, long y) {
		if (x != y)
			throw new IllegalStateException("Assertion failed: " + message);
	}

	public static void assertEquals(String message, Object x, Object y) {
		if (!x.equals(y))
			throw new IllegalStateException("Assertion failed: " + message);
	}

	public static void assertNotNull(String message, Object x) {
		if (x == null)
			throw new NullPointerException("Assertion failed: " + message);
	}
}
