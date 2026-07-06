package org.gendut.collection;

public interface BinaryFunction<X, Y, E> {
	E get(X x, Y y);
}
