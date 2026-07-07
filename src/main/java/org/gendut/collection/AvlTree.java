package org.gendut.collection;


import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static java.math.BigInteger.valueOf;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.Objects;
import java.util.Stack;

final class AvlTree {

	private final static class BinNode {
		public final Object left, right;
		public final int height;
		public final int hash;
		public final BigInteger count;

		private BinNode() {
			left = right = null;
			height = 0;
			hash = 0;
			count = BigInteger.ZERO;
		}

		public BinNode(Object left, Object right) {
			if (left == emptyTree)
				throw new IllegalArgumentException("left tree ist empty");
			if (right == emptyTree)
				throw new IllegalArgumentException("right tree ist empty");

			this.left = left;
			this.right = right;
			this.count = count(left).add(count(right));
			// TODO: we could implement a more robust hash via:
			// (hash(left)^count(right) % prime) + hash(right)) % prime
			// by using a fast modular exponentiation algorithm.
			this.hash = AvlTree.hashCode(left) + AvlTree.hashCode(right);
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

		@Override
		public String toString() {
			return AvlTree.toString(this);
		}

	}

	static Object left(Object tree) {
		return ((BinNode) tree).left;
	}

	static Object right(Object tree) {
		return ((BinNode) tree).right;
	}

	public static int hashCode(Object tree) {
		if (isLeaf(tree))
			return Math.abs(Objects.hashCode(tree));
		else
			return ((BinNode) tree).hash;
	}

	static final Object emptyTree = new BinNode();

	static BigInteger count(Object tree) {
		if ((tree == null) || (tree.getClass() != BinNode.class))
			return BigInteger.ONE;
		else
			return ((BinNode) tree).count;
	}

	static Object createLeaf(Object o) {
	//TODO	Assertions.asserts(o == null || o.getClass() != BinNode.class);
		return o;
	}

	static boolean isLeaf(Object tree) {
		return ((tree == null) || (tree.getClass() != BinNode.class));
	}

	static int height(Object tree) {
		if ((tree == null) || (tree.getClass() != BinNode.class))
			return 1;
		else
			return ((BinNode) tree).height;
	}

	static Object concat(Object t1, Object t2) {
		if (t2 == emptyTree)
			return t1;
		if (t1 == emptyTree)
			return t2;
		int h1 = height(t1);
		int h2 = height(t2);
		if (Math.abs(h1 - h2) <= 1)
			return combine(t1, t2);
		else if (h1 > h2)
			return balance(left(t1), concat(right(t1), t2));
		else
			return balance(concat(t1, left(t2)), right(t2));
	}

	static Object combine(Object left, Object right) {
		return new BinNode(left, right);
	}

	static <E> Object replaceAt(Object root, BigInteger pos, E e) {
		if (root == emptyTree)
			return e;
		else if (isLeaf(root)) {
			return e;
		} else {
			BigInteger leftCount = count(left(root));
			int cmp = pos.compareTo(leftCount);
			if (cmp < 0)
				return combine(replaceAt(left(root), pos, e), right(root));
			else
				return combine(left(root), replaceAt(right(root), pos.subtract(leftCount), e));
		}
	}

	static Object get(Object tree, BigInteger pos) {

		if (!(BigInteger.ZERO.compareTo(pos) <= 0) && (pos.compareTo(count(tree)) < 0)) {
			throw new IndexOutOfBoundsException("position " + pos);
		}

		while (!isLeaf(tree)) {
			Object left = left(tree);
			BigInteger posInRightSubtree = pos.subtract(count(left));
			if (posInRightSubtree.compareTo(BigInteger.ZERO) >= 0) {
				pos = posInRightSubtree;
				tree = right(tree);
			} else
				tree = left;
		}
		return tree;
	}

	static Object insertAt(Object root, BigInteger pos, Object e) {
		if (root == emptyTree)
			return e;
		else if (isLeaf(root)) {
			if (pos.signum() <= 0)
				return combine(e, root);
			else
				return combine(root, e);
		} else {
			BigInteger leftCount = count(left(root));
			int cmp = pos.compareTo(leftCount);
			if (cmp < 0)
				return balance(insertAt(left(root), pos, e), right(root));
			else
				return balance(left(root), insertAt(right(root), pos.subtract(leftCount), e));
		}
	}

