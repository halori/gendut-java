package org.gendut.collection.mutable;

public class MutableValue<A> {
	
	public MutableValue(A initialValue) {
		value = initialValue;
	}
	
	private A value;
	public A set(A newValue) {
		A oldValue = value;
		value = newValue;
		return oldValue;
	}
	
	public A get() { return value; }
}
