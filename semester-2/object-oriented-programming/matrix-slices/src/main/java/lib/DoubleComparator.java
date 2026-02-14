package lib;

public class DoubleComparator
{
    private final static double EPSILON = 0.0000001;

    public static boolean areConsideredEqual(double a, double b)
    {
        return Math.abs(a - b) < EPSILON;
    }

    public static long getValueForHash(double x)
    {
        return Math.round(x / EPSILON);
    }
}
