package lib;

public class SliceVector extends AbstractVector
{
    private final int startIndex;
    private final int endIndex;
    private final AbstractVector source;

    private SliceVector(int startIndex, int endIndex, AbstractVector source)
    {
        super(source.getOrientation());
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.source = source;
    }

    public static SliceVector createInstance(int startIndex, int endIndex, AbstractVector source) throws InvalidIndexes
    {
        if (startIndex > endIndex || startIndex < 0 || endIndex >= source.getLength())
        {
            throw new InvalidIndexes();
        }
        return new SliceVector(startIndex, endIndex, source);
    }

    @Override
    public int getLength()
    {
        return endIndex - startIndex + 1;
    }

    @Override
    public void set(int n, double value) throws InvalidIndexes
    {
        source.set(startIndex + n, value);
    }

    @Override
    public double get(int n) throws InvalidIndexes
    {
        return source.get(startIndex + n);
    }

    @Override
    public Vector getCopy()
    {
        return new SliceVector(startIndex, endIndex, source);
    }
}
