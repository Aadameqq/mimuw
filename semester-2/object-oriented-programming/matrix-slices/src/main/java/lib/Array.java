package lib;

public interface Array
{
    int[] getShape();

    int getElementsCount();

    int getDimensions();

    void transpose();

    void negate();

    Array getCopy();

    Array getNegation();

    Array getSum(Scalar other);

    Array getProduct(Scalar other);

    Array getSum(Vector vector) throws ShapeMismatch, VectorOrientationMismatch;

    Array getProduct(Vector vector) throws ShapeMismatch;

    Array getSum(Matrix matrix) throws ShapeMismatch;

    Array getProduct(Matrix matrix) throws ShapeMismatch;

    double getValue(int... indexes) throws InvalidIndexes;

    void setValue(double value, int... indexes) throws InvalidIndexes;

}
