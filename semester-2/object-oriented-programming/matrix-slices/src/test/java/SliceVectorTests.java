import lib.ArrayVector;
import lib.InvalidIndexes;
import lib.Vector;
import lib.VectorOrientation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SliceVectorTests
{
    private Vector testHorizontalVector;

    @BeforeEach
    void setUp()
    {
        testHorizontalVector = createTestVector(new double[]{1, 2, 3, 4, 5}, VectorOrientation.HORIZONTAL);
    }

    @Test
    void testChangeShouldChangeSource() throws InvalidIndexes
    {
        Vector slice = testHorizontalVector.createSlice(1, 3);

        slice.set(0, 100);

        assertEquals(100, testHorizontalVector.get(1));
    }

    @Test
    void testShouldReflectSourceChange() throws InvalidIndexes
    {
        Vector slice = testHorizontalVector.createSlice(1, 3);

        testHorizontalVector.set(1, 100);

        assertEquals(100, slice.get(0));
    }

    @Test
    void testShouldNotChangeWhenSourceIsTransposed() throws InvalidIndexes
    {
        Vector slice = testHorizontalVector.createSlice(1, 3);

        testHorizontalVector.transpose();

        Vector expected = createTestVector(new double[]{2, 3, 4}, VectorOrientation.HORIZONTAL);

        assertEquals(expected, slice);
    }

    @Test
    void testTransposition() throws InvalidIndexes
    {
        Vector slice = testHorizontalVector.createSlice(1, 3);

        slice.transpose();

        Vector expected = createTestVector(new double[]{2, 3, 4}, VectorOrientation.VERTICAL);

        assertEquals(expected, slice);
    }

    @ParameterizedTest
    @CsvSource({
            "-1, 3",
            "4, 3",
            "1, 8"
    })
    void testCreateInstanceShouldFailGivenInvalidRange(
            int startRow,
            int endRow
    )
    {
        assertThrows(InvalidIndexes.class,
                () -> testHorizontalVector.createSlice(startRow, endRow));
    }

    private Vector createTestVector(double[] array, VectorOrientation orientation)
    {
        return new ArrayVector(array, orientation);
    }
}
