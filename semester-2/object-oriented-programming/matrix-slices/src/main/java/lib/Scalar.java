package lib;

public interface Scalar extends Array
{
    double get();

    void set(double value);

    void assign(Scalar other);

    Scalar createSlice();

    void add(Scalar other);

    void multiplyBy(Scalar other);

    Scalar getCopy();

    Scalar getNegation();

    Scalar getSum(Scalar other);

    Scalar getProduct(Scalar other);

    Vector getSum(Vector vector) throws ShapeMismatch, VectorOrientationMismatch;

    Vector getProduct(Vector vector) throws ShapeMismatch;

    Matrix getSum(Matrix matrix) throws ShapeMismatch;

    Matrix getProduct(Matrix matrix) throws ShapeMismatch;
}
