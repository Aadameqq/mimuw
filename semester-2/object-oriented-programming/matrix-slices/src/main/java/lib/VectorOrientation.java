package lib;

public enum VectorOrientation
{
    HORIZONTAL,
    VERTICAL;

    VectorOrientation getOpposite()
    {
        return this == HORIZONTAL
                ? VERTICAL
                : HORIZONTAL;
    }

    boolean isVertical()
    {
        return this == VERTICAL;
    }

    boolean isHorizontal()
    {
        return this == HORIZONTAL;
    }

    @Override
    public String toString()
    {
        return switch (this)
        {
            case HORIZONTAL -> "horizontal";
            case VERTICAL -> "vertical";
        };
    }
}