	static Object removeAt(Object tree, BigInteger pos) {
		if (tree == emptyTree || isLeaf(tree))
			return emptyTree;
		else {
			int c = pos.compareTo(count(left(tree)));
			if (c < 0)
				return balance(removeAt(left(tree), pos), right(tree));
			else
				return balance(left(tree), removeAt(right(tree), pos.subtract(count(left(tree)))));
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
				return combine(combine(leftLeft, left(leftRight)), combine(right(leftRight), right));
			else
				return combine(leftLeft, combine(leftRight, right));
		} else {
			Object rightLeft = left(right);
			Object rightRight = right(right);
			if (height(rightRight) - height(rightLeft) < 0)
				return combine(combine(left, left(rightLeft)), combine(right(rightLeft), rightRight));
			else
				return combine(combine(left, rightLeft), rightRight);
		}
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
			buildString(right(root), str);
			str.append(')');
		}
		return;
	}

	static Object slice(Object tree, BigInteger start, BigInteger end) {

		if ((start.compareTo(BigInteger.ZERO) <= 0) && (end.compareTo(count(tree)) >= 0))
			return tree;

		if (isLeaf(tree)) {
			return emptyTree;
		}
		Object left = left(tree);
		BigInteger leftSize = count(left);
		boolean isInLeft = (start.compareTo(leftSize.subtract(BigInteger.ONE)) <= 0);
		boolean isInRight = (end.compareTo(leftSize) >= 0);
		if (isInLeft && isInRight)
			return concat(slice(left(tree), start, end),
					slice(right(tree), start.subtract(leftSize), end.subtract(leftSize)));
		else if (isInLeft)
			return slice(left(tree), start, end);
		else if (isInRight)
			return slice(right(tree), start.subtract(leftSize), end.subtract(leftSize));
		else
			return emptyTree;
	}

	static final class TreeIterator<E> implements Iterator<E> {

		final Stack<Object> treeRest = new Stack<Object>();

		public TreeIterator(Object tree) {
			gotoFirst(tree);
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

	static final class PreorderVisitor implements Iterator<Object> {

		public Stack<Object> treeRest = new Stack<Object>();
		public BigInteger leafCount = ZERO;

		public PreorderVisitor(Object tree) {
			treeRest.push(tree);
		}

		public boolean hasNext() {
			return treeRest.size() > 0;
		}

		public Object next() {
			Object tree = treeRest.pop();
			if (!isLeaf(tree)) {
				treeRest.push(right(tree));
				treeRest.push(left(tree));
			} else {
				leafCount.add(ONE);
			}

			return tree;
		}

		public Object curentNode() {
			return treeRest.lastElement();
		}

		public void skipCurrentNode() {
			Object tree = treeRest.pop();
			leafCount = leafCount.add(count(tree));
		}
	}

	public static boolean isEqual(Object t1, Object t2) {
		return count(t1).equals(count(t2)) && hashCode(t1) == hashCode(t2)
				&& firstDifference(t1, t2).equals(valueOf(-1));
	}

	static BigInteger firstDifference(Object t1, Object t2) {

		//TODOasserts(count(t1).equals(count(t2)));

		PreorderVisitor visitor1 = new PreorderVisitor(t1);
		PreorderVisitor visitor2 = new PreorderVisitor(t2);

		while (visitor1.hasNext()) {
			//TODOasserts(visitor2.hasNext());
			//TODOasserts(visitor1.leafCount.equals(visitor2.leafCount));
			Object node1 = visitor1.curentNode();
			Object node2 = visitor2.curentNode();

			if (node1 == node2) {
				visitor1.skipCurrentNode();
				visitor2.skipCurrentNode();
			} else {
				int cmp = count(node1).compareTo(count(node2));
				if (cmp > 0) {
					PreorderVisitor tmp = visitor1;
					visitor1 = visitor2;
					visitor2 = tmp;
				} else if (cmp < 0) {
					visitor2.next();
				} else {
					if (count(node2).equals(ONE)) {
						//TODOasserts(isLeaf(node1));
						//TODOasserts(isLeaf(node2));
						if (!node1.equals(node2))
							return visitor1.leafCount;
					}
					visitor1.next();
					visitor2.next();
				}
			}
		}
		//TODOasserts(!visitor2.hasNext());
		//TODOasserts(visitor1.leafCount.equals(visitor2.leafCount));
		//TODOasserts(visitor1.leafCount.equals(count(t2)));

		return valueOf(-1);
	}

}