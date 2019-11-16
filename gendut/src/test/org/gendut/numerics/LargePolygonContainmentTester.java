package org.gendut.numerics;

import org.gendut.algorithm.Comparator;
import org.gendut.collection.IntervalRecord;
import org.gendut.collection.IntervalSet;
import org.gendut.collection.MonoidMap;
import org.gendut.func.Function;


public class LargePolygonContainmentTester implements Function<Vector, Boolean>
{
    private final double[] px, py;

    private final double xmin, xmax, ymin, ymax;

    private final double e;

    private final IntervalSet<Double, Line> lines;

    static final Comparator<Double> lineXComp = new Comparator<Double>()
    {
        public int compare(Double a, Double b)
        {
            if (a < b)
                return -1;
            else if (a > b)
                return 1;
            else
            return 0;
        }
    };

    class Line implements IntervalRecord<Double, Line>
    {
        public final int start, end;

        public Line(int start, int end)
        {
            if (py[start] > py[end])
            {
                int tmp = start;
                start = end;
                end = tmp;
            }
            this.start = start;
            this.end = end;
        }

        public Double start()
        {
            return py[start];
        }

        public Double end()
        {
            return py[end];
        }

        public Comparator<? super Double> baseComparator()
        {
            return lineXComp;
        }

        public int compare(Line B)
        {
            Line other = (Line) B;
            if (py[start] < py[other.start])
                return -1;
            else if (py[start] > py[other.start])
                return 1;
            else if (py[end] < py[other.end])
                return -1;
            else if (py[end] > py[other.end])
                return 1;
            else if (px[start] < px[other.start])
                return -1;
            else if (px[start] > px[other.start])
                return 1;
            else if (px[end] < px[other.end])
                return -1;
            else if (px[end] > px[other.end])
                return 1;
            else return 0;
        }

        public int compareEndTo(Double p)
        {
            if (py[end] < p)
                return -1;
            else if (py[end] > p)
                return 1;
            else return 0;
        }

        public int compareEnds(Line B)
        {
            Line other = (Line) B;
            if (py[end] < py[other.end])
                return -1;
            else if (py[end] > py[other.end])
                return 1;
            else return 0;
        }

        public boolean contains(Double point)
        {
            return false;
        }

        public boolean contains(Line other)
        {
            return false;
        }

        public Line gap(Line B)
        {
            return null;
        }

        public Line intersect(Line B)
        {
            return null;
        }

        public boolean intersects(Line other)
        {
            return (py[end] > py[other.start] && py[other.end] > py[start]);
        }

        public boolean intersects(Double start, Double end)
        {
            return (py[this.end] > start && end > py[this.start]);
        }

        @Override
        public String toString()
        {
            return "[(" + px[start] + ',' + py[start] + ")(" + px[end] + ','
                            + py[end] + ")]";
        }
    }
    class CloseToBorder implements MonoidMap<Line, Boolean>
    {
        final double x, y;

        public CloseToBorder(double x, double y)
        {
            this.x = x;
            this.y = y;
        }

        public Boolean add(Boolean a, Boolean b)
        {
            return a || b;
        }

        public Boolean map(Line v)
        {
            return (withinDistance(x, y, px[v.start], py[v.start], px[v.end],
                            py[v.end], e));
        }

        public Boolean zero()
        {
            return false;
        }
    };

    private Function<Boolean, Boolean> alwaysFalse = new Function<Boolean, Boolean>()
    {
        public Boolean get(Boolean e)
        {
            return false;
        }
    };;

    class IntersectionAlternator implements MonoidMap<Line, Boolean>
    {
        final double x;

        final double y;

        public IntersectionAlternator(double x, double y)
        {
            this.x = x;
            this.y = y;
        }

        public Boolean add(Boolean x, Boolean y)
        {
            return x ^ y;
        }

        public Boolean map(Line a)
        {
            int i = a.start;
            int j = a.end;
            return (
            /*
             * At least some part of the line segment must be left from the vertical line at $x$.
             */
            (x <= px[i] || x <= px[j]) &&
                            /*
                             * The line segment must not be completely at either side of the
                             * horizontal line at $y$.
                             */
                            (((py[i] <= y) && (y < py[j])) || ((py[j] <= y) && (y < py[i]))) &&
            /*
             * The line segment intersects the horizontal line at $y$ on the left side from point
             * $(x,y)$.
             */
            (((x < px[i]) && x < px[j]) || (x < (px[j] - px[i]) * (y - py[i])
                            / (py[j] - py[i]) + px[i])));
        }

