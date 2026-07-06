package org.gendut.collection;

import org.gendut.func.Function;
import org.gendut.func.Pair;
import org.gendut.iterator.ForwardIterator;
import org.gendut.seq.Seq;

public interface ImmutableMap<Key, Value> extends ImmutableSet<Pair<Key, Value>>, Function<Key, Value>
{
	 public ForwardIterator<Key> keyIterator();
	  public Key findKey(Key key);
	  public Seq<Key> keySeq();
	  public ImmutableSet<Key> keys();
	  public boolean containsKey(Key key);
}
