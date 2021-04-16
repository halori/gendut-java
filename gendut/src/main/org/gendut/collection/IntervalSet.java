package org.gendut.collection;

import java.util.Comparator;

import org.gendut.arithmetic.Int;
import org.gendut.collection.mutable.ExtendibleArray;
import org.gendut.collection.mutable.MutableValue;
import org.gendut.func.Function;
import org.gendut.iterator.ForwardIterator;
import org.gendut.seq.Seq;
import org.gendut.seq.SeqFromIterator;

// !Immutable Set of Intervals
/**
 * This is an immutable set of intervals that allows some common queries. It is
 * implemented by an interval tree. Intervals sets identify intervals by the
 * lexicographic order of their boundaries.
 */
public final class IntervalSet<T, I extends IntervalRecord<T, I>> extends
		AbstractCollection<I> implements UpdatableSet<I, IntervalSet<T, I>> {
	/*
	 * A tree can be a) empty, or b) a leaf (an interval), or c) a combination
	 * of two subtrees (one of it non-empty) plus a node item.
	 * 
	 * We use AVL trees for our implementation.
	 * 
	 * the value of a node is a) the tree itself if it is a leaf, or b) the
	 * value left-most subtree.
	 */
	private final static class BinNode<T, I extends IntervalRecord<T, I>> {
		public final Object left, right;

		public final I interval;

		public final I maxEnd;

		public final int height;

		public final int count;

		private BinNode() {
			left = right = null;
			maxEnd = null;
			interval = null;
			height = 0;
			count = 0;
		}

		@SuppressWarnings({ "rawtypes" })
		static final public BinNode emptyTree = new BinNode();

		@SuppressWarnings({ "unchecked" })
		public BinNode(Object left, Object right, I interval, T maxEnd) {
			if (left == emptyTree && right == emptyTree)
				throw new IllegalArgumentException(
						"left and right trees are empty");
			this.left = left;
			this.right = right;
			I leftMax = (I) maxEnd(left);
			I rightMax = (I) maxEnd(right);
			I childrenMax = leftMax == null ? rightMax
					: rightMax == null ? leftMax : leftMax
							.compareEnds(rightMax) < 0 ? rightMax : leftMax;
			this.maxEnd = childrenMax.compareEnds(interval) > 0 ? childrenMax
					: interval;
			this.interval = interval;
			this.count = count(left) + count(right) + 1;
			int leftHeight = height(left);
			int rightHeight = height(right);
			int balance = rightHeight - leftHeight;
			if ((balance < -1) || (balance > 1))
				throw new IllegalArgumentException("avl-balance violated");
			int h = leftHeight;
			if (rightHeight > h)
				h = rightHeight;
			height = h + 1;
		}

		public String toString() {
			return IntervalSet.toString(this);
		}
	}// `inner class`

	static final Object emptyTree = BinNode.emptyTree;

	static final Object noElementFound = new Object() {
		public String toString() {
			return "non-existing object";
		};
	};

	@SuppressWarnings({ "unchecked", "rawtypes" })
	static final Comparator<IntervalRecord<?, ?>> intervalComperator = new Comparator<IntervalRecord<?, ?>>() {
		public int compare(IntervalRecord a, IntervalRecord b) {
			return a.compare(b);
		}
	};

	private final Object tree;

	private IntervalSet(Object tree) {
		this.tree = tree;
	}

	@SuppressWarnings({ "rawtypes" })
	static Object left(Object tree) {
		if (tree.getClass() != BinNode.class)
			return emptyTree;
		else
			return ((BinNode) tree).left;
	}

	@SuppressWarnings("rawtypes")
	static Object right(Object tree) {
		if (tree.getClass() != BinNode.class)
			return emptyTree;
		else
			return ((BinNode) tree).right;
	}

	@SuppressWarnings("rawtypes")
	static IntervalRecord maxEnd(Object tree) {
		if ((tree == null) || (tree.getClass() != BinNode.class))
			return (IntervalRecord) tree;
		else
			return ((BinNode) tree).maxEnd;
	}

	@SuppressWarnings("rawtypes")
	static IntervalRecord interval(Object tree) {
		if (tree.getClass() != BinNode.class)
			return (IntervalRecord) tree;
		else
			return ((BinNode) tree).interval;
	}

	/**
	 * calculate the height of a tree. Note that every object (including null)
	 * other than a node is interpreted as a leaf and therefore has height 1.
	 */
	@SuppressWarnings("rawtypes")
	static int height(Object tree) {
		if ((tree == null) || (tree.getClass() != BinNode.class))
			return 1;
		else
			return ((BinNode) tree).height;
	}

	/**
	 * calculate the number of elements of a tree.
	 */
	@SuppressWarnings("rawtypes")
	static int count(Object tree) {
		if ((tree == null) || (tree.getClass() != BinNode.class))
			return 1;
		else
			return ((BinNode) tree).count;
	}

	static boolean isLeaf(Object tree) {
		return ((tree == null) || (tree.getClass() != BinNode.class));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	static Object createFromSortedList(Array<? extends IntervalRecord> list,
			long start, long end) {
		if (start >= end)
			return emptyTree;
		if (start == end - 1)
			return list.get(start);
		long m = (start + end) / 2;
		return combine(createFromSortedList(list, start, m),
				createFromSortedList(list, m + 1, end), list.get(m));
	}

	/**
	 * combines two trees, if unbalanced throws an exception The value of the
	 * tree will be the value of the leftmost non-empty subtree
	 */
	@SuppressWarnings("unchecked")
	static <T, I extends IntervalRecord<T, I>> Object combine(Object left,
			Object right, I interval) {
		if ((left == emptyTree) && (right == emptyTree))
			return interval;
		T max = (left != emptyTree) ? (T) maxEnd(left) : (T) maxEnd(right);
		return new BinNode<T, I>(left, right, interval, max);
	}

	@SuppressWarnings("unchecked")
	static <T, I extends IntervalRecord<T, I>> Object balance(Object left,
			Object right, I interval) {
		int heightLeft = height(left);
		int heightRight = height(right);
		int diff = heightRight - heightLeft;
		if (diff >= -1 && diff <= 1)
			return combine(left, right, interval);
		else if (diff < -1) {
			Object leftLeft = left(left);
			Object leftRight = right(left);
			if (height(leftLeft) - height(leftRight) < 0)
				return combine(
						combine(leftLeft, left(leftRight), interval(left)),
						combine(right(leftRight), right, interval),
						interval(leftRight));
			else
				return combine(leftLeft, combine(leftRight, right, interval),
						interval(left));
		} else {
			Object rightLeft = left(right);
			Object rightRight = right(right);
			if (height(rightRight) - height(rightLeft) < 0)
				return combine(combine(left, left(rightLeft), interval),
						combine(right(rightLeft), rightRight, interval(right)),
						interval(rightLeft));
			else
				return combine(combine(left, rightLeft, interval), rightRight,
						interval(right));
		}
	}

	@SuppressWarnings("unchecked")
	static <T, I extends IntervalRecord<T, I>> Object insert(Object tree, I interval) {
		if (tree == emptyTree)
			return interval;
		int c = interval(tree).compare(interval);
		if (c == 0)
			return combine(left(tree), right(tree), interval);
		if (isLeaf(tree)) {
			if (c < 0)
				return combine(tree, emptyTree, interval);
			else
				return combine(emptyTree, tree, interval);
		} else {
			if (c < 0)
				return balance(left(tree), insert(right(tree), interval),
						interval(tree));
			else
				return balance(insert(left(tree), interval), right(tree),
						interval(tree));
		}
	}

	@SuppressWarnings("unchecked")
	static <T, N, I extends IntervalRecord<T, I>> N containing(Object tree, T point,
			MonoidMap<I, N> hom, Function<N, Boolean> isAbsorbing,
			MutableValue<Boolean> isFinished, N leftResult) {
		if (tree == emptyTree)
			throw new IllegalArgumentException("empty tree is not allowed");
		if (isAbsorbing.get(leftResult)) {
			isFinished.set(true);
			return leftResult;
		}
		I interval = (I) interval(tree);
		if (isLeaf(tree)) {
			if (interval.contains(point)) {
				leftResult = hom.add(leftResult, hom.map(interval));
				return leftResult;
			} else {
				isFinished.set(true);
				return leftResult;
			}
		}
		if (left(tree) != emptyTree
				&& maxEnd(left(tree)).compareEndTo(point) > 0) {
			leftResult = containing(left(tree), point, hom, isAbsorbing,
					isFinished, leftResult);
			if (isFinished.get())
				return leftResult;
		}
		if (interval.contains(point)) {
			leftResult = hom.add(leftResult, hom.map(interval));
		}
		if (right(tree) != emptyTree
				&& maxEnd(right(tree)).compareEndTo(point) > 0)
			return containing(right(tree), point, hom, isAbsorbing, isFinished,
					leftResult);
		else
			return leftResult;
	}

	@SuppressWarnings("unchecked")
	static <T, N, I extends IntervalRecord<T, I>> N intersecting(Object tree,
			T start, T end, MonoidMap<I, N> hom,
			Function<N, Boolean> isAbsorbing, MutableValue<Boolean> isFinished,
			N leftResult) {
		if (tree == emptyTree)
			throw new IllegalArgumentException("empty tree is not allowed");
		if (isAbsorbing.get(leftResult)) {
			isFinished.set(true);
			return leftResult;
		}
		I interval = (I) interval(tree);
		if (isLeaf(tree)) {
			if (interval.intersects(start, end)) {
				leftResult = hom.add(leftResult, hom.map(interval));
				return leftResult;
			} else {
				isFinished.set(true);
				return leftResult;
			}
		}
		if (left(tree) != emptyTree
				&& maxEnd(left(tree)).compareEndTo(start) > 0) {
			leftResult = intersecting(left(tree), start, end, hom, isAbsorbing,
					isFinished, leftResult);
			if (isFinished.get())
				return leftResult;
		}
		if (interval.intersects(start, end)) {
			leftResult = hom.add(leftResult, hom.map(interval));
		}
		if (right(tree) != emptyTree
				&& maxEnd(right(tree)).compareEndTo(start) > 0)
			return intersecting(right(tree), start, end, hom, isAbsorbing,
					isFinished, leftResult);
		else
			return leftResult;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	static IntervalRecord find(Object tree, IntervalRecord e) {
		if (tree == emptyTree)
			return null;
		int cmp = interval(tree).compare(e);
		if (cmp == 0)
			return interval(tree);
		else if (isLeaf(tree))
			return null;
		else if (cmp > 0)
			return find(left(tree), e);
		else
			return find(right(tree), e);
	}

	@SuppressWarnings("unchecked")
	static <T, I extends IntervalRecord<T, I>> Object remove(Object tree, I interval) {
		if (tree == emptyTree)
			return tree;
		int c = interval(tree).compare(interval);
		if (isLeaf(tree)) {
			return (c == 0) ? emptyTree : tree;
		}
		if (c < 0)
			return balance(left(tree), remove(right(tree), interval),
					interval(tree));
		else if (c > 0)
			return balance(remove(left(tree), interval), right(tree),
					interval(tree));
		else {
			Object left = left(tree);
			Object right = right(tree);
			if (left == emptyTree)
				return right;
			else if (right == emptyTree)
				return left;
			else
				return balance(left, removeFirst(right), first(right));
		}
	}

	@SuppressWarnings("rawtypes")
	private static IntervalRecord first(Object tree) {
		while (left(tree) != emptyTree) {
			tree = left(tree);
		}
		return interval(tree);
	}

	@SuppressWarnings("unchecked")
	private static Object removeFirst(Object tree) {
		if (left(tree) != emptyTree)
			return balance(removeFirst(left(tree)), right(tree), interval(tree));
		else
			return right(tree);
	}

	static String toString(Object root) {
		StringBuffer str = new StringBuffer();
		buildString(root, str);
		return str.toString();
	}

	private static void buildString(Object root, StringBuffer str) {
		if (root == emptyTree) {
			str.append('.');
			return;
		}
		if (isLeaf(root)) {
			str.append(root == null ? "<null>" : root.toString());
		} else {
			str.append('(');
			buildString(left(root), str);
			str.append(' ');
			str.append(maxEnd(root).end());
			str.append(':');
			str.append(interval(root));
			str.append(' ');
			buildString(right(root), str);
			str.append(')');
		}
		return;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	static final class MyIterator<I extends IntervalRecord> implements
			ForwardIterator<I> {
		final ExtendibleArray<Object> treeRest = new ExtendibleArray<Object>();

		public MyIterator(Object tree) {
			gotoFirst(tree);
		}

		private void gotoFirst(Object tree) {
			while (tree != emptyTree) {
				treeRest.push(tree);
				tree = left(tree);
			}
		}

		public boolean hasNext() {
			return treeRest.size() > 0;
		}

		public I next() {
			Object bottom = treeRest.pop();
			if (isLeaf(bottom)) {
				return (I) bottom;
			} else {
				I val = (I) interval(bottom);
				gotoFirst(right(bottom));
				return val;
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private static IntervalSet emptyIntervaltree = new IntervalSet(emptyTree);

	@SuppressWarnings("unchecked")
	public static <T, I extends IntervalRecord<T, I>> IntervalSet<T, I> create() {
		return emptyIntervaltree;
	}

	public static <T, I extends IntervalRecord<T, I>> IntervalSet<T, I> fromCollection(
			ImmutableCollection<I> intervals) {
		Array<I> list = Collections.sortUnique(intervals, intervalComperator);
		Object tree = createFromSortedList(list, 0, list.size());
		return new IntervalSet<T, I>(tree);
	}

	/**
	 * Adding an interval. The insertion place is determined by the order
	 * relation, object identity is ignored. If (according to the order
	 * relation) an equal interval already exists, it is replaced by the given
	 * interval. This allows to store attributed intervals in order to have a
	 * compact representation of an interval map. TODO: test replacement
	 * behavior
	 */
	public IntervalSet<T, I> add(I e) {
		return new IntervalSet<T, I>(insert(tree, e));
	}

	@SuppressWarnings("unchecked")
	public IntervalSet<T, I> clear() {
		return emptyIntervaltree;
	}

	public IntervalSet<T, I> remove(I interval) {
		return new IntervalSet<T, I>(remove(tree, interval));
	}

	public boolean contains(I e) {
		return find(e) != null;
	}

	public Int elementCount() {
		return Int.create(count(tree));
	}

	@SuppressWarnings("unchecked")
	public I find(I e) {
		return (I) find(tree, e);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ForwardIterator<I> iterator() {
		return (ForwardIterator<I>) new MyIterator(tree);
	}

	public Seq<I> seq() {
		return SeqFromIterator.create(iterator());
	}

	public long size() throws IndexOutOfBoundsException {
		return count(tree);
	}

	static private Function<Object, Boolean> constantFalse = new Function<Object, Boolean>() {
		public Boolean get(Object e) {
			return false;
		}
	};

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Array<I> containing(T point) {
		Object intervals = containing(point, (MonoidMap) collector,
				constantFalse);
		return ((ExtendibleArray) intervals).asConstant();
	}

	public <N> N containing(T point, MonoidMap<I, N> hom,
			Function<N, Boolean> isAbsorbing) {
		if (tree == emptyTree)
			return hom.zero();
		else
			return containing(tree, point, hom, isAbsorbing,
					new MutableValue<Boolean>(false), hom.zero());
	}

	public Array<I> intersections(IntervalRecord<T, I> s) {
		return intersections(s.start(), s.end());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Array<I> intersections(T start, T end) {
		Object intervals = intersections(start, end, (MonoidMap) collector,
				constantFalse);
		return ((ExtendibleArray) intervals).asConstant();
	}

	public <N> N intersections(IntervalRecord<T, I> s, MonoidMap<I, N> hom,
			Function<N, Boolean> isAbsorbing) {
		return intersections(s.start(), s.end(), hom, isAbsorbing);
	}

	public <N> N intersections(T start, T end, MonoidMap<I, N> hom,
			Function<N, Boolean> isAbsorbing) {
		if (tree == emptyTree)
			return hom.zero();
		else
			return intersecting(tree, start, end, hom, isAbsorbing,
					new MutableValue<Boolean>(false), hom.zero());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	static private MonoidMap<IntervalRecord, Object> collector = new MonoidMap<IntervalRecord, Object>() {
		public Object map(IntervalRecord a) {
			return a;
		}

		public Object zero() {
			return new ExtendibleArray();
		}

		public Object add(Object x, Object y) {
			((ExtendibleArray) x).add(y);
			return x;
		}
	};

	public int hashCode() {
		return Collections.hashCodeForSet(this);
	}

	public boolean equals(Object obj) {
		return Collections.equalsForSets(this, obj);
	}

	public String toString() {
		return Collections.toStringIterationOrder(this);
	}

	// @Override
	public IntervalSet<T, I> minus(Set<? extends I> other) {
		IntervalSet<T, I> result = this;
		ForwardIterator<? extends I> it = other.iterator();
		while (it.hasNext()) {
			result = result.remove(it.next());
		}
		return result;
	}
}// ` class`
