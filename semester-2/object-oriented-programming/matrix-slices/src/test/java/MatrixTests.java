import lib.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MatrixTests
{
    private Matrix testMatrix1;
    private Matrix testMatrix2;
    private Matrix testMatrix3;

    @BeforeEach
    void setUp()
    {
        testMatrix1 = createTestMatrix(new double[][]{
                {1, 2.3, 3},
                {-4, 0, -6},
                {7, 8.1, 9.3}
        });

        testMatrix2 = createTestMatrix(new double[][]{
                {1, 2.3, 3},
                {-4, 0, -6},
        });

        testMatrix3 = createTestMatrix(new double[][]{
                {1},
                {2},
                {3},
        });
    }

    @Test
    void testNegation()
    {
        Matrix expected = createTestMatrix(new double[][]{
                {-1.0, -2.3, -3.0},
                {4.0, 0.0, 6.0},
                {-7.0, -8.1, -9.3}
        });

        assertEquals(expected, testMatrix1.getNegation());

        testMatrix1.negate();

        assertEquals(expected, testMatrix1);
    }

    @Test
    void testSumShouldAddScalarToEachMatrixElementGivenScalar()
    {
        Scalar scalar = createTestScalar(0.2);

        Matrix expected = createTestMatrix(new double[][]{
                {1.2, 2.5, 3.2},
                {-3.8, 0.2, -5.8},
                {7.2, 8.3, 9.5}
        });

        assertEquals(expected, testMatrix1.getSum(scalar));

        testMatrix1.add(scalar);

        assertEquals(expected, testMatrix1);
    }

    @Test
    void testProductShouldMultiplyEachMatrixElementByScalarGivenScalar()
    {
        Scalar scalar = createTestScalar(0.2);

        Matrix expected = createTestMatrix(new double[][]{
                {0.2, 0.46, 0.6},
                {-0.8, 0.0, -1.2},
                {1.4, 1.62, 1.86}
        });

        assertEquals(expected, testMatrix1.getProduct(scalar));

        testMatrix1.multiplyBy(scalar);

        assertEquals(expected, testMatrix1);
    }

    @Test
    void testAssignGivenScalarShouldOverwriteEachMatrixElementByIt()
    {
        testMatrix1.assign(createTestScalar(0.2));

        Matrix expected = createTestMatrix(new double[][]{
                {0.2, 0.2, 0.2},
                {0.2, 0.2, 0.2},
                {0.2, 0.2, 0.2}
        });

        assertEquals(expected, testMatrix1);
    }

    @Test
    void testAssignShouldFailGivenVectorWithNotMatchingShape()
    {
        Vector vector1 = createTestVector(new double[]{1, 2},
                VectorOrientation.HORIZONTAL);
        Vector vector2 = createTestVector(new double[]{1, 2, 3},
                VectorOrientation.VERTICAL);

        assertThrows(ShapeMismatch.class,
                () -> testMatrix2.assign(vector1));
        assertThrows(ShapeMismatch.class,
                () -> testMatrix2.assign(vector2));
    }

    @Test
    void testAssignGivenHorizontalVectorShouldOverwriteEachRowWithIt() throws ShapeMismatch
    {
        Vector vector1 = createTestVector(new double[]{1, 2, 3},
                VectorOrientation.HORIZONTAL);

        Matrix expected = createTestMatrix(new double[][]{
                {1, 2, 3},
                {1, 2, 3},
        });

        testMatrix2.assign(vector1);

        assertEquals(expected, testMatrix2);
    }

    @Test
    void testAssignGivenVerticalVectorShouldOverwriteEachColumnWithIt() throws ShapeMismatch
    {
        Vector vector1 = createTestVector(new double[]{1, 2},
                VectorOrientation.VERTICAL);

        Matrix expected = createTestMatrix(new double[][]{
                {1, 1, 1},
                {2, 2, 2},
        });

        testMatrix2.assign(vector1);

        assertEquals(expected, testMatrix2);
    }

    @Test
    void testAssignShouldFailGivenMatrixWithDifferentShape()
    {
        Matrix matrix1 = createTestMatrix(new double[][]{
                {1, 1, 1},
                {2, 2, 2},
        });

        Matrix matrix2 = createTestMatrix(new double[][]{
                {1, 1},
                {2, 2},
                {3, 3},
        });

        assertThrows(ShapeMismatch.class, () -> testMatrix1.assign(matrix1));
        assertThrows(ShapeMismatch.class,
                () -> testMatrix1.assign(matrix2));
    }

    @Test
    void testAssignShouldOverwriteWithGivenMatrix() throws ShapeMismatch
    {
        Matrix matrix = createTestMatrix(new double[][]{
                {10, 11, 12},
                {13, 14, 15},
                {16, 17, 18}
        });

        testMatrix1.assign(matrix);

        assertEquals(matrix, testMatrix1);
    }

    @Test
    void testSumShouldFailGivenVectorWithNotMatchingShape()
    {
        Vector vector1 = createTestVector(new double[]{1, 2},
                VectorOrientation.HORIZONTAL);
        Vector vector2 = createTestVector(new double[]{1, 2, 3},
                VectorOrientation.VERTICAL);

        assertThrows(ShapeMismatch.class, () -> testMatrix2.getSum(vector1));
        assertThrows(ShapeMismatch.class,
                () -> testMatrix2.getSum(vector2));

        assertThrows(ShapeMismatch.class, () -> testMatrix2.add(vector1));
        assertThrows(ShapeMismatch.class,
                () -> testMatrix2.add(vector2));
    }

    @Test
    void testSumGivenHorizontalVectorShouldAddItToEachRow() throws ShapeMismatch, VectorOrientationMismatch
    {
        Vector vector1 = createTestVector(new double[]{1, 2, 3},
                VectorOrientation.HORIZONTAL);

        Matrix expected = createTestMatrix(new double[][]{
                {2, 4.3, 6},
                {-3, 2, -3},
        });

        assertEquals(expected, testMatrix2.getSum(vector1));

        testMatrix2.add(vector1);

        assertEquals(expected, testMatrix2);
    }

    @Test
    void testAddGivenVerticalVectorShouldAddItToEachColumn() throws ShapeMismatch, VectorOrientationMismatch
    {
        Vector vector1 = createTestVector(new double[]{1, 2},
                VectorOrientation.VERTICAL);

        Matrix expected = createTestMatrix(new double[][]{
                {2, 3.3, 4},
                {-2, 2, -4},
        });

        assertEquals(expected, testMatrix2.getSum(vector1));

        testMatrix2.add(vector1);

        assertEquals(expected, testMatrix2);
    }

    @Test
    void testTransposition()
    {
        Matrix expected1 = createTestMatrix(new double[][]{
                {1.0, -4.0, 7.0},
                {2.3, 0.0, 8.1},
                {3.0, -6.0, 9.3}
        });

        testMatrix1.transpose();
        assertEquals(expected1, testMatrix1);

        Matrix expected2 = createTestMatrix(new double[][]{
                {1, 2.3, 3},
                {-4, 0, -6},
                {7, 8.1, 9.3}
        });

        testMatrix1.transpose();
        assertEquals(expected2, testMatrix1);
    }

    @Test
    void testSumShouldFailGivenMatrixDifferentShape()
    {
        Matrix matrix1 = createTestMatrix(new double[][]{
                {1, 1, 1},
                {2, 2, 2},
        });

        Matrix matrix2 = createTestMatrix(new double[][]{
                {1, 1},
                {2, 2},
                {3, 3},
        });

        assertThrows(ShapeMismatch.class, () -> testMatrix1.add(matrix1));
        assertThrows(ShapeMismatch.class, () -> testMatrix1.getSum(matrix1));
        assertThrows(ShapeMismatch.class,
                () -> testMatrix1.add(matrix2));
        assertThrows(ShapeMismatch.class,
                () -> testMatrix1.getSum(matrix2));
    }

    @Test
    void testSumGivenMatrixWithMatchingShape() throws ShapeMismatch
    {
        Matrix matrix = createTestMatrix(new double[][]{
                {10, 11, 12},
                {13, 14, 15},
                {16, 17, 18}
        });

        Matrix expected = createTestMatrix(new double[][]{
                {11.0, 13.3, 15.0},
                {9.0, 14.0, 9.0},
                {23.0, 25.1, 27.3}
        });

        assertEquals(expected, testMatrix1.getSum(matrix));

        testMatrix1.add(matrix);

        assertEquals(expected, testMatrix1);
    }

    @Test
    void testProductShouldFailGivenMatrixWithColumnsCountThatIsNotEqualToThisRowsCount()
    {
        Matrix matrix = createTestMatrix(new double[][]{
                {1, 2, 3},
                {4, 5, 6}
        });

        assertThrows(ShapeMismatch.class, () -> testMatrix1.getProduct(matrix));
    }

    @Test
    void testProductGivenMatrixWithMatchingShape() throws ShapeMismatch
    {
        Matrix matrix = createTestMatrix(new double[][]{
                {1, 2},
                {5, 6},
                {9, 10}
        });

        Matrix expected = createTestMatrix(new double[][]{
                {39.5, 45.8},
                {-58.0, -68.0},
                {131.2, 155.6}
        });

        assertEquals(expected, testMatrix1.getProduct(matrix));
    }

    @Test
    void testMultiplyShouldFailGivenMatrixWithColumnsCountThatDoesNotMatch()
    {
        Matrix matrix = createTestMatrix(new double[][]{
                {1, 2},
                {5, 6},
                {5, 6},
        });

        assertThrows(ShapeMismatch.class,
                () -> testMatrix2.multiplyBy(matrix));
    }

    @Test
    void testMultiplyShouldFailGivenMatrixWithRowsCountThatDoesNotMatch()
    {
        Matrix matrix = createTestMatrix(new double[][]{
                {1, 2, 3},
                {4, 5, 6},
        });

        assertThrows(ShapeMismatch.class,
                () -> testMatrix2.multiplyBy(matrix));
    }

    @Test
    void testMultiplyByGivenMatrixWithMatchingShape() throws ShapeMismatch
    {
        Matrix matrix = createTestMatrix(new double[][]{
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9},
        });

        testMatrix2.multiplyBy(matrix);

        Matrix expected = createTestMatrix(new double[][]{
                {31.2, 37.5, 43.8},
                {-46.0, -56.0, -66.0}
        });

        assertEquals(expected, testMatrix2);
    }

    @Test
    void testMultiplyByShouldFailGivenVerticalVector()
    {
        Vector vector = createTestVector(new double[]{1}, VectorOrientation.VERTICAL);

        assertThrows(ShapeMismatch.class, () -> testMatrix3.multiplyBy(vector));
    }

    @Test
    void testMultiplyByShouldFailWhenThisHasMoreThanOneColumn()
    {
        Vector vector = createTestVector(new double[]{1},
                VectorOrientation.HORIZONTAL);

        assertThrows(ShapeMismatch.class, () -> testMatrix2.multiplyBy(vector));
    }

    @Test
    void testMultiplyByShouldFailGivenVectorWithLengthNotEqualToOne()
    {
        Vector vector = createTestVector(new double[]{1, 2},
                VectorOrientation.HORIZONTAL);

        assertThrows(ShapeMismatch.class, () -> testMatrix3.multiplyBy(vector));
    }

    @Test
    void testMultiplyByGivenMatchingVector() throws ShapeMismatch
    {
        Vector vector = createTestVector(new double[]{5},
                VectorOrientation.HORIZONTAL);

        testMatrix3.multiplyBy(vector);

        Matrix expected = createTestMatrix(new double[][]{
                {5},
                {10},
                {15},
        });

        assertEquals(expected, testMatrix3);
    }

    @Test
    void testGetAndSetShouldFailGivenIncorrectIndexesAmount()
    {
        assertThrows(InvalidIndexes.class, () -> testMatrix1.getValue(1));
        assertThrows(InvalidIndexes.class, () -> testMatrix1.getValue(1, 2, 3));

        assertThrows(InvalidIndexes.class, () -> testMatrix1.setValue(2.0, 1));
        assertThrows(InvalidIndexes.class, () -> testMatrix1.setValue(2.0, 1,
                2, 3));

        Matrix expected = createTestMatrix(new double[][]{
                {1, 2.3, 3},
                {-4, 0, -6},
                {7, 8.1, 9.3}
        });

        assertEquals(expected, testMatrix1);
    }

    @Test
    void testGet() throws InvalidIndexes
    {
        assertEquals(8.1, testMatrix1.get(2, 1));
        assertEquals(8.1, testMatrix1.getValue(2, 1));
    }

    @Test
    void testSet() throws InvalidIndexes
    {
        testMatrix1.setValue(9.0, 2, 1);

        Matrix expected1 = createTestMatrix(new double[][]{
                {1, 2.3, 3},
                {-4, 0, -6},
                {7, 9.0, 9.3}
        });

        assertEquals(expected1, testMatrix1);

        testMatrix1.set(2, 1, 10.0);

        Matrix expected2 = createTestMatrix(new double[][]{
                {1, 2.3, 3},
                {-4, 0, -6},
                {7, 10.0, 9.3}
        });

        assertEquals(expected2, testMatrix1);
    }

    @ParameterizedTest
    @CsvSource({"-1, 2", "1, -1", "10, 1", "1, 10",})
    void testGetAndSetFailsIfIndexIsInvalid(int row, int col)
    {
        assertThrows(InvalidIndexes.class, () -> testMatrix1.get(row, col));
        assertThrows(InvalidIndexes.class, () -> testMatrix1.getValue(row, col));

        assertThrows(InvalidIndexes.class, () -> testMatrix1.set(row, col, 2.0));
        assertThrows(InvalidIndexes.class, () -> testMatrix1.setValue(2.0, row, col));

        Matrix expected = createTestMatrix(new double[][]{
                {1, 2.3, 3},
                {-4, 0, -6},
                {7, 8.1, 9.3}
        });

        assertEquals(expected, testMatrix1);
    }

    @Test
    void testProductShouldFailGivenVerticalVectorWithLengthNotEqualToThisColumnsAmount()
    {
        Vector vector = createTestVector(new double[]{2, 3}, VectorOrientation.VERTICAL);

        assertThrows(ShapeMismatch.class, () -> testMatrix2.getProduct(vector));
    }

    @Test
    void testProductShouldReturnVectorGivenVerticalVectorWithMatchingLength() throws ShapeMismatch
    {
        Vector vector = createTestVector(new double[]{2, 3, 4}, VectorOrientation.VERTICAL);

        Vector expected = createTestVector(new double[]{20.9, -32.0}, VectorOrientation.VERTICAL);

        assertEquals(expected, testMatrix2.getProduct(vector));
    }

    @Test
    void testProductShouldFailGivenHorizontalVectorWhenThisHasColumnsCountNotEqualToOne() throws ShapeMismatch
    {
        Vector vector = createTestVector(new double[]{2, 3, 4}, VectorOrientation.HORIZONTAL);

        assertThrows(ShapeMismatch.class, () -> testMatrix2.getProduct(vector));
    }

    @Test
    void testProductGivenHorizontalVectorWhenThisHasOneColumn() throws ShapeMismatch
    {
        Vector vector = createTestVector(new double[]{2, 3, 4, 5, 6}, VectorOrientation.HORIZONTAL);

        Matrix expected = createTestMatrix(new double[][]{
                {2, 3, 4, 5, 6},
                {4, 6, 8, 10, 12},
                {6, 9, 12, 15, 18}
        });

        assertEquals(expected, testMatrix3.getProduct(vector));
    }

    @Test
    void testToString()
    {
        String expected = """
                Shape: 2x3
                [1.0, 2.3, 3.0]
                [-4.0, 0.0, -6.0]
                """;

        assertEquals(expected, testMatrix2.toString());
    }

    private Matrix createTestMatrix(double[][] array)
    {
        return new ArrayMatrix(array);
    }

    private Scalar createTestScalar(double value)
    {
        return new BasicScalar(value);
    }

    private Vector createTestVector(double[] values, VectorOrientation orientation)
    {
        return new ArrayVector(values, orientation);
    }
}
