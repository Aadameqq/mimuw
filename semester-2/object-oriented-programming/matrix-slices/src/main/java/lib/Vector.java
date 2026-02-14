package lib;

public interface Vector extends Array
{
    VectorOrientation getOrientation();

    int getLength();

    void set(int n, double value) throws InvalidIndexes;

    double get(int n) throws InvalidIndexes;

    void add(Scalar other);

    void add(Vector other) throws ShapeMismatch, VectorOrientationMismatch;

    void multiplyBy(Scalar other);

    void assign(Scalar scalar);

    void assign(Vector vector) throws ShapeMismatch;

    Vector createSlice(int start, int end) throws InvalidIndexes;

    Vector getCopy();

    Vector getNegation();

    Vector getSum(Scalar other);

    Vector getProduct(Scalar other);

    Vector getSum(Vector vector) throws ShapeMismatch, VectorOrientationMismatch;

    Matrix getSum(Matrix matrix) throws ShapeMismatch;
}
