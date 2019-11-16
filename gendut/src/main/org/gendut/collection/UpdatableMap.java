package org.gendut.collection;

public interface UpdatableMap<Key, Value, ReturnType extends UpdatableMap<Key, Value, ReturnType>>
    extends ImmutableMap<Key, Value> {
  public ReturnType put(Key key, Value val);

  public ReturnType remove(Key key);

  public ReturnType clear();
}
