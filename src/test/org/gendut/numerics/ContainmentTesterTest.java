package org.gendut.numerics;

import junit.framework.TestCase;

public class ContainmentTesterTest extends TestCase {

    public void testRectangle() {
        Matrix rectangle = new Matrix();
        rectangle = rectangle.appendRow(0.0, 0.0);
        rectangle = rectangle.appendRow(0.0, 1.0);
        rectangle = rectangle.appendRow(1.0, 1.0);
        rectangle = rectangle.appendRow(1.0, 0.0);

        ContainmentTester ct = new ContainmentTester(rectangle, 0.001);

        for (int k = -10; k < 10; k++)
            for (int i = -10; i < 10; i++) {
                double x = i / 10.0;
                double y = k / 10.0;
                Vector p = new Vector(x, y);
                Boolean inside = ((0 <= x) && (x <= 1.0) && (0 <= y) && (y <= 1.0));
                assertEquals(inside, ct.get(p));
            }
        assertTrue(ct.get(new Vector(-0.0001, -0.0001)));
        assertTrue(ct.get(new Vector(-0.0001, 0.5)));
    }

    static long N = 400;
    int M = 10;

    public void testNGon() {
        Matrix rectangle = new Matrix();
        for (int i = 0; i < N; i++) {
            double a = 2 * Math.PI * i / N;
            rectangle = rectangle.appendRow(Math.sin(a), Math.cos(a));
        }
        ContainmentTester ct1 = new ContainmentTester(rectangle, 0.001);
        LargePolygonContainmentTester ct2 = new LargePolygonContainmentTester(rectangle, 0.001);

        for (int m = 0; m < M; m++) {
            for (int k = -11; k < 11; k++) {
                for (int i = -11; i < 11; i++) {
                    double x = i / 10.0;
                    double y = k / 10.0;
                    Vector p = new Vector(x, y);
                    assertEquals(ct1.get(p), ct2.get(p));
                }
            }
        }
    }

    public void testNGonTiming() {
        Matrix rectangle = new Matrix();
        for (int i = 0; i < N; i++) {
            double a = 2 * Math.PI * i / N;
            rectangle = rectangle.appendRow(Math.sin(a), Math.cos(a));
        }
        LargePolygonContainmentTester ct = new LargePolygonContainmentTester(rectangle, 0.001);

        for (int m = 0; m < M; m++) {
            for (int k = -11; k < 11; k++) {
                for (int i = -11; i < 11; i++) {
                    double x = i / 10.0;
                    double y = k / 10.0;
                    Vector p = new Vector(x, y);
                    ct.get(p);
                }
            }
        }
    }
}
