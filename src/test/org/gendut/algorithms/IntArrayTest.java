package org.gendut.algorithms;

import junit.framework.TestCase;

import org.gendut.algorithm.IntArray;

/*<literate>*/
public class IntArrayTest extends TestCase {
    /*
     * Testcases for integer array routines.
     */
    public void testMerge() {
        int[][] data = { {}, {},
                {}, // 1
                {}, { 2, 3 },
                { 2, 3 }, // 2
                { 1, 2, 3, 3, 7 }, { 0, 2, 4, 4, 8, 9 },
                { 0, 1, 2, 2, 3, 3, 4, 4, 7, 8, 9 }, // 3
                { 0, 2, 3, 3, 7 }, { 0, 0, 1, 2, 4, 4, 8, 9, 9, 9, 9 },
                { 0, 0, 0, 1, 2, 2, 3, 3, 4, 4, 7, 8, 9, 9, 9, 9 } // 4
        };

        for (int i = 0; i < data.length; i += 3) {
            int[] a = data[i];
            int[] b = data[i + 1];
            int[] c = IntArray.mergeSortedArrays(a, b);
            String expected = IntArray.toString(data[i + 2]);
            assertEquals(expected, IntArray.toString(c));

            // `Test with a, b swapped:`

            c = IntArray.mergeSortedArrays(b, a);
            expected = IntArray.toString(data[i + 2]);
            assertEquals(expected, IntArray.toString(c));
        }
    }

    public void testMinus() {
        int[][] data = { {}, { 1, 2 },
                {}, // 1
                { 2, 3 }, { 2, 3 },
                {}, // 2
                { 1, 2, 3, 3, 7 }, { 0, 2 },
                { 1, 3, 3, 7 }, // 3
                { 1, 2, 3, 3, 7 }, { 0, 3 },
                { 1, 2, 7 }, // 3
                { 0, 2, 3, 3, 7 }, { 0, 0, 1, 2, 4, 4, 8, 9, 9, 9, 9 },
                { 3, 3, 7 } // 4
        };

        for (int i = 0; i < data.length; i += 3) {
            int[] a = data[i];
            int[] b = data[i + 1];
            int[] c = IntArray.minusSortedArrays(a, b);
            String expected = IntArray.toString(data[i + 2]);
            assertEquals(expected, IntArray.toString(c));
        }
    }

    public void testUnify() {
        int[][] data = { {}, {}, // 1
                { 2, 3 }, { 2, 3 }, // 2
                { 1, 2, 3, 3, 7 }, { 1, 2, 3, 7 }, // 3
                { 0, 1, 2, 2, 3, 3, 4, 4, 7, 8, 9 }, { 0, 1, 2, 3, 4, 7, 8, 9 }, // 4
                { 0, 0, 0, 1, 2, 2, 3, 3, 4, 4, 7, 8, 9, 9, 9, 9 }, { 0, 1, 2, 3, 4, 7, 8, 9 } // 5
        };

        for (int i = 0; i < data.length; i += 2) {
            int[] a = data[i];
            int[] b = IntArray.unifySortedArray(a);
            String expected = IntArray.toString(data[i + 1]);
            assertEquals(expected, IntArray.toString(b));
        }
    }

    public void testDifference() {
        int[][] data = { {}, {}, {}, // 1
                { 2, 3 }, { 3, 4 }, { 2, 4 }, // 2
                { 1, 2, 3, 3, 7 }, { 1, 2, 3, 7 }, { 3 }, // 3
                { 0, 1, 2, 5 }, { 0, 2, 3, 7, 8, 9 }, { 1, 3, 5, 7, 8, 9 } // 4
        };

        for (int i = 0; i < data.length; i += 3) {
            int[] a = data[i];
            int[] b = data[i + 1];
            int[] c = IntArray.differenceForSortedArrays(a, b);
            String expected = IntArray.toString(data[i + 2]);
            assertEquals(expected, IntArray.toString(c));
        }
    }

    public void testContainmentTrue() {
        int[][] data = { {}, {}, // 1
                { 2, 3 }, { 2, 3 }, // 2
                { 1, 2, 3, 7 }, { 1, 2, 3, 7, 8 }, // 3
                { 1, 2, 7, 8 }, { 0, 1, 2, 4, 7, 8, 9 }, // 4
                { 0, 4, 9 }, { 0, 1, 2, 3, 4, 7, 8, 9 } // 5
        };

        for (int i = 0; i < data.length; i += 2) {
            int[] a = data[i];
            int[] b = data[i + 1];
            assertTrue(IntArray.isContainedForSortedArrays(a, b));
        }
    }

    public void testContainmentFalse() {
        int[][] data = { { 1 }, {}, // 1
                { 2, 3 }, { 2, 4 }, // 2
                { 1, 2, 3, 7 }, { 0, 2, 3, 7, 8 }, // 3
                { 1, 2, 8, 10 }, { 0, 1, 2, 4, 7, 8, 9 }, // 4
                { 0, 4, 9 }, { 0, 1, 2, 3, 7, 8, 9 }, // 5
                { 0, 1, 2, 3, 4, 7, 8, 9 }, { 0, 1, 2, 3, 4, 8, 9 } // 6
        };

        for (int i = 0; i < data.length; i += 2) {
            int[] a = data[i];
            int[] b = data[i + 1];
            assertFalse(IntArray.isContainedForSortedArrays(a, b));
        }
    }
}// `class`
