package lib;

public class BasicScalar implements Scalar
{
    private double value;

    public BasicScalar(double value)
    {
        this.value = value;
    }

    @Override
    public Scalar getNegation()
    {
        Scalar newScalar = new BasicScalar(value);
        newScalar.negate();
        return newScalar;
    }

    @Override
    public void negate()
    {
        value = value == 0 ? 0 : -value;
    }

    @Override
    public int[] getShape()
    {
        return new int[0];
    }

    @Override
    public int getElementsCount()
    {
        return 1;
    }

    @Override
    public Scalar getCopy()
    {
        return new BasicScalar(value);
    }

    @Override
    public int getDimensions()
    {
        return 0;
    }

    @Override
    public void transpose()
    {
    }

    @Override
    public void multiplyBy(Scalar other)
    {
        value *= other.get();
    }

    @Override
    public void add(Scalar other)
    {
        value += other.get();
    }

    @Override
    public Scalar getSum(Scalar other)
    {
        return new BasicScalar(value + other.get());
    }

    @Override
    public Scalar getProduct(Scalar other)
    {
        return new BasicScalar(value * other.get());
    }

    @Override
    public Vector getSum(Vector vector)
    {
        return vector.getSum(this);
    }

    @Override
    public Vector getProduct(Vector vector)
    {
        return vector.getProduct(this);
    }

    @Override
    public Matrix getSum(Matrix matrix)
    {
        return matrix.getSum(this);
    }

    @Override
    public Matrix getProduct(Matrix matrix)
    {
        return matrix.getProduct(this);
    }

    @Override
    public double getValue(int... indexes) throws InvalidIndexes
    {
        if (indexes.length != 0)
        {
            throw new InvalidIndexes();
        }
        return value;
    }

    @Override
    public void setValue(double value, int... indexes) throws InvalidIndexes
    {
        if (indexes.length != 0)
        {
            throw new InvalidIndexes();
        }
        this.value = value;
    }

    @Override
    public void assign(Scalar other)
    {
        value = other.get();
    }

    @Override
    public Scalar createSlice()
    {
        return this;
    }

    @Override
    public double get()
    {
        return value;
    }

    @Override
    public void set(double newValue)
    {
        value = newValue;
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Scalar scalar)) return false;
        return DoubleComparator.areConsideredEqual(get(), scalar.get());
    }

    @Override
    public int hashCode()
    {
        long valueForHash = DoubleComparator.getValueForHash(get());
        return Long.hashCode(valueForHash);
    }

    @Override
    public String toString()
    {
        return "[" + get() + "]\n";
    }
}