        public Boolean zero()
        {
            return true;
        }
    };

    final static Function<Boolean, Boolean> isTrue = new Function<Boolean, Boolean>()
    {
        public Boolean get(Boolean e)
        {
            return e;
        }
    };

    /**
     * Each row of the matrix represents a point of the polygon. The second argument denotes the
     * tolerance: Points within distance <i>e</i> are considered to be inside.
     */
    public LargePolygonContainmentTester(Matrix polygon, double e)
    {
        if (e < 0.0)
        {
            throw new IllegalArgumentException("Error bound is negative.");
        }
        this.e = e;
        px = polygon.getColumn(0).rawData();
        py = polygon.getColumn(1).rawData();
        xmin = polygon.getColumn(0).min();
        xmax = polygon.getColumn(0).max();
        ymin = polygon.getColumn(1).min();
        ymax = polygon.getColumn(1).max();
        IntervalSet<Double, Line> lineIntervals = IntervalSet.create();
        for (int i = 1; i < px.length; i++)
        {
            lineIntervals = lineIntervals.add(new Line(i - 1, i));
        }
        lineIntervals = lineIntervals.add(new Line(px.length - 1, 0));
        lines = lineIntervals;
    }

    /**
     * Test if a point is inside of the polygon.
     */
    public Boolean get(Vector v)
    {
        if (v.size() != 2)
            throw new IllegalArgumentException(
                            "Expected a 2-dimensional vector.");
        double x = v.at(0);
        double y = v.at(1);
        /*
         * We test if $v$ is inside the bounding box.
         */
        if ((xmin <= x) && (x <= xmax) && (ymin <= y) && (y <= ymax))
        {
            boolean evenNumberOfIntersections = lines.intersections(y - 2 * e,
                            y + 2 * e, new IntersectionAlternator(x, y), alwaysFalse);
            if (!evenNumberOfIntersections)
            {
                return true;
            }
        }
        if (e == 0.0)
        {
            /*
             * No tolerance given.
             */
            return false;
        }
        /*
         * Now check the distance to the polygon boundaries:
         */
        // return lines.intersections(y - 2 * e, y + 2 * e, new CloseToBorder(x,
        // y), isTrue);
        if ((xmin - e <= x) && (x <= xmax + e) && (ymin - e <= y)
                        && (y <= ymax + e))
        {
            return lines.intersections(y - 2 * e, y + 2 * e, new CloseToBorder(
                            x, y), isTrue);
        }
        return false;
    }

    private static boolean withinDistance(double x, double y, double qx1,
                    double qy1, double qx2, double qy2, double e)
    {
        /*
         * Test bounding box:
         */
        double xmin = Math.min(qx1, qx2);
        double xmax = Math.max(qx1, qx2);
        double ymin = Math.min(qy1, qy2);
        double ymax = Math.max(qy1, qy2);
        if ((x < xmin - e) || (x > xmax + e) || (y < ymin - e)
                        || (y > ymax + e))
        {
            return false;
        }// `if`
        /*
         * Test distance to end points:
         */
        double eSquare = e * e;
        double d1Square = (x - qx1) * (x - qx1) + (y - qy1) * (y - qy1);
        if (d1Square <= eSquare)
        {
            return true;
        }
        double d2Square = (x - qx2) * (x - qx2) + (y - qy2) * (y - qy2);
        if (d2Square <= eSquare)
        {
            return true;
        }
        /*
         * All fast tests failed. Now project $(x,y)$ on the infinite extension of line segment $q$,
         * resulting in point $a$. If $v$ is on the line segment and $|a-(x,y)|<e$ i.e.
         * ($|a-(x,y)|^2 < e^2$), then $q$ is within distance. Before, we translate $(x,y)$ and $q$
         * such that $q$ starts in the origin.
         */
        x = x - qx1;
        y = y - qy1;
        double qx = qx2 - qx1;
        double qy = qy2 - qy1;
        double r = (x * qx) + (y * qy);
        double qLength = qx * qx + qy * qy;
        if (r < 0.0 || r > qLength)
        {
            return false;
        }
        r = r / qLength;
        double ax = r * qx;
        double ay = r * qy;
        double dx = ax - x;
        double dy = ay - y;
        return (dx * dx + dy * dy < eSquare);
    }
}
