package lib;

import java.util.Arrays;
import java.util.Objects;

public abstract class AbstractVector implements Vector
{
    private VectorOrientation orientation;

    public AbstractVector(VectorOrientation orientation)
    {
        this.orientation = orientation;
    }

    @Override
    public void transpose()
    {
        orientation = orientation.getOpposite();
    }

    @Override
    public VectorOrientation getOrientation()
    {
        return orientation;
    }

    @Override
    public int[] getShape()
    {
        return new int[]{getLength()};
    }

    @Override
    public int getElementsCount()
    {
        return getLength();
    }

    @Override
    public int getDimensions()
    {
        return 1;
    }

    @Override
    public double getValue(int... indexes) throws InvalidIndexes
    {
        if (indexes.length != 1)
        {
            throw new InvalidIndexes();
        }
        return get(indexes[0]);
    }

    @Override
    public void setValue(double value, int... indexes) throws InvalidIndexes
    {
        if (indexes.length != 1)
        {
            throw new InvalidIndexes();
        }
        set(indexes[0], value);
    }

    private void makeLinearTransformation(double scalar, double translation)
    {
        try
        {
            for (int i = 0; i < getLength(); i++)
            {
                double curr = get(i);
                set(i, (curr == 0 ? 0 : scalar * curr) + translation);
            }
        }
        catch (InvalidIndexes e)
        {
            ExceptionHandler.handle("Tried to access non-existent index", e);
        }
    }

    @Override
    public void negate()
    {
        makeLinearTransformation(-1, 0);
    }

    @Override
    public Vector getNegation()
    {
        Vector vector = getCopy();
        vector.negate();
        return vector;
    }

    @Override
    public void add(Scalar other)
    {
        makeLinearTransformation(1, other.get());
    }

    @Override
    public Vector getSum(Scalar other)
    {
        Vector vector = getCopy();
        vector.add(other);
        return vector;
    }

    @Override
    public void multiplyBy(Scalar other)
    {
        makeLinearTransformation(other.get(), 0);
    }

    @Override
    public Vector getProduct(Scalar other)
    {
        Vector vector = getCopy();
        vector.multiplyBy(other);
        return vector;
    }

    @Override
    public Matrix getSum(Matrix matrix) throws ShapeMismatch
    {
        return matrix.getSum(this);
    }

    @Override
    public void assign(Scalar scalar)
    {
        makeLinearTransformation(0, scalar.get());
    }

    @Override
    public void add(Vector other) throws ShapeMismatch, VectorOrientationMismatch
    {
        if (getLength() != other.getLength())
        {
            throw new ShapeMismatch();
        }

        if (getOrientation() != other.getOrientation())
        {
            throw new VectorOrientationMismatch();
        }
        try
        {
            for (int i = 0; i < getLength(); i++)
            {
                set(i, get(i) + other.get(i));
            }
        }
        catch (InvalidIndexes e)
        {
            ExceptionHandler.handle("Tried to access non-existent index", e);

        }
    }

    @Override
    public Vector getSum(Vector other) throws ShapeMismatch, VectorOrientationMismatch
    {
        Vector vector = getCopy();
        vector.add(other);
        return vector;
    }

    @Override
    public Array getProduct(Vector other) throws ShapeMismatch
    {
        if (getOrientation() == other.getOrientation())
        {
            if (getLength() != other.getLength())
            {
                throw new ShapeMismatch();
            }
            return getScalarProduct(other);
        }

        if (getOrientation().isVertical())
        {
            return getMatrixProductWithHorizontal(other);
        }

        if (getLength() != other.getLength())
        {
            throw new ShapeMismatch();
        }

        return getMatrixProductWithVertical(other);
    }

    private Array getMatrixProductWithHorizontal(Vector other)
    {
        Matrix result = Factory.createEmptyMatrix(getLength(), other.getLength());
        try
        {
            for (int row = 0; row < result.getRows(); row++)
            {
                for (int col = 0; col < result.getColumns(); col++)
                {
                    result.set(row, col, get(row) * other.get(col));
                }
            }
        }
        catch (InvalidIndexes e)
        {
            ExceptionHandler.handle("Tried to access non-existent index", e);

        }
        return result;
    }

