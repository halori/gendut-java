package org.gendut.collection;

import java.util.Comparator;

import org.gendut.arithmetic.Int;
import org.gendut.collection.mutable.ExtendibleArray;
import org.gendut.collection.mutable.MutableHashMap;
import org.gendut.collection.mutable.MutableHashSet;
import org.gendut.func.Function;
import org.gendut.iterator.ForwardIterator;
import org.gendut.seq.Seq;
import org.gendut.seq.SeqFromIterator;

/**
 * A catenable tree which maintains monoid images
 */
abstract class CatenableArrayTree<E> extends AbstractList<E> implements
		CatenableArray<E> {

	final Object root;

	final Comparator<? super E> cmp;

	// TODO: a) Make all methods maintain images!

	final ImmutableHashMap<MonoidMap<E, ?>, Object> images;

	public CatenableArrayTree(Object root,
			ImmutableHashMap<MonoidMap<E, ?>, Object> images,
			Comparator<? super E> cmp) {
		super();
		if (images == null)
			images = ImmutableHashMap.create();

		this.root = root;
		this.images = images;
		this.cmp = cmp;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public CatenableArrayTree(CatenableArrayTree<E> arr1,
			CatenableArrayTree<E> arr2) {
		checkCompatibilty(arr1, arr2);

		this.cmp = arr1.cmp;

		if (arr1.images.size() != 0) {
			int n = (int) arr1.images.size();
			MonoidMap maps[] = new MonoidMap[n];
			Object[] leftImages = new Object[n];
			Object[] rightImages = new Object[n];
			Object[] resultImages = new Object[n];

			ForwardIterator<MonoidMap<E, ?>> iter = arr1.images.keys()
					.iterator();
			for (int i = 0; i < n; i++) {
				MonoidMap map = iter.next();
				maps[i] = map;
				leftImages[i] = arr1.images.get(map);
				rightImages[i] = arr2.images.get(map);
			}
			this.root = concat(arr1.root, arr2.root, leftImages, rightImages,
					maps, resultImages);

			ImmutableHashMap<MonoidMap<E, ?>, Object> newImages = ImmutableHashMap
					.create();

			for (int i = 0; i < n; i++) {
				newImages = newImages.put(maps[i], resultImages[i]);
			}
			this.images = newImages;
		} else {
			this.root = concat(arr1.root, arr2.root, null, null, null, null);
			this.images = arr1.images;
		}
	}

	private void checkCompatibilty(CatenableArrayTree<E> arr1,
			CatenableArrayTree<E> arr2) {
		if (((arr1.cmp != null) && (arr2.cmp != null))
				|| ((arr1.cmp != null) && !arr1.cmp.equals(arr2.cmp)))
			throw new IllegalArgumentException(
					"Arrays have incompatible comparators.");
		if ((arr1.images.size() != 0) || (arr2.images.size() != 0)) {
			if (!arr1.images.keys().equals(arr2.images.keys())) {
				throw new IllegalArgumentException(
						"Arrays have not the same mappings");
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	final public CatenableArray<E> catenate(CatenableArray<? extends E> array) {

		if (!(array instanceof CatenableArrayTree))
			return (CatenableArray<E>) array.catenateTo((CatenableArray) this);

		int numberOfLargeArrays = countClasses(LargeArray.class, this, array);
		int numberOfSortedCollections = countClasses(SortedCollection.class,
				this, array);

		if (numberOfLargeArrays == 0) {
			if (numberOfSortedCollections == 0)
				return new SortedSet(this, (CatenableArrayTree) array);
			else
				return new SortedCollection(this, (CatenableArrayTree) array);
		} else
			return new LargeArray(this, (CatenableArrayTree) array);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	final public CatenableArray<E> catenateTo(CatenableArray<? extends E> array) {
		return (CatenableArray<E>) array.catenate((CatenableArray) this);

	}

	@SuppressWarnings("rawtypes")
	private static int countClasses(Class clazz, Object a, Object b) {
		int count = 0;
		if (a.getClass() == clazz)
			count = count + 1;
		if (b.getClass() == clazz)
			count = count + 1;
		return count;
	}

	@SuppressWarnings("unchecked")
	final public E first() {
		return (E) first(root);
	}

	@SuppressWarnings("unchecked")
	final public E last() {
		return (E) last(root);
	}

	public final Int elementCount() {
		return count(root);
	}

	public final long size() throws IndexOutOfBoundsException {
		return elementCount().longValue();
	}

	public final ForwardIterator<E> iterator() {
		return new MyIterator<E>(root);
	}

	public final Seq<E> seq() {
		return SeqFromIterator.create(iterator());
	}

	@SuppressWarnings("unchecked")
	final public E get(Int pos) {
		return (E) get(root, pos);
	}

	final public E get(long pos) {
		return get(Int.create(pos));
	}

	final public Int firstOf(Function<E, Boolean> condition) {
		MutableHashSet<Object> nonMatchingTrees = new MutableHashSet<Object>();
		return firstOf(root, condition, nonMatchingTrees);
	}

	final public Int lastOf(Function<E, Boolean> condition) {
		MutableHashSet<Object> nonMatchingTrees = new MutableHashSet<Object>();
		return lastOf(root, condition, nonMatchingTrees);
	}

	public ForwardIterator<E> iterator(long start) {
		return iterator(Int.create(start));
	}

	final public ForwardIterator<E> iterator(Int start) {
		return new MyIterator<E>(root, start);
	}

	final public ImmutableSet<MonoidMap<E, ?>> getMaps() {
		return images.keys();
	}

	@SuppressWarnings("unchecked")
	final public <F> F getImage(MonoidMap<E, F> map) {
		return (F) value(images.get(map));
	}

	@SuppressWarnings("unchecked")
	final public <F> F computeImage(MonoidMap<E, F> map) {
		F image = (F) value(images.get(map));
		if (image == null)
			return (F) value(computeImageTree(root, map));
		else
			return image;
	}

	/*
	 * A tree can be a) empty, or b) a leaf (either null or any object except a
	 * node instances), or c) a combination of two non-empty(!) subtrees plus a
	 * node item.
	 * 
	 * We use AVL trees for our implementation.
	 * 
	 * the value of a node is a) the tree itself if it is a leaf, b) the
	 * combined value of the value of its subtrees if the tree represents a
	 * homomorphic image or otherwhise c) the value left-most subtree.
	 */
	private final static class BinNode {
		public final Object left, right, value;

		public final int height;

		public final Int count;

		private BinNode() {
			left = right = value = null;
			height = 0;
			count = Int.ZERO;
		}

		static final public BinNode emptyTree = new BinNode();

		public BinNode(Object left, Object right, Object item) {
			if (left == emptyTree)
				throw new IllegalArgumentException("left tree ist empty");
			if (right == emptyTree)
				throw new IllegalArgumentException("right tree ist empty");

			this.left = left;
			this.right = right;
			this.value = item;
			this.count = count(left).add(count(right));
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
			return CatenableArrayTree.toString(this);
		}
	}// `inner class`

	/**
	 * We set the maximum used recursion depth. For recursions deeper than this,
	 * we use an explicit stack which is located on the heap. The value is
	 * package-writable in order to allow unit tests to set a small value for
	 * testing.
	 */
	static int maxRecursionDepth = 50;

	static final Object emptyTree = BinNode.emptyTree;

	static final Object noElementFound = new Object() {
		public String toString() {
			return "non-existing object";
		};
	};

	static Object left(Object tree) {
		return ((BinNode) tree).left;
	}

	static Object right(Object tree) {
		return ((BinNode) tree).right;
	}

	static Object value(Object tree) {
		if ((tree == null) || (tree.getClass() != BinNode.class))
			return tree;
		else
			return ((BinNode) tree).value;
	}

	/**
	 * calculate the height of a tree. Note that every object (including null)
	 * other than a node is interpreted as a leaf and therefore has height 1.
	 */
	static int height(Object tree) {
		if ((tree == null) || (tree.getClass() != BinNode.class))
			return 1;
		else
			return ((BinNode) tree).height;
	}

	/**
	 * calculate the number of elements of a tree.
	 */
	static Int count(Object tree) {
		if ((tree == null) || (tree.getClass() != BinNode.class))
			return Int.ONE;
		else
			return ((BinNode) tree).count;
	}

	/**
	 * calculate the number of elements of a tree.
	 */
	static int _count(Object tree) {
		return count(tree).intValue();
	}

	static boolean isLeaf(Object tree) {
		return ((tree == null) || (tree.getClass() != BinNode.class));
	}

	/**
	 * combines two trees, if unbalanced throws an exception The value of the
	 * tree will be the value of the leftmost non-empty subtree
	 */
	static Object combine(Object left, Object right) {
		Object value = (left != emptyTree) ? value(left) : value(right);
		return new BinNode(left, right, value);
	}

	/**
	 * combines two homomorphic images to a new image
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	static Object combineImage(Object left, Object right, MonoidMap hom) {
		return new BinNode(left, right, hom.add(value(left), value(right)));
	}

	/**
	 * Inserts a new element into a sorted avl tree. A flag determines if copies
	 * are allowed. This method uses the machine stack for recursion.
	 */
	@SuppressWarnings("unchecked")
	static <E> Object insert(Object root, Comparator<? super E> cmp, E e,
			int behavior) {
		if (root == emptyTree)
			return e;
		else if (isLeaf(root)) {
			int c = cmp.compare(e, (E) value(root));
			if (c == 0) {
				return behavior == setBehavior ? e : combine(e, root);
			} else if (c < 0)
				return combine(e, root);
			else
				return combine(root, e);
		} else {
			int c = cmp.compare(e, (E) value(right(root)));
			if (c < 0)
				return balance(insert(left(root), cmp, e, behavior),
						right(root));
			else
				return balance(left(root),
						insert(right(root), cmp, e, behavior));
		}
	}

	/**
	 * TODO: explicit stack for height > maxRecursion Replaces an element at a
	 * given position. All boundary checks must be performed before the call.
	 */
	static <E> Object replace(Object root, Int pos, E e) {
		if (root == emptyTree)
			return e;
		else if (isLeaf(root)) {
			return e;
		} else {
			Int leftCount = count(left(root));
			int cmp = pos.compareTo(leftCount);
			if (cmp < 0)
				return combine(replace(left(root), pos, e), right(root));
			else
				return combine(left(root),
						replace(right(root), pos.subtract(leftCount), e));
		}
	}

	/**
	 * TODO: explicit stack for height > maxRecursion Replaces an element at a
	 * given position. All boundary checks must be performed before the call.
	 */
	static <E> Object insertAt(Object root, Int pos, E e) {
		if (root == emptyTree)
			return e;
		else if (isLeaf(root)) {
			if (pos.signum() <= 0)
				return combine(e, root);
			else
				return combine(root, e);
		} else {
			Int leftCount = count(left(root));
			int cmp = pos.compareTo(leftCount);
			if (cmp < 0)
				return balance(insertAt(left(root), pos, e), right(root));
			else
				return balance(left(root),
						insertAt(right(root), pos.subtract(leftCount), e));
		}
	}

	static Object balance(Object left, Object right) {
		if (left == emptyTree)
			return right;

		if (right == emptyTree)
			return left;

		int heightLeft = height(left);
		int heightRight = height(right);
		int diff = heightRight - heightLeft;
		if (diff >= -1 && diff <= 1)
			return combine(left, right);
		else if (diff < -1) {
			Object leftLeft = left(left);
			Object leftRight = right(left);
			if (height(leftLeft) - height(leftRight) < 0)
				return combine(combine(leftLeft, left(leftRight)),
						combine(right(leftRight), right));
			else
				return combine(leftLeft, combine(leftRight, right));
		} else {
			Object rightLeft = left(right);
			Object rightRight = right(right);
			if (height(rightRight) - height(rightLeft) < 0)
				return combine(combine(left, left(rightLeft)),
						combine(right(rightLeft), rightRight));
			else
				return combine(combine(left, rightLeft), rightRight);
		}
	}

	/**
	 * Inserts a new element into a sorted avl tree and updates an array of
	 * homomorphic images. A flag determines if copies are allowed. This method
	 * uses the machine stack for recursion
	 */
	@SuppressWarnings("rawtypes")
	static <E> Object insert(Object root, Object[] images, MonoidMap[] maps,
			Comparator<? super E> cmp, E e, int behavior) {
		return null;
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
			str.append(value(root));
			str.append(' ');
			buildString(right(root), str);
			str.append(')');
		}
		return;
	}

	@SuppressWarnings("rawtypes")
	static boolean isImage(Object root, Object image, MonoidMap hom) {
		return false;
	}

	final public static int allowCopies = 0;

	final public static int setBehavior = 1;

	/**
	 * combines two trees, if unbalanced throws an exception. This method also
	 * calculates an array of homomorphic images.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	static void combine(MonoidMap[] hom, Object[] hLeft, Object[] hRight,
			Object[] hResult) {
		if (hResult != null && hResult.length > 0) {
			for (int i = 0; i < hResult.length; i++) {
				hResult[i] = new BinNode(hLeft[i], hRight[i], hom[i].add(
						value(hLeft[i]), value(hRight[i])));
			}
		}
	}

	static Object first(Object tree) {
		if (tree == emptyTree)
			throw new IllegalArgumentException(
					"Cannot take first argument of empty tree.");
		return value(tree);
	}

	@SuppressWarnings("unchecked")
	static <E> Object findFirst(Object root, Comparator<? super E> cmp, E e) {

		if (root == emptyTree)
			return noElementFound;
		int c = cmp.compare(e, (E) value(root));
		if (c == 0)
			return value(root);
		if (c < 0)
			return noElementFound;

		Object found = noElementFound;

		while (!isLeaf(root)) {
			Object right = right(root);
			c = cmp.compare(e, (E) value(right));
			if (c == 0)
				found = value(right);
			root = (c <= 0) ? left(root) : right(root);
		}
		return cmp.compare(e, (E) value(root)) == 0 ? value(root) : found;
	}

	static Object last(Object tree) {
		if (tree == emptyTree)
			throw new IllegalArgumentException(
					"Cannot take first vargument of empty tree.");
		if (isLeaf(tree))
			return tree;

		BinNode node = (BinNode) tree;
		if (node.right != emptyTree)
			return last(node.right);
		else
			return last(node.left);
	}

	static final class MyIterator<E> implements ForwardIterator<E> {

		final ExtendibleArray<Object> treeRest = new ExtendibleArray<Object>();

		public MyIterator(Object tree) {
			gotoFirst(tree);
		}

		public MyIterator(Object tree, Int pos) {
			Int originalPos = pos;

			while (tree != emptyTree) {
				if (pos.equals(Int.ZERO)) {
					gotoFirst(tree);
					return;
				} else if (isLeaf(tree)) {
					tree = emptyTree;
				} else {
					Object left = left(tree);
					Object right = right(tree);
					Int posInRightSubtree = pos.subtract(count(left));
					if (posInRightSubtree.compareTo(Int.ZERO) >= 0) {
						pos = posInRightSubtree;
						tree = right;
					} else {
						treeRest.push(right);
						tree = left;
					}
				}
			}
			throw new IndexOutOfBoundsException("position " + originalPos);
		}

		private void gotoFirst(Object tree) {
			if (tree == emptyTree)
				return;
			while (!isLeaf(tree)) {
				treeRest.push(right(tree));
				tree = left(tree);
			}
			treeRest.push(tree);
		}

		public boolean hasNext() {
			return treeRest.size() > 0;
		}

		@SuppressWarnings("unchecked")
		public E next() {
			E result = (E) treeRest.pop();
			if (hasNext()) {
				Object nextSubtree = treeRest.pop();
				gotoFirst(nextSubtree);
			}
			return result;
		}
	}

	static Object get(Object tree, Int pos) {

		Int originalPos = pos;

		while (tree != emptyTree) {
			if (pos.equals(Int.ZERO))
				return value(tree);

			if (isLeaf(tree))
				tree = emptyTree;
			else {
				Object left = left(tree);
				Int posInRightSubtree = pos.subtract(count(left));
				if (posInRightSubtree.compareTo(Int.ZERO) >= 0) {
					pos = posInRightSubtree;
					tree = right(tree);
				} else
					tree = left;
			}
		}
		throw new IndexOutOfBoundsException("position " + originalPos);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	static Int firstOf(Object root, Function condition,
			MutableHashSet<Object> nonMatchingTrees) {
		if (root == emptyTree)
			return Int.MINUS_ONE;
		if (isLeaf(root))
			return (Boolean) condition.get(root) ? Int.ZERO : Int.MINUS_ONE;
		if (nonMatchingTrees.contains(root))
			return Int.MINUS_ONE;
		Int leftIndex = firstOf(left(root), condition, nonMatchingTrees);
		if (!leftIndex.equals(Int.MINUS_ONE))
			return leftIndex;
		Int rightIndex = firstOf(right(root), condition, nonMatchingTrees);
		if (!rightIndex.equals(Int.MINUS_ONE))
			return count(left(root)).add(rightIndex);
		else {
			/**
			 * only trees with a certain height will be cached for further
			 * searches
			 */
			if (height(root) > 3)
				nonMatchingTrees.add(root);
			return Int.MINUS_ONE;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	static Int lastOf(Object root, Function condition,
			MutableHashSet<Object> nonMatchingTrees) {
		if (root == emptyTree)
			return Int.MINUS_ONE;
		if (isLeaf(root))
			return (Boolean) condition.get(root) ? Int.ZERO : Int.MINUS_ONE;
		if (nonMatchingTrees.contains(root))
			return Int.MINUS_ONE;
		Int rightIndex = lastOf(right(root), condition, nonMatchingTrees);
		if (!rightIndex.equals(Int.MINUS_ONE))
			return count(left(root)).add(rightIndex);
		Int leftIndex = lastOf(left(root), condition, nonMatchingTrees);
		if (!leftIndex.equals(Int.MINUS_ONE))
			return leftIndex;
		else {
			/**
			 * only trees with a certain height will be cached for further
			 * searches
			 */
			if (height(root) > 3)
				nonMatchingTrees.add(root);
			return Int.MINUS_ONE;
		}
	}

	@SuppressWarnings("unchecked")
	static <E> Object remove(Object root, Comparator<? super E> cmp, E e) {
		if (root == emptyTree)
			return emptyTree;
		else if (isLeaf(root)) {
			return cmp.compare(e, (E) value(root)) == 0 ? emptyTree : root;
		} else {
			int c = cmp.compare(e, (E) value(right(root)));
			if (c < 0)
				return balance(remove(left(root), cmp, e), right(root));
			else
				return balance(left(root), remove(right(root), cmp, e));
		}
	}

	@SuppressWarnings("unchecked")
	static <E> Object computeImageTree(Object root, MonoidMap<E, ?> map) {
		if (root == emptyTree)
			return map.zero();

		ExtendibleArray<Object> tasks = new ExtendibleArray<Object>();
		MutableHashMap<Object, Object> nodeToImage = new MutableHashMap<Object, Object>();
		tasks.push(root);
		while (tasks.size() > 0) {
			Object actual = tasks.top();
			if (isLeaf(actual)) {
				nodeToImage.put(actual, map.map((E) actual));
				tasks.pop();
			} else if (!nodeToImage.containsKey(left(actual))) {
				tasks.push(left(actual));
			} else if (!nodeToImage.containsKey(right(actual))) {
				tasks.push(right(actual));
			} else {
				Object left = nodeToImage.get(left(actual));
				Object right = nodeToImage.get(right(actual));
				nodeToImage.put(actual, combineImage(left, right, map));
				tasks.pop();
			}
		}
		return nodeToImage.get(root);
	}

	static Object[] emptyTreeImages(MonoidMap<?, ?>[] maps) {
		Object[] newImages = null;
		if (maps != null) {
			int n = maps.length;
			newImages = new Object[n];
			for (int i = 0; i < n; i++) {
				newImages[i] = maps[i].zero();
			}
		}
		return newImages;
	}

	// TODO: version for images, maxRecDepth, explicit stack
	@SuppressWarnings("rawtypes")
	static Object concat(Object t1, Object t2, Object[] images1,
			Object[] images2, MonoidMap[] maps, Object[] resultImages) {
		if (t2 == emptyTree)
			return t1;
		if (t1 == emptyTree)
			return t2;
		int h1 = height(t1);
		int h2 = height(t2);
		if (Math.abs(h1 - h2) <= 1)
			return combine(t1, t2);
		else if (h1 > h2)
			return balance(left(t1),
					concat(right(t1), t2, null, null, null, null));
		else
			return balance(concat(t1, left(t2), null, null, null, null),
					right(t2));
	}

	// TODO: version for images, maxRecDepth, explicit stack
	static Object subseq(Object tree, Int start, Int end) {

		if ((start.compareTo(Int.ZERO) <= 0)
				&& (end.compareTo(count(tree)) >= 0))
			return tree;

		if (isLeaf(tree)) {
			return emptyTree;
		}
		Object left = left(tree);
		Int leftSize = count(left);
		boolean isInLeft = (start.compareTo(leftSize.subtract(Int.ONE)) <= 0);
		boolean isInRight = (end.compareTo(leftSize) >= 0);
		if (isInLeft && isInRight)
			return concat(
					subseq(left(tree), start, end),
					subseq(right(tree), start.subtract(leftSize),
							end.subtract(leftSize)), null, null, null, null);
		else if (isInLeft)
			return subseq(left(tree), start, end);
		else if (isInRight)
			return subseq(right(tree), start.subtract(leftSize),
					end.subtract(leftSize));
		else
			return emptyTree;
	}

	static <E> ImmutableHashMap<MonoidMap<E, ?>, Object> emptyImages(
			ImmutableSet<MonoidMap<E, ?>> maps) {
		ImmutableHashMap<MonoidMap<E, ?>, Object> images = ImmutableHashMap
				.create();
		ForwardIterator<MonoidMap<E, ?>> it = maps.iterator();
		while (it.hasNext()) {
			MonoidMap<E, ?> map = it.next();
			images = images.put(map, map.zero());
		}
		return images;
	}
}
