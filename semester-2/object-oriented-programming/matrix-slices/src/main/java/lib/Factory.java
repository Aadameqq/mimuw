package lib;

public class Factory
{
    public static Matrix createEmptyMatrix(int rows, int columns)
    {
        return new ArrayMatrix(rows, columns);
    }

    public static Vector createEmptyVector(int length, VectorOrientation orientation)
    {
        return new ArrayVector(length, orientation);
    }

    public static Scalar createScalar(double value)
    {
        return new BasicScalar(value);
    }
}
