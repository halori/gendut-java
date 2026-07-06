package org.gendut.collection;


public class ImmutableHashMap<Key, Value> extends
    AbstractMapFromSet<Key, Value, ImmutableHashMap<Key, Value>> implements
    UpdatableMap<Key, Value, ImmutableHashMap<Key, Value>> {

  private ImmutableHashMap() {
    super(ImmutableHashSet.create());
  }

  private ImmutableHashMap(UpdatableSet<Object, ?> set) {
    super(set);
  }

  @SuppressWarnings("rawtypes")
  private static final ImmutableHashMap emptyMap = new ImmutableHashMap();

  @SuppressWarnings("unchecked")
  public static <Key, Value> ImmutableHashMap<Key, Value> create() {
    return emptyMap;
  }

  public ImmutableHashMap<Key, Value> put(Key key, Value val) {
    return new ImmutableHashMap<Key, Value>(asSet
        .add(new MapEntry<Key, Value>(key, val)));
  }

  public ImmutableHashMap<Key, Value> remove(Key key) {
    return new ImmutableHashMap<Key, Value>(asSet.remove(key));
  }

   @SuppressWarnings("unchecked")
  public ImmutableHashMap<Key, Value> clear() {
    return emptyMap;
  }
}