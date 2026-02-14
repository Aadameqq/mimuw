import lib.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VectorTests
{
    private Vector testVector;

    @BeforeEach
    void setUp()
    {
        testVector = createTestVector(new double[]{1.0, 0.0, 3.0, 4.0, 5.0});
    }

    @Test
    void testTransposeShouldChangeOrientation()
    {
        testVector.transpose();

        Vector expected = createTestVector(new double[]{1.0, 0.0, 3.0, 4.0,
                        5.0},
                VectorOrientation.HORIZONTAL);

        assertEquals(expected, testVector);
    }

    @Test
    void testAssignGivenScalarShouldOverwriteEachVectorElementWithItsValue()
    {
        testVector.assign(new BasicScalar(7.0));

        Vector expected = createTestVector(new double[]{7.0, 7.0, 7.0, 7.0,
                7.0});

        assertEquals(expected, testVector);
    }

    @Test
    void testSumGivenScalarShouldAddItToEachElement()
    {
        Vector expected = createTestVector(new double[]{3.0, 2.0, 5.0, 6.0,
                7.0});
        Scalar scalar = createTestScalar(2.0);

        assertEquals(expected, testVector.getSum(scalar));

        testVector.add(scalar);

        assertEquals(expected, testVector);
    }

    @Test
    void testProductGivenScalarShouldMultiplyEachElementByIt()
    {
        Vector expected = createTestVector(new double[]{2.0, 0.0, 6.0, 8.0,
                10.0});
        Scalar scalar = createTestScalar(2.0);

        assertEquals(expected, testVector.getProduct(scalar));

        testVector.multiplyBy(scalar);

        assertEquals(expected, testVector);
    }

    @Test
    void testNegation()
    {
        Vector expected = createTestVector(new double[]{-1.0, 0.0, -3.0, -4.0});

        assertEquals(expected, testVector.getNegation());

        testVector.negate();

        assertEquals(expected, testVector);
    }

    @Test
    void testAddShouldFailGivenVectorWithDifferentOrientation()
    {
        Vector vector = createTestVector(new double[]{1.0, 0.0, 3.0, 4.0,
                5.0}, VectorOrientation.HORIZONTAL);

        assertThrows(VectorOrientationMismatch.class,
                () -> testVector.getSum(vector));

        assertThrows(VectorOrientationMismatch.class, () -> testVector.add(vector));
    }

    @Test
    void testAddShouldFailGivenVectorWithDifferentShape()
    {
        Vector vector = createTestVector(new double[]{1.0, 0.0, 3.0});

        assertThrows(ShapeMismatch.class, () -> testVector.getSum(vector));

        assertThrows(ShapeMismatch.class, () -> testVector.add(vector));
    }

    @Test
    void testAddGivenVectorWithSameOrientationAndShape() throws VectorOrientationMismatch, ShapeMismatch
    {
        Vector vector = createTestVector(new double[]{2.0, 0.0, 1.0, 2.0,
                1.0});

        Vector expected = createTestVector(new double[]{3.0, 0.0, 4.0, 6.0,
                6.0});

        assertEquals(expected, testVector.getSum(vector));

        testVector.add(vector);
        assertEquals(expected, testVector);
    }

    @Test
    void testAssignShouldFailGivenVectorWithDifferentLength()
    {
        Vector vector = createTestVector(new double[]{2.0, 0.0});

        assertThrows(ShapeMismatch.class, () -> testVector.assign(vector));
    }

    @Test
    void testAssignGivenVectorShouldOverwriteElementsAndOrientation() throws ShapeMismatch
    {
        Vector vector = createTestVector(new double[]{0.0, 0.0, 1.0, 0.0,
                0.0}, VectorOrientation.HORIZONTAL);

        testVector.assign(vector);

        assertEquals(vector, testVector);
    }

    @Test
    void testProductShouldFailGivenVectorOfSameOrientationButDifferentLengths()
    {
        Vector vector = createTestVector(new double[]{1.0, 0.0, 3.0});

        assertThrows(ShapeMismatch.class, () -> testVector.getProduct(vector));
    }

    @Test
    void testProductShouldReturnScalarProductGivenVectorOfSameOrientation() throws ShapeMismatch
    {
        Vector vector = createTestVector(new double[]{1.0, 2.0, 3.0, 3.0, 4.0});

        Scalar expected = createTestScalar(42.0);

        assertEquals(expected, testVector.getProduct(vector));
    }

    @Test
    void testProductWhenThisIsVerticalGivenHorizontalVectorShouldReturnVectorProduct() throws ShapeMismatch
    {
        Vector vector = createTestVector(new double[]{5.0, 6.0, 7.0},
                VectorOrientation.HORIZONTAL);

        Matrix expected = createTestMatrix(new double[][]{
                {5.0, 6.0, 7.0},
                {0.0, 0.0, 0.0},
                {15.0, 18.0, 21.0},
                {20.0, 24.0, 28.0},
                {25.0, 30.0, 35.0}
        });

        assertEquals(expected, testVector.getProduct(vector));
    }

    @Test
    void testProductWhenThisIsHorizontalGivenVerticalVectorWithDifferentLengthShouldFail()
    {
        Vector vector1 = createTestVector(new double[]{5.0, 6.0, 7.0},
                VectorOrientation.HORIZONTAL);

        assertThrows(ShapeMismatch.class, () -> vector1.getProduct(testVector));
    }

    @Test
    void testProductWhenThisIsHorizontalGivenVerticalVectorShouldReturnScalarProductAsMatrix() throws ShapeMismatch
    {
        Vector vector1 = createTestVector(new double[]{5.0, 6.0, 7.0, 8.0, 9.0},
                VectorOrientation.HORIZONTAL);

        Matrix expected = createTestMatrix(new double[][]{{103.0}});

        assertEquals(expected, vector1.getProduct(testVector));
    }

    @Test
    void testGetAndSetValueShouldFailGivenIncorrectAmountOfIndexes()
    {
        assertThrows(InvalidIndexes.class, () -> testVector.getValue());
        assertThrows(InvalidIndexes.class, () -> testVector.getValue(1, 2));

        assertThrows(InvalidIndexes.class, () -> testVector.setValue(2.0));
        assertThrows(InvalidIndexes.class, () -> testVector.setValue(2.0, 1, 2));
        assertEquals(createTestVector(new double[]{1.0, 0.0, 3.0, 4.0, 5.0}), testVector);
    }

    @Test
    void testGet() throws InvalidIndexes
    {
        assertEquals(3.0, testVector.get(2));
        assertEquals(3.0, testVector.getValue(2));
    }

    @Test
    void testSet() throws InvalidIndexes
    {
        testVector.setValue(9.0, 1);

        Vector expected1 = createTestVector(new double[]{1.0, 9.0, 3.0, 4.0,
                5.0});

        assertEquals(expected1, testVector);

        testVector.set(1, 10.0);

        Vector expected2 = createTestVector(new double[]{1.0, 10.0, 3.0, 4.0,
                5.0});

        assertEquals(expected2, testVector);

    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 100})
    void testGetAndSetShouldFailGivenInvalidRanges(int index)
    {
        assertThrows(InvalidIndexes.class, () -> testVector.get(index));
        assertThrows(InvalidIndexes.class, () -> testVector.getValue(index));

        assertThrows(InvalidIndexes.class, () -> testVector.set(index, 2.0));
        assertThrows(InvalidIndexes.class, () -> testVector.setValue(2.0, index));
        assertEquals(createTestVector(new double[]{1.0, 0.0, 3.0, 4.0, 5.0}), testVector);
    }

    @Test
    void testProductShouldFailGivenMatrixWithRowsCountNotEqualToThisLengthWhenThisIsHorizontal()
    {
        Vector vector = createTestVector(new double[]{1.0, 0.0, 3.0, 4.0}, VectorOrientation.HORIZONTAL);

        Matrix matrix = createTestMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0},
        });

        assertThrows(ShapeMismatch.class, () -> vector.getProduct(matrix));
    }

    @Test
    void testProductGivenMatrixWithRowsCountEqualToThisLengthWhenThisIsHorizontal() throws ShapeMismatch
    {
        Vector vector = createTestVector(new double[]{1.0, 0.0, 3.0, 4.0}, VectorOrientation.HORIZONTAL);

        Matrix matrix = createTestMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {1.0, 2.0, 3.0},
                {1.0, 2.0, 3.0},
                {1.0, 2.0, 3.0},
        });

        Vector expected = createTestVector(new double[]{8.0, 16.0, 24.0}, VectorOrientation.HORIZONTAL);

        assertEquals(expected, vector.getProduct(matrix));
    }

    @Test
    void testProductShouldFailGivenMatrixWithRowsCountNotEqualToOneWhenThisIsVertical()
    {
        Matrix matrix = createTestMatrix(new double[][]{
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0},
        });

        assertThrows(ShapeMismatch.class, () -> testVector.getProduct(matrix));
    }

    @Test
    void testProductGivenMatrixWithOneRowWhenThisIsVertical() throws ShapeMismatch
    {
        Matrix matrix = createTestMatrix(new double[][]{
                {7.0, 8.0, 9.0},
        });

        Matrix expected = createTestMatrix(new double[][]{
                {7.0, 8.0, 9.0},
                {0.0, 0.0, 0.0},
                {21.0, 24.0, 27.0},
                {28.0, 32.0, 36.0},
                {35.0, 40.0, 45.0}
        });

        assertEquals(expected, testVector.getProduct(matrix));
    }

    @Test
    void testToString()
    {
        Vector vector = createTestVector(new double[]{-2.3, 0.1, 5.1});

        String expected = """
                Orientation: vertical
                [-2.3, 0.1, 5.1]
                """;

        assertEquals(expected, vector.toString());
    }

    private Vector createTestVector(double[] values)
    {
        return createTestVector(values, VectorOrientation.VERTICAL);
    }

    private Vector createTestVector(double[] values, VectorOrientation orientation)
    {
        return new ArrayVector(values, orientation);
    }

    private Matrix createTestMatrix(double[][] values)
    {
        return new ArrayMatrix(values);
    }

    private Scalar createTestScalar(double value)
    {
        return new BasicScalar(value);
    }
}
