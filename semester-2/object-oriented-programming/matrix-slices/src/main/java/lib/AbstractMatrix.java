package lib;

import java.util.Arrays;

public abstract class AbstractMatrix implements Matrix
{
    protected boolean isTransposed = false;

    @Override
    public int getElementsCount()
    {
        return getRows() * getColumns();
    }

    @Override
    public int[] getShape()
    {
        return new int[]{getRows(), getColumns()};
    }

    @Override
    public int getDimensions()
    {
        return 2;
    }

    @Override
    public void setValue(double value, int... indexes) throws InvalidIndexes
    {
        if (indexes.length != 2)
        {
            throw new InvalidIndexes();
        }

        int row = indexes[0];
        int column = indexes[1];
        set(row, column, value);
    }

    @Override
    public double getValue(int... indexes) throws InvalidIndexes
    {
        if (indexes.length != 2)
        {
            throw new InvalidIndexes();
        }

        int row = indexes[0];
        int column = indexes[1];
        return get(row, column);
    }

    private void makeLinearTransformation(double scalar, double translation)
    {
        makeLinearTransformation(this, scalar, translation);
    }

    private Matrix getLinearTransformation(double scalar, double translation)
    {
        Matrix matrix = Factory.createEmptyMatrix(getRows(), getColumns());
        makeLinearTransformation(matrix, scalar, translation);
        return matrix;
    }

    private void makeLinearTransformation(Matrix target, double scalar, double translation)
    {
        try
        {
            for (int row = 0; row < getRows(); row++)
            {
                for (int col = 0; col < getColumns(); col++)
                {
                    double value = get(row, col) * scalar + translation;
                    target.set(row, col, value);
                }
            }
        }
        catch (InvalidIndexes e)
        {
            ExceptionHandler.handle("Tried to access non-existent index", e);
        }

    }

    private void makeLinearTransformation(double scalar, Vector vector)
    {
        makeLinearTransformation(this, scalar, vector);
    }

    private Matrix getLinearTransformation(double scalar, Vector vector)
    {
        Matrix matrix = Factory.createEmptyMatrix(getRows(), getColumns());
        makeLinearTransformation(matrix, scalar, vector);
        return matrix;
    }

    private void makeLinearTransformation(Matrix target, double scalar, Vector vector)
    {
        try
        {
            for (int row = 0; row < getRows(); row++)
            {
                for (int col = 0; col < getColumns(); col++)
                {
                    int vectorIndex = vector.getOrientation().isVertical() ? row : col;
                    double value = scalar * get(row, col) + vector.get(vectorIndex);
                    target.set(row, col, value);
                }
            }
        }
        catch (InvalidIndexes e)
        {
            ExceptionHandler.handle("Tried to access non-existent index", e);
        }
    }

    private void makeLinearTransformation(double scalar, Matrix other)
    {
        makeLinearTransformation(this, scalar, other);
    }

    private Matrix getLinearTransformation(double scalar, Matrix other)
    {
        Matrix matrix = Factory.createEmptyMatrix(getRows(), getColumns());
        makeLinearTransformation(matrix, scalar, other);
        return matrix;
    }

