package lib;

import java.util.Arrays;

public class ArrayVector extends AbstractVector
{
    private final double[] array;

    public ArrayVector(double[] array, VectorOrientation orientation)
    {
        super(orientation);
        this.array = array;
    }

    public ArrayVector(int length, VectorOrientation orientation)
    {
        this(new double[length], orientation);
    }

    @Override
    public Vector getCopy()
    {
        double[] newArr = Arrays.copyOf(array, array.length);
        return new ArrayVector(newArr, getOrientation());
    }

    private void failIfIndexInvalid(int index) throws InvalidIndexes
    {
        if (index < 0 || index >= getLength())
        {
            throw new InvalidIndexes();
        }

    }

    @Override
    public double get(int n) throws InvalidIndexes
    {
        failIfIndexInvalid(n);
        return array[n];
    }

    @Override
    public int getLength()
    {
        return array.length;
    }

    @Override
    public void set(int n, double value) throws InvalidIndexes
    {
        failIfIndexInvalid(n);
        array[n] = value;
    }
}
