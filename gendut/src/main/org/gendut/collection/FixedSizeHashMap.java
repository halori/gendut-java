package org.gendut.collection;

import org.gendut.arithmetic.Int;

public class FixedSizeHashMap<Key, Value> extends
    AbstractMapFromSet<Key, Value, FixedSizeHashMap<Key, Value>> implements
    UpdatableMap<Key, Value, FixedSizeHashMap<Key, Value>> {

  private FixedSizeHashMap(long size) {
    super(FixedSizeHashSet.create(size));
  }

  private FixedSizeHashMap(UpdatableSet<Object, ?> set) {
    super(set);
  }

  public static <Key, Value> FixedSizeHashMap<Key, Value> create(long size) {
    return new FixedSizeHashMap<Key, Value>(size);
  }

  public FixedSizeHashMap<Key, Value> put(Key key, Value val) {
    return new FixedSizeHashMap<Key, Value>(asSet.add(new MapEntry<Key, Value>(
        key, val)));
  }

  public FixedSizeHashMap<Key, Value> remove(Key key) {
    return new FixedSizeHashMap<Key, Value>(asSet.remove(key));
  }

  @SuppressWarnings("rawtypes")
  public long capacity() {
    return ((FixedSizeHashSet) asSet).capacity();
  }

  public Int elementCount() {
    return Int.create(size());
  }

  public FixedSizeHashMap<Key, Value> clear() {
    return new FixedSizeHashMap<Key, Value>(capacity());
  }
}