package org.gendut.algorithm;

import org.gendut.collection.Array;
import org.gendut.collection.mutable.ExtendibleArray;
import org.gendut.collection.mutable.MutableHashSet;
import org.gendut.func.Function;
import org.gendut.seq.Seq;

public final class DirectedGraphs {
	/**
	 * return the topological order of an acyclic directed graph. The graph is
	 * given by a map from each node to the node's successors. If the graph
	 * contains a cycle, an exception is thrown. TODO: unit tests TODO: explicit
	 * stack or Kahn's algorithm for long paths.
	 */
	public static <T> Array<T> topologicalSort(Seq<T> nodes, final Function<T, ? extends Seq<T>> successors) {

		MutableHashSet<T> visited = new MutableHashSet<T>();
		MutableHashSet<T> queued = new MutableHashSet<T>();

		ExtendibleArray<T> list = new ExtendibleArray<T>();
		while (!nodes.isEmpty()) {
			T n = nodes.first();
			nodes = nodes.rest();
			if (!queued.contains(n)) {
				topSortVisit(successors, n, visited, queued, list);
			}
		}

		return list.asConstant();

	}

	private static <T> void topSortVisit(Function<T, ? extends Seq<T>> successors, T n, MutableHashSet<T> visited,
			MutableHashSet<T> queued, ExtendibleArray<T> list) {

		if (queued.contains(n)) {
			return;
		}
		if (visited.contains(n)) {
			throw new IllegalArgumentException("graph contains a cycle");
		}

		visited.add(n);

		Seq<T> succs = successors.get(n);
		
		while (!succs.isEmpty()) {
			topSortVisit(successors, succs.first(), visited, queued, list);
			succs = succs.rest();
		}
		visited.remove(n);
		queued.add(n);
		list.add(n);
	}
}
