package lib;

public interface Matrix extends Array
{
    int getRows();

    int getColumns();

    double get(int row, int col) throws InvalidIndexes;

    void set(int row, int col, double value) throws InvalidIndexes;

    void add(Scalar scalar);

    void add(Vector vector) throws ShapeMismatch;

    void add(Matrix other) throws ShapeMismatch;

    void multiplyBy(Scalar scalar);

    void multiplyBy(Vector vector) throws ShapeMismatch;

    void multiplyBy(Matrix other) throws ShapeMismatch;

    void assign(Scalar scalar);

    void assign(Vector vector) throws ShapeMismatch;

    void assign(Matrix other) throws ShapeMismatch;

    Matrix createSlice(int startRow, int endRow, int startCol, int endCol) throws InvalidIndexes;

    Matrix getCopy();

    Matrix getNegation();

    Matrix getSum(Scalar other);

    Matrix getProduct(Scalar other);

    Matrix getSum(Vector vector) throws ShapeMismatch;

    Matrix getSum(Matrix matrix) throws ShapeMismatch;

    Matrix getProduct(Matrix matrix) throws ShapeMismatch;
}