    private void makeLinearTransformation(Matrix target, double scalar, Matrix other)
    {
        try
        {
            for (int row = 0; row < getRows(); row++)
            {
                for (int column = 0; column < getColumns(); column++)
                {
                    double value = scalar * get(row, column) + other.get(row, column);
                    target.set(row, column, value);
                }
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
    public Matrix getNegation()
    {
        return getLinearTransformation(-1, 0);
    }

    @Override
    public void multiplyBy(Scalar other)
    {
        makeLinearTransformation(other.get(), 0);
    }

    @Override
    public void add(Scalar other)
    {
        makeLinearTransformation(1, other.get());
    }

    @Override
    public Matrix getSum(Scalar other)
    {
        return getLinearTransformation(1, other.get());
    }

    @Override
    public Matrix getProduct(Scalar other)
    {
        return getLinearTransformation(other.get(), 0);
    }

    @Override
    public void assign(Scalar scalar)
    {
        makeLinearTransformation(0, scalar.get());
    }

    @Override
    public void assign(Vector vector) throws ShapeMismatch
    {
        if (vector.getOrientation().isHorizontal() && getColumns() != vector.getLength())
        {
            throw new ShapeMismatch();
        }

        if (vector.getOrientation().isVertical() && getRows() != vector.getLength())
        {
            throw new ShapeMismatch();
        }

        makeLinearTransformation(0, vector);
    }

    @Override
    public Matrix getSum(Vector vector) throws ShapeMismatch
    {
        Matrix matrix = getCopy();
        matrix.add(vector);
        return matrix;
    }

    @Override
    public void add(Vector vector) throws ShapeMismatch
    {
        int vectorLength = vector.getLength();
        VectorOrientation orientation = vector.getOrientation();
        if (orientation.isVertical() && getRows() != vectorLength)
        {
            throw new ShapeMismatch();
        }
        if (orientation.isHorizontal() && getColumns() != vectorLength)
        {
            throw new ShapeMismatch();
        }

        makeLinearTransformation(1, vector);
    }

    @Override
    public void multiplyBy(Vector vector) throws ShapeMismatch
    {
        if (!vector.getOrientation().isHorizontal() || vector.getLength() != getColumns() || getColumns() != 1)
        {
            throw new ShapeMismatch();
        }

        Matrix result = getProductWithHorizontal(vector);
        assign(result);
    }

    @Override
    public void add(Matrix other) throws ShapeMismatch
    {
        if (getRows() != other.getRows() || getColumns() != other.getColumns())
        {
            throw new ShapeMismatch();
        }

        makeLinearTransformation(1, other);
    }

    @Override
    public Matrix getSum(Matrix other) throws ShapeMismatch
    {
        Matrix matrix = getCopy();
        matrix.add(other);
        return matrix;
    }

    private void makeProduct(Matrix target, Matrix other)
    {
        try
        {
            for (int row = 0; row < target.getRows(); row++)
            {
                for (int col = 0; col < target.getColumns(); col++)
                {
                    double value = 0;
                    for (int depth = 0; depth < getColumns(); depth++)
                    {
                        value += get(row, depth) * other.get(depth, col);
                    }
                    target.set(row, col, value);
                }
            }
        }
        catch (InvalidIndexes e)
        {
            ExceptionHandler.handle("Tried to access non-existent index", e);
        }
    }

    @Override
    public Matrix getProduct(Matrix other) throws ShapeMismatch
    {
        if (getColumns() != other.getRows())
        {
            throw new ShapeMismatch();
        }
        Matrix result = Factory.createEmptyMatrix(getRows(), other.getColumns());
        makeProduct(result, other);
        return result;
    }

    @Override
    public void assign(Matrix other) throws ShapeMismatch
    {
        if (getRows() != other.getRows() || getColumns() != other.getColumns())
        {
            throw new ShapeMismatch();
        }
        makeLinearTransformation(0, other);
    }

    private Matrix getProductWithHorizontal(Vector vector)
    {
        Matrix matrix = Factory.createEmptyMatrix(getRows(), vector.getLength());
        try
        {
            for (int resultRow = 0; resultRow < matrix.getRows(); resultRow++)
            {
                for (int resultCol = 0; resultCol < matrix.getColumns(); resultCol++)
                {
                    double value = get(resultRow, 0) * vector.get(resultCol);
                    matrix.set(resultRow, resultCol, value);
                }
            }
        }
        catch (InvalidIndexes e)
        {
            ExceptionHandler.handle("Tried to access non-existent index", e);
        }
        return matrix;
    }

    private Vector getProductWithVertical(Vector vector)
    {
        Vector result = Factory.createEmptyVector(getRows(), VectorOrientation.VERTICAL);
        try
        {
            for (int n = 0; n < getRows(); n++)
            {
                for (int depth = 0; depth < getColumns(); depth++)
                {
                    double value = result.get(n) + get(n, depth) * vector.get(depth);
                    result.set(n, value);
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
    public Array getProduct(Vector vector) throws ShapeMismatch
    {
        if (vector.getOrientation().isVertical())
        {
            if (getColumns() != vector.getLength())
            {
                throw new ShapeMismatch();
            }
            return getProductWithVertical(vector);
        }

        if (getColumns() != 1)
        {
            throw new ShapeMismatch();
        }

        return getProductWithHorizontal(vector);
    }

    @Override
    public void multiplyBy(Matrix other) throws ShapeMismatch
    {
        if (other.getColumns() != other.getRows() || other.getColumns() != getColumns())
        {
            throw new ShapeMismatch();
        }
        Matrix newMatrix = Factory.createEmptyMatrix(getRows(), getColumns());
        makeProduct(newMatrix, other);
        assign(newMatrix);
    }

    @Override
    public Matrix createSlice(int startRow, int endRow, int startCol, int endCol) throws InvalidIndexes
    {
        return SliceMatrix.createInstance(startRow, endRow, startCol, endCol, this);
    }

    protected abstract void doTranspose();

    @Override
    public void transpose()
    {
        isTransposed = !isTransposed;
        doTranspose();
    }

    @Override
    public String toString()
    {

        StringBuilder sb = new StringBuilder();
        sb.append("Shape: ")
                .append(getRows())
                .append("x")
                .append(getColumns())
                .append("\n");
        try
        {
            for (int row = 0; row < getRows(); row++)
            {
                sb.append("[");
                for (int col = 0; col < getColumns(); col++)
                {
                    sb.append(get(row, col));
                    if (col != getColumns() - 1)
                    {
                        sb.append(", ");
                    }
                }
                sb.append("]\n");
            }
        }
        catch (InvalidIndexes e)
        {
            ExceptionHandler.handle("Tried to access non-existent index", e);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Matrix matrix)) return false;
        try
        {
            for (int row = 0; row < getRows(); row++)
            {
                for (int col = 0; col < getColumns(); col++)
                {
                    if (!DoubleComparator.areConsideredEqual(matrix.get(row,
                            col), get(row, col)))
                    {
                        return false;
                    }
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
        long[][] valuesForHash = new long[getRows()][getColumns()];

        try
        {
            for (int row = 0; row < getRows(); row++)
            {
                for (int col = 0; col < getColumns(); col++)
                {
                    valuesForHash[row][col] = DoubleComparator.getValueForHash(get(row, col));
                }
            }
        }
        catch (InvalidIndexes e)
        {
            ExceptionHandler.handle("Tried to access non-existent index", e);
        }

        return Arrays.deepHashCode(valuesForHash);
    }
}
