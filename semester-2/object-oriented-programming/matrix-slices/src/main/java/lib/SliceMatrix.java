package lib;

public class SliceMatrix extends AbstractMatrix
{
    private final int startRow;
    private final int endRow;
    private final int startColumn;
    private final int endColumn;
    private final AbstractMatrix source;

    private SliceMatrix(int startRow, int endRow, int startColumn, int endColumn, AbstractMatrix source)
    {
        this.startRow = startRow;
        this.endRow = endRow;
        this.startColumn = startColumn;
        this.endColumn = endColumn;
        this.source = source;
    }

    public static SliceMatrix createInstance(int startRow, int endRow, int startColumn, int endColumn, AbstractMatrix source) throws InvalidIndexes
    {
        if (isPositionInvalid(startRow, endRow, source.getRows()) ||
                isPositionInvalid(startColumn, endColumn, source.getColumns()))
        {
            throw new InvalidIndexes();
        }
        return new SliceMatrix(startRow, endRow, startColumn, endColumn, source);
    }

    private static boolean isPositionInvalid(int start, int end, int length)
    {
        return start > end || start < 0 || end >= length;
    }

    @Override
    public int getRows()
    {
        return isTransposed ? endColumn - startColumn + 1 : endRow - startRow + 1;
    }

    @Override
    public int getColumns()
    {
        return isTransposed ? endRow - startRow + 1 : endColumn - startColumn + 1;

    }

    @Override
    public double get(int row, int col) throws InvalidIndexes
    {
        int absoluteRow = startRow + (isTransposed ? col : row);
        int absoluteCol = startColumn + (isTransposed ? row : col);

        if (source.isTransposed)
        {
            return source.get(absoluteCol, absoluteRow);
        }
        return source.get(absoluteRow, absoluteCol);
    }

    @Override
    public void set(int row, int col, double value) throws InvalidIndexes
    {
        int absoluteRow = startRow + (isTransposed ? col : row);
        int absoluteCol = startColumn + (isTransposed ? row : col);

        if (source.isTransposed)
        {
            source.set(absoluteCol, absoluteRow, value);
            return;
        }
        source.set(absoluteRow, absoluteCol, value);
    }

    @Override
    public Matrix getCopy()
    {
        return new SliceMatrix(startRow, endRow, startColumn, endColumn, source);
    }

    @Override
    public void doTranspose()
    {
    }

}