    private Matrix getMatrixProductWithVertical(Vector other)
    {
        Matrix result = Factory.createEmptyMatrix(1, 1);

        double acc = 0;
        try
        {
            for (int depth = 0; depth < getLength(); depth++)
            {
                acc += get(depth) * other.get(depth);
            }
            result.set(0, 0, acc);
        }
        catch (InvalidIndexes e)
        {
            ExceptionHandler.handle("Tried to access non-existent index", e);

        }
        return result;
    }

    private Scalar getScalarProduct(Vector other)
    {
        double acc = 0;
        try
        {
            for (int depth = 0; depth < getLength(); depth++)
            {
                acc += get(depth) * other.get(depth);
            }
        }
        catch (InvalidIndexes e)
        {
            ExceptionHandler.handle("Tried to access non-existent index", e);

        }
        return Factory.createScalar(acc);
    }

    @Override
    public void assign(Vector other) throws ShapeMismatch
    {
        if (other.getLength() != getLength())
        {
            throw new ShapeMismatch();
        }
        try
        {
            for (int i = 0; i < getLength(); i++)
            {
                set(i, other.get(i));
            }
        }
        catch (InvalidIndexes e)
        {
            ExceptionHandler.handle("Tried to access non-existent index", e);

        }
        orientation = other.getOrientation();
    }

    @Override
    public Array getProduct(Matrix matrix) throws ShapeMismatch
    {
        if (getOrientation().isVertical())
        {
            if (matrix.getRows() != 1)
            {
                throw new ShapeMismatch();
            }
            return getProductOfVerticalWithMatrix(matrix);
        }

        if (getLength() != matrix.getRows())
        {
            throw new ShapeMismatch();
        }

        return getProductOfHorizontalWithMatrix(matrix);
    }

    private Vector getProductOfHorizontalWithMatrix(Matrix matrix)
    {
        Vector result = Factory.createEmptyVector(matrix.getColumns(), orientation);
        try
        {
            for (int n = 0; n < result.getLength(); n++)
            {
                for (int depth = 0; depth < getLength(); depth++)
                {
                    result.set(n, result.get(n) + get(depth) * matrix.get(depth, n));
                }
            }
        }
        catch (InvalidIndexes e)
        {
            ExceptionHandler.handle("Tried to access non-existent index", e);

        }

        return result;
    }

    private Matrix getProductOfVerticalWithMatrix(Matrix matrix)
    {
        Matrix result = Factory.createEmptyMatrix(getLength(), matrix.getColumns());
        try
        {
            for (int resultRow = 0; resultRow < result.getRows(); resultRow++)
            {
                for (int resultCol = 0; resultCol < result.getColumns(); resultCol++)
                {
                    double value = get(resultRow) * matrix.get(0, resultCol);
                    result.set(resultRow, resultCol, value);
                }
            }
        }
        catch (InvalidIndexes e)
        {
            ExceptionHandler.handle("Tried to access non-existent index", e);

        }
        return result;
    }

    @Override
    public Vector createSlice(int start, int end) throws InvalidIndexes
    {
        return SliceVector.createInstance(start, end, this);
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Orientation: ").append(getOrientation()).append("\n");
        sb.append("[");
        try
        {
            for (int i = 0; i < getLength(); i++)
            {
                sb.append(get(i));

                if (i != getLength() - 1)
                {
                    sb.append(", ");
                }
            }
        }
        catch (InvalidIndexes e)
        {
            ExceptionHandler.handle("Tried to access non-existent index", e);

        }
        sb.append("]\n");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof AbstractVector other)) return false;

        if (getOrientation() != other.getOrientation())
        {
            return false;
        }
        try
        {
            for (int i = 0; i < getLength(); i++)
            {
                if (!DoubleComparator.areConsideredEqual(get(i), other.get(i)))
                {
                    return false;
                }
            }
        }
        catch (InvalidIndexes e)
        {
            ExceptionHandler.handle("Tried to access non-existent index", e);
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        long[] valuesForHash = new long[getLength()];
        try
        {
            for (int i = 0; i < getLength(); i++)
            {
                valuesForHash[i] = DoubleComparator.getValueForHash(get(i));
            }
        }
        catch (InvalidIndexes e)
        {
            ExceptionHandler.handle("Tried to access non-existent index", e);
        }

        return Objects.hash(Arrays.hashCode(valuesForHash), getOrientation());
    }
}
