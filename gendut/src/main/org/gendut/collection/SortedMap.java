package org.gendut.collection;

import org.gendut.algorithm.Comparator;

public final class SortedMap<Key, Value> extends
    AbstractMapFromSet<Key, Value, SortedMap<Key, Value>> implements
    UpdatableMap<Key, Value, SortedMap<Key, Value>> {

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private SortedMap(Comparator<? super Key> cmp) {
    super((SortedSet) SortedSet.create(MapEntry.keyComparator(cmp)));
  }

  protected SortedMap(UpdatableSet<Object, ?> set) {
    super(set);
  }

  public static <Key, Value> SortedMap<Key, Value> create(
      Comparator<? super Key> cmp) {
    return new SortedMap<Key, Value>(cmp);
  }

  public SortedMap<Key, Value> put(Key key, Value val) {
    return new SortedMap<Key, Value>(asSet.add(new MapEntry<Key, Value>(key,
        val)));
  }

  public SortedMap<Key, Value> remove(Key key) {
    return new SortedMap<Key, Value>(asSet.remove(key));
  }

  public SortedMap<Key, Value> clear() {
    return new SortedMap<Key, Value>(asSet.clear());
  }
}
