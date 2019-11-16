package org.gendut.collection.mutable;

import java.util.NoSuchElementException;

import org.gendut.collection.Collections;
import org.gendut.collection.ImmutableHashMap;
import org.gendut.collection.ImmutableSet;
import org.gendut.func.Pair;
import org.gendut.iterator.ForwardIterator;
import org.gendut.iterator.IterableCollection;
import org.gendut.seq.Seq;
import org.gendut.seq.SeqFromIterator;

// ! Mutable Hashmap
/* <literate> */
/**
 * This mutable hashmap is a helper class. It serves as a replacement for the
 * hashmap implementation from the Java collections, which is not present in
 * Java ME.<br />
 * The key values must not be null. Furthermore, their equal-methods must be
 * consistent to their hash codes. Note that for the equality and hash value of
 * maps, iteration order is irrelevant. The Function-interface is not
 * implemented since functions must be immutable.
 */
@SuppressWarnings("unchecked")
public final class MutableHashMap<Key, Value> implements
		IterableCollection<Pair<Key, Value>> {
	private int size = 0;
	private Object[] table;

	/*
	 * Wrap extendible array class so that the class can be used to
	 * differentiate between user-objects and lists. It is essential that this
	 * class is private.
	 */
	private class List {
		ExtendibleArray<Object> arr = new ExtendibleArray<Object>();

		@Override
		public String toString() {
			return arr.toString();
		}

		@Override
		public boolean equals(Object obj) {
			throw new IllegalStateException(
					"Internal class is incomparable. This exception points "
							+ "to an implementation error of the enclosing class.");
		}

		@Override
		public int hashCode() {
			throw new IllegalStateException(
					"Internal class has no meaningful hashcode. This "
							+ "exception points to an implementation error of the "
							+ "enclosing class.");
		}
	}

	public final static int DEFAULT_INITIAL_CAPACITY = 1513;

	public MutableHashMap() {
		table = new Object[2 * DEFAULT_INITIAL_CAPACITY];
	}

	public MutableHashMap(long initialCapacity) {
		if (initialCapacity > Integer.MAX_VALUE / 2)
			throw new IllegalArgumentException("Capacity too large "
					+ initialCapacity);
		table = new Object[(int) (2 * initialCapacity)];
	}

	/**
	 * The key value must not be null. Furthermore, its equal-method must be
	 * consistent with the hash values.
	 */
	final public void put(Key a, Value b) {
		if (size * 4 > table.length) {
			rehash(5 * table.length);
		}
		int hc = a.hashCode();
		if (hc < 0) {
			hc = -hc;
		}
		int k = 2 * (hc % (table.length / 2));
		if (table[k] == null) {
			table[k] = a;
			table[k + 1] = b;
		} else {
			List list = null;
			if (table[k].getClass() == List.class) {
				list = (List) table[k];
				for (int i = 0; i < list.arr.size(); i = i + 2) {
					if (a.equals(list.arr.get(i))) {
						list.arr.set(i, a);
						list.arr.set(i + 1, b);
						return;
					}// `if`
				}// `for`
			} else {
				if (a.equals(table[k])) {
					table[k] = a;
					table[k + 1] = b;
					return;
				}
				list = new List();
				list.arr.addLast(table[k]);
				list.arr.addLast(table[k + 1]);
				table[k] = list;
				table[k + 1] = null;
			}// `else`
			list.arr.addLast(a);
			list.arr.addLast(b);
		}// else
		size = size + 1;
	}

	final public void remove(Key a) {
		if ((size * 8 < table.length)
				&& (DEFAULT_INITIAL_CAPACITY * 8 < table.length)) {
			rehash(table.length / 4);
		}
		int hc = a.hashCode();
		if (hc < 0) {
			hc = -hc;
		}
		int k = 2 * (hc % (table.length / 2));
		if (table[k] == null) {
			return;
		} else if (table[k].getClass() != List.class) {
			if (a.equals(table[k])) {
				table[k] = null;
				table[k + 1] = null;
				size = size - 1;
			} // `if`
		} else {
			List list = (List) table[k];
			for (int i = 0; i < list.arr.size(); i = i + 2) {
				if (a.equals(list.arr.get(i))) {
					Object value = list.arr.removeLast();
					Object key = list.arr.removeLast();
					if (i < list.arr.size()) {
						list.arr.set(i, key);
						list.arr.set(i + 1, value);
					}// `if`
					if (list.arr.size() == 2) {
						table[k] = list.arr.get(0);
						table[k + 1] = list.arr.get(1);
					}// `if`
					size = size - 1;
				}// `if`
			}// `for`
		}// `else`
	}

	final public boolean containsKey(Key a) {
		int hc = a.hashCode();
		if (hc < 0) {
			hc = -hc;
		}
		int k = 2 * (hc % (table.length / 2));
		if (table[k] == null) {
			return false;
		} else if (table[k].getClass() != List.class) {
			return a.equals(table[k]);
		} else {
			List list = (List) table[k];
			for (int i = 0; i < list.arr.size(); i = i + 2) {
				if (a.equals(list.arr.get(i))) {
					return true;
				}
			}// `for`
			return false;
		}// `else`
	}

	final public Value get(Key a) {
		int hc = a.hashCode();
		if (hc < 0) {
			hc = -hc;
		}
		int k = 2 * (hc % (table.length / 2));
		if (table[k] == null) {
			return (Value) table[k + 1];
		} else if (table[k].getClass() != List.class) {
			if (a.equals(table[k])) {
				return (Value) table[k + 1];
			} else {
				return null;
			}
		} else {
			List list = (List) table[k];
			for (int i = 0; i < list.arr.size(); i = i + 2) {
				if (a.equals(list.arr.get(i))) {
					return (Value) list.arr.get(i + 1);
				}
			}// `for`
			return null;
		}// `else`
	}

	final public Key findKey(Key a) {
		int hc = a.hashCode();
		if (hc < 0) {
			hc = -hc;
		}
		int k = 2 * (hc % (table.length / 2));
		if (table[k] == null) {
			return (Key) table[k];
		} else if (table[k].getClass() != List.class) {
			if (a.equals(table[k])) {
				return (Key) table[k];
			} else {
				return null;
			}
		} else {
			List list = (List) table[k];
			for (int i = 0; i < list.arr.size(); i = i + 2) {
				if (a.equals(list.arr.get(i))) {
					return (Key) list.arr.get(i);
				}
			}// `for`
			return null;
		}// `else`
	}

	final public long size() {
		return size;
	}

	final public void assertInvariant() {
		int trueSize = 0;
		int trueHashCode = 0;
		for (int i = 0; i < table.length; i = i + 2) {
			if (table[i] != null) {
				if (table[i].getClass() != List.class) {
					trueSize = trueSize + 1;
					int hc = table[i].hashCode();
					if (hc < 0) {
						hc = -hc;
					}

					if (2 * (hc % (table.length / 2)) != i) {
						throw new IllegalStateException("Wrong table position.");
					}

					trueHashCode = trueHashCode + hc;
				} else {
					List list = (List) table[i];
					trueSize = trueSize + (int) list.arr.size() / 2;
					for (int k = 0; k < list.arr.size(); k = k + 2) {
						int hc = list.arr.get(k).hashCode();
						if (hc < 0) {
							hc = -hc;
						}
						if (2 * (hc % (table.length / 2)) != i) {
							throw new IllegalStateException(
									"Wrong table position.");
						}
						trueHashCode = trueHashCode + hc;
					}// `for`
				}// `else`
			}// `if`
		}// `for`
		if (size != trueSize) {
			throw new IllegalStateException("Wrong size.");
		}
	}

	private class Iterator implements ForwardIterator<Pair<Key, Value>> {

		int listPos = 0;
		int tablePos = 0;

		public Iterator() {
			tablePos = advanceTablePos(0);
		}

		public Pair<Key, Value> next() {
			if (!hasNext()) {
				throw new NoSuchElementException("No next element.");
			}

			if (table[tablePos].getClass() != List.class) {
				Pair<Key, Value> result = new Pair<Key, Value>(
						(Key) table[tablePos], (Value) table[tablePos + 1]);
				tablePos = advanceTablePos(tablePos + 2);
				return result;
			} else {
				List list = (List) table[tablePos];
				Pair<Key, Value> result = new Pair<Key, Value>(
						(Key) list.arr.get(listPos),
						(Value) list.arr.get(listPos + 1));
				listPos = listPos + 2;
				if (listPos >= list.arr.size()) {
					listPos = 0;
					tablePos = advanceTablePos(tablePos + 2);
				}// `if`
				return result;
			}// `else`
		}

		public boolean hasNext() {
			return (tablePos < table.length);
		}
	}

	private class KeyIterator implements ForwardIterator<Key> {

		int listPos = 0;
		int tablePos = 0;

		public KeyIterator() {
			tablePos = advanceTablePos(0);
		}

		public Key next() {
			if (!hasNext()) {
				throw new NoSuchElementException("No next element.");
			}

			if (table[tablePos].getClass() != List.class) {
				Key result = (Key) table[tablePos];
				tablePos = advanceTablePos(tablePos + 2);
				return result;
			} else {
				List list = (List) table[tablePos];
				Key result = (Key) list.arr.get(listPos);
				listPos = listPos + 2;
				if (listPos >= list.arr.size()) {
					listPos = 0;
					tablePos = advanceTablePos(tablePos + 2);
				}// `if`
				return result;
			}// `else`
		}

		public boolean hasNext() {
			return (tablePos < table.length);
		}
	}

	private int advanceTablePos(int tablePos) {
		while ((tablePos < table.length) && (table[tablePos] == null)) {
			tablePos = tablePos + 2;
		}// `while`
		return tablePos;
	}

	final public ForwardIterator<Pair<Key, Value>> iterator() {
		return new Iterator();
	}

	final public ForwardIterator<Key> keyIterator() {
		return new KeyIterator();
	}

	public final Seq<Pair<Key, Value>> seq() {
		return SeqFromIterator.create(iterator());
	}

	private void rehash(int newCapacity) {
		MutableHashMap<Key, Value> newMap = new MutableHashMap<Key, Value>(
				newCapacity);
		int tablePos = advanceTablePos(0);
		int listPos = 0;
		while (tablePos < table.length) {
			if (table[tablePos].getClass() != List.class) {
				newMap.put((Key) table[tablePos], (Value) table[tablePos + 1]);
				tablePos = advanceTablePos(tablePos + 2);
			} else {
				List list = (List) table[tablePos];
				newMap.put((Key) list.arr.get(listPos),
						(Value) list.arr.get(listPos + 1));
				listPos = listPos + 2;
				if (listPos >= list.arr.size()) {
					listPos = 0;
					tablePos = advanceTablePos(tablePos + 2);
				}// `if`
			}// `else`
		}// `while`
		this.table = newMap.table;
	};

	/**
	 * Debug string
	 */
	public String debug() {
		StringBuffer s = new StringBuffer();
		s.append(size + " c:" + table.length / 2 + " [");
		for (int i = 0; i < table.length; i++) {
			if (i > 0) {
				s.append(",");
			}
			s.append(table[i]);
		}
		s.append("]");
		return s.toString();
	}

	public boolean contains(Pair<Key, Value> e) {
		Key k = e.first();
		if (k == null)
			return false;

		if (!containsKey(k))
			return false;

		Value v = get(k);
		if (v == null)
			return e.second() == null;
		else
			return v.equals(e.second());
	}

	public Pair<Key, Value> find(Pair<Key, Value> e) {
		Key k = findKey(e.first());
		if (k == null)
			return null;

		Value v = get(k);

		if (((v == null) && (e.second() == null))
				|| ((v != null) && (e.second() != null) && v.equals(e.second())))
			return new Pair<Key, Value>(k, v);
		else
			return null;
	}

	public Seq<Key> keySeq() {
		return SeqFromIterator.create(keyIterator());
	}

	public ImmutableSet<Key> keys() {
		return Collections.createSet(keySeq());
	}

	@Override
	public String toString() {
		return asConstant().toString();
	}

	public ImmutableHashMap<Key, Value> asConstant() {
		ImmutableHashMap<Key, Value> result = ImmutableHashMap.create();
		ForwardIterator<Pair<Key, Value>> it = iterator();
		while (it.hasNext()) {
			Pair<Key, Value> pair = it.next();
			result = result.put(pair.first(), pair.second());
		}
		return result;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || other.getClass() != MutableHashMap.class)
			return false;
		MutableHashMap<?, ?> otherMap = (MutableHashMap<?, ?>) other;
		if (this.size() != otherMap.size())
			return false;
		return this.asConstant().equals(otherMap.asConstant());
	}

	@Override
	public int hashCode() {
		return Collections.hashCodeForSet(this);
	}
}// `class`
