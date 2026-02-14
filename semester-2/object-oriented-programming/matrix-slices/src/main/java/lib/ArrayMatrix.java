package lib;

import java.util.Arrays;

public class ArrayMatrix extends AbstractMatrix
{
    private double[][] array;

    public ArrayMatrix(double[][] array)
    {
        this.array = array;
    }

    public ArrayMatrix(int rows, int columns)
    {
        this(new double[rows][columns]);
    }

    @Override
    public Matrix getCopy()
    {
        double[][] newArr = new double[getRows()][];

        for (int row = 0; row < getRows(); row++)
        {
            newArr[row] = Arrays.copyOf(array[row], getColumns());
        }
        return new ArrayMatrix(newArr);
    }

    @Override
    public double get(int row, int col) throws InvalidIndexes
    {
        failIfIndexesInvalid(row, col);
        return array[row][col];
    }

    @Override
    public void set(int row, int col, double value) throws InvalidIndexes
    {
        failIfIndexesInvalid(row, col);
        array[row][col] = value;
    }

    private void failIfIndexesInvalid(int row, int col) throws InvalidIndexes
    {
        if (row < 0 || row >= getRows())
        {
            throw new InvalidIndexes();
        }

        if (col < 0 || col >= getColumns())
        {
            throw new InvalidIndexes();
        }
    }

    @Override
    public int getRows()
    {
        return array.length;
    }

    @Override
    public int getColumns()
    {
        return array[0].length;
    }

    @Override
    public void doTranspose()
    {
        double[][] newArray = new double[getColumns()][getRows()];

        for (int row = 0; row < getRows(); row++)
        {
            for (int col = 0; col < getColumns(); col++)
            {
                newArray[col][row] = array[row][col];
            }
        }
        this.array = newArray;
    }
}
