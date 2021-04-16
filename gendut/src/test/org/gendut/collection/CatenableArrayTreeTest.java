package org.gendut.collection;


import java.util.Comparator;

import org.gendut.arithmetic.Int;
import org.gendut.seq.Seq;
import org.gendut.seq.SeqFromIterator;

import junit.framework.TestCase;

public class CatenableArrayTreeTest extends TestCase {

  static private Object T(Object left, Object right) {
    return CatenableArrayTree.combine(left, right);
  }

  Comparator<Integer> cmp = new Comparator<Integer>() {
    public int compare(Integer a, Integer b) {
      return a.compareTo(b);
    }
  };

  public void testNavigation() {
    assertEquals(CatenableArrayTree.noElementFound,
        CatenableArrayTree.findFirst(CatenableArrayTree.emptyTree, cmp, 1));

    Object tree = T(T(0, 1), (T(1, 2)));
    assertEquals(2, CatenableArrayTree.last(tree));
    assertEquals(0, CatenableArrayTree.findFirst(tree, cmp, 0));
    assertEquals(CatenableArrayTree.noElementFound,
        CatenableArrayTree.findFirst(tree, cmp, 111));
    assertEquals(1, CatenableArrayTree.findFirst(tree, cmp, 1));

    tree = T(T(4, 4), (T(4, 5)));
    assertEquals(CatenableArrayTree.noElementFound,
        CatenableArrayTree.findFirst(tree, cmp, 0));
    assertEquals(4, CatenableArrayTree.findFirst(tree, cmp, 4));
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public void testSequence() {

    Object tree = T(T(0, 1), (T(T(1, 1), T(2, 3))));

    Seq<Integer> seq = SeqFromIterator
        .create(new CatenableArrayTree.MyIterator(tree));

    assertEquals((Integer) 0, seq.first());
    assertEquals((Integer) 1, seq.rest().first());
    assertEquals((Integer) 1, seq.rest().rest().first());
    assertEquals((Integer) 1, seq.rest().rest().rest().first());
    assertEquals((Integer) 2, seq.rest().rest().rest().rest().first());
    assertEquals((Integer) 3, seq.rest().rest().rest().rest().rest().first());
    assertTrue(seq.rest().rest().rest().rest().rest().rest().isEmpty());
  }

  public void testGetForPosition() {

    Object tree = T(T(0, T(0, 1)), (T(T(1, 2), T(2, 3))));

    LargeArray<Integer> arr = new LargeArray<Integer>(tree);

    assertEquals((Integer) 0, arr.get(0));
    assertEquals((Integer) 0, arr.get(1));
    assertEquals((Integer) 1, arr.get(2));
    assertEquals((Integer) 1, arr.get(3));
    assertEquals((Integer) 2, arr.get(4));
    assertEquals((Integer) 2, arr.get(5));
    assertEquals((Integer) 3, arr.get(6));

    try {
      arr.get(7);
      fail("IndexOutOfBoundsException expected.");
    } catch (IndexOutOfBoundsException e) {
    }
    ;
  }

  public void testIteratorForPosition() {

    Object tree = T(T(0, T(1, 1)), (T(T(1, 2), T(2, 3))));

    LargeArray<Integer> arr = new LargeArray<Integer>(tree);

    assertEquals((Integer) 0, arr.iterator(0).next());
    assertEquals((Integer) 1, arr.iterator(1).next());
    assertEquals((Integer) 1, arr.iterator(2).next());
    assertEquals((Integer) 1, arr.iterator(3).next());
    assertEquals((Integer) 2, arr.iterator(4).next());
    assertEquals((Integer) 2, arr.iterator(5).next());
    assertEquals((Integer) 3, arr.iterator(6).next());

    try {
      arr.iterator(7);
      fail("IndexOutOfBoundsException expected.");
    } catch (IndexOutOfBoundsException e) {
    }
    ;

    Seq<Integer> seq = SeqFromIterator.create(arr.iterator(0));

    assertEquals((Integer) 0, seq.first());
    assertEquals((Integer) 1, seq.rest().first());
    assertEquals((Integer) 1, seq.rest().rest().first());
    assertEquals((Integer) 1, seq.rest().rest().rest().first());
    assertEquals((Integer) 2, seq.rest().rest().rest().rest().first());
    assertEquals((Integer) 2, seq.rest().rest().rest().rest().rest().first());
    assertEquals((Integer) 3, seq.rest().rest().rest().rest().rest().rest()
        .first());
    assertTrue(seq.rest().rest().rest().rest().rest().rest().rest().isEmpty());

    seq = SeqFromIterator.create(arr.iterator(1));

    assertEquals((Integer) 1, seq.first());
    assertEquals((Integer) 1, seq.rest().first());
    assertEquals((Integer) 1, seq.rest().rest().first());
    assertEquals((Integer) 2, seq.rest().rest().rest().first());
    assertEquals((Integer) 2, seq.rest().rest().rest().rest().first());
    assertEquals((Integer) 3, seq.rest().rest().rest().rest().rest().first());
    assertTrue(seq.rest().rest().rest().rest().rest().rest().isEmpty());

    seq = SeqFromIterator.create(arr.iterator(3));

    assertEquals((Integer) 1, seq.first());
    assertEquals((Integer) 2, seq.rest().first());
    assertEquals((Integer) 2, seq.rest().rest().first());
    assertEquals((Integer) 3, seq.rest().rest().rest().first());
    assertTrue(seq.rest().rest().rest().rest().isEmpty());

  }

  public void testFindFirst() {
    Object tree = T(T(T(0, 1), 2), T(3, T(5, 6)));
    assertEquals(0, CatenableArrayTree.findFirst(tree, cmp, 0));
    assertEquals(1, CatenableArrayTree.findFirst(tree, cmp, 1));
    assertEquals(2, CatenableArrayTree.findFirst(tree, cmp, 2));
    assertEquals(3, CatenableArrayTree.findFirst(tree, cmp, 3));
    assertEquals(CatenableArrayTree.noElementFound,
        CatenableArrayTree.findFirst(tree, cmp, 4));
    assertEquals(5, CatenableArrayTree.findFirst(tree, cmp, 5));
    assertEquals(6, CatenableArrayTree.findFirst(tree, cmp, 6));
    assertEquals(CatenableArrayTree.noElementFound,
        CatenableArrayTree.findFirst(tree, cmp, -1));
    assertEquals(CatenableArrayTree.noElementFound,
        CatenableArrayTree.findFirst(tree, cmp, 8));
  }

  public void testBalance() {
    Object left = T(0, 1);
    Object right = T(T(1, 2), 3);
    Object tree = CatenableArrayTree.balance(left, right);
    assertEquals("((0 0 1) 0 ((1 1 2) 1 3))", tree.toString());

    left = 0;
    right = T(1, 2);
    tree = CatenableArrayTree.balance(left, right);
    assertEquals("(0 0 (1 1 2))", tree.toString());

    left = 2;
    right = 0;
    tree = CatenableArrayTree.balance(left, right);
    assertEquals("(2 2 0)", tree.toString());

    left = 0;
    right = T(1, T(2, 3));
    tree = CatenableArrayTree.balance(left, right);
    assertEquals("((0 0 1) 0 (2 2 3))", tree.toString());

    left = 0;
    right = T(T(1, 2), 3);
    tree = CatenableArrayTree.balance(left, right);
    assertEquals("((0 0 1) 0 (2 2 3))", tree.toString());

    left = T(0, T(1, 2));
    right = 3;
    tree = CatenableArrayTree.balance(left, right);
    assertEquals("((0 0 1) 0 (2 2 3))", tree.toString());

    left = T(T(1, 2), 3);
    right = 4;
    tree = CatenableArrayTree.balance(left, right);
    assertEquals("((1 1 2) 1 (3 3 4))", tree.toString());

    left = T(T(0, 1), T(2, 3));
    right = 4;
    tree = CatenableArrayTree.balance(left, right);
    assertEquals("((0 0 1) 0 ((2 2 3) 2 4))", tree.toString());

    left = 0;
    right = T(T(1, 2), T(3, 4));
    tree = CatenableArrayTree.balance(left, right);
    assertEquals("((0 0 (1 1 2)) 0 (3 3 4))", tree.toString());
  }

  public void testRemove() {
    Object tree = T(T(T(0, 1), 2), T(3, T(5, 6)));
    assertEquals("(((0 0 1) 0 2) 0 (3 3 (5 5 6)))",
        CatenableArrayTree.remove(tree, cmp, 4).toString());
    assertEquals("(((0 0 1) 0 2) 0 (3 3 (5 5 6)))",
        CatenableArrayTree.remove(tree, cmp, -1).toString());
    assertEquals("(((0 0 1) 0 2) 0 (3 3 (5 5 6)))",
        CatenableArrayTree.remove(tree, cmp, 10).toString());
    assertEquals("((1 1 2) 1 (3 3 (5 5 6)))",
        CatenableArrayTree.remove(tree, cmp, 0).toString());
    assertEquals("((0 0 2) 0 (3 3 (5 5 6)))",
        CatenableArrayTree.remove(tree, cmp, 1).toString());
    assertEquals("((0 0 1) 0 (3 3 (5 5 6)))",
        CatenableArrayTree.remove(tree, cmp, 2).toString());
    assertEquals("(((0 0 1) 0 2) 0 (5 5 6))",
        CatenableArrayTree.remove(tree, cmp, 3).toString());
    assertEquals("(((0 0 1) 0 2) 0 (3 3 6))",
        CatenableArrayTree.remove(tree, cmp, 5).toString());
    assertEquals("(((0 0 1) 0 2) 0 (3 3 5))",
        CatenableArrayTree.remove(tree, cmp, 6).toString());
  }

  public void testReplace() {
    Object tree = T(T(T(0, 1), 2), T(3, T(5, 6)));
    assertEquals("(((0 0 1) 0 2) 0 (3 3 (5 5 6)))", tree.toString());
    assertEquals("(((99 99 1) 99 2) 99 (3 3 (5 5 6)))", CatenableArrayTree
        .replace(tree, Int.create(0), 99).toString());
    assertEquals("(((0 0 99) 0 2) 0 (3 3 (5 5 6)))", CatenableArrayTree
        .replace(tree, Int.create(1), 99).toString());
    assertEquals("(((0 0 1) 0 99) 0 (3 3 (5 5 6)))", CatenableArrayTree
        .replace(tree, Int.create(2), 99).toString());
    assertEquals("(((0 0 1) 0 2) 0 (99 99 (5 5 6)))", CatenableArrayTree
        .replace(tree, Int.create(3), 99).toString());
    assertEquals("(((0 0 1) 0 2) 0 (3 3 (99 99 6)))", CatenableArrayTree
        .replace(tree, Int.create(4), 99).toString());
    assertEquals("(((0 0 1) 0 2) 0 (3 3 (5 5 99)))", CatenableArrayTree
        .replace(tree, Int.create(5), 99).toString());
  }

  public void testComputeImage() {
    Object tree = T(T(T(0, 1), 2), T(3, T(5, 6)));
    Object image = CatenableArrayTree.value(CatenableArrayTree
        .computeImageTree(tree, MonoidMaps.sameTree));
    assertEquals(tree.toString(), image.toString());
    image = CatenableArrayTree.value(CatenableArrayTree.computeImageTree(tree,
        MonoidMaps.sumOfInteger));
    assertEquals("17", image.toString());

    /*
     * The following test ensures that computing the image works with trees of
     * exponential size, i.e. the computation must not take forever in order to
     * complete.
     */
    for (int i = 0; i < 24; i++) {
      tree = CatenableArrayTree.combine(tree, tree);
    }
    assertEquals(6 * 1024 * 1024 * 16, CatenableArrayTree.count(tree)
        .longValue());
    image = CatenableArrayTree.value(CatenableArrayTree.computeImageTree(tree,
        MonoidMaps.sumOfInteger));
    assertEquals(Integer.valueOf(17 * 1024 * 1024 * 16), image);
  }

  public void testConcat() {
    Object tree1 = T(T(T(0, 1), 2), T(3, T(5, 6)));
    Object tree2 = T(8, 9);
    assertEquals("(((0 0 1) 0 2) 0 ((3 3 (5 5 6)) 3 (8 8 9)))",
        CatenableArrayTree.concat(tree1, tree2, null, null, null, null).toString());
    assertEquals("(((8 8 9) 8 ((0 0 1) 0 2)) 8 (3 3 (5 5 6)))",
        CatenableArrayTree.concat(tree2, tree1, null, null, null, null).toString());

    Object veryLargeTree = "A";
    Int veryLargeTreeSize = Int.create(1);
    for (int i = 0; i < 300; i++) {
      veryLargeTree = CatenableArrayTree.concat(veryLargeTree, veryLargeTree, null, null, null, null);
      veryLargeTreeSize = veryLargeTreeSize.multiply(2);
      assertEquals(veryLargeTreeSize, CatenableArrayTree.count(veryLargeTree));
    }
    veryLargeTree = CatenableArrayTree.concat(tree2, veryLargeTree, null, null, null, null);
    veryLargeTreeSize = veryLargeTreeSize.add(2);
    assertEquals(veryLargeTreeSize, CatenableArrayTree.count(veryLargeTree));
  }

  public void testSubseq() {
    Object tree = T(T(T(0, 1), T(2, 3)), T(T(4, 5), T(6, 7)));
    Object subseq = CatenableArrayTree.subseq(tree, Int.create(2),
        Int.create(7));
    assertEquals(Int.create(5), CatenableArrayTree.count(subseq));
    assertEquals("((2 2 3) 2 ((4 4 5) 4 6))", subseq.toString());

    subseq = CatenableArrayTree.subseq(tree, Int.create(2), Int.create(9));
    assertEquals(Int.create(6), CatenableArrayTree.count(subseq));
    assertEquals("((2 2 3) 2 ((4 4 5) 4 (6 6 7)))", subseq.toString());

    subseq = CatenableArrayTree.subseq(tree, Int.create(-1), Int.create(9));
    assertEquals(Int.create(8), CatenableArrayTree.count(subseq));
    assertEquals(tree.toString(), subseq.toString());

    subseq = CatenableArrayTree.subseq(tree, Int.create(100), Int.create(109));
    assertTrue(subseq == CatenableArrayTree.emptyTree);

    subseq = CatenableArrayTree.subseq(tree, Int.create(-10), Int.create(1));
    assertEquals("0", subseq.toString());

    subseq = CatenableArrayTree.subseq(tree, Int.create(-10), Int.create(0));
    assertTrue(subseq == CatenableArrayTree.emptyTree);

    subseq = CatenableArrayTree.subseq(CatenableArrayTree.emptyTree,
        Int.create(-1), Int.create(9));
    assertTrue(subseq == CatenableArrayTree.emptyTree);
  }
}
