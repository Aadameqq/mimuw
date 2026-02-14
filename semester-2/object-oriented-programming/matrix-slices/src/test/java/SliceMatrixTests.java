import lib.Array;
import lib.ArrayMatrix;
import lib.InvalidIndexes;
import lib.Matrix;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SliceMatrixTests
{
    private Matrix testSource;
    private Matrix testSlice;

    @BeforeEach
    void setUp() throws InvalidIndexes
    {
        testSource = createTestMatrix(
                new double[][]{
                        {1, 2, 3, 4},
                        {5, 6, 7, 8},
                        {9, 10, 11, 12}
                }
        );
        testSlice = testSource.createSlice(1, 2, 1, 3);
    }

    @Test
    void testSliceShouldHaveCorrectShapeAndContent()
    {
        Matrix expected = createTestMatrix(
                new double[][]{
                        {6, 7, 8},
                        {10, 11, 12}
                }
        );

        assertEquals(expected, testSlice);
    }

    @Test
    void testSliceChangeShouldChangeSource() throws InvalidIndexes
    {
        testSlice.set(0, 0, 100);

        assertEquals(100, testSource.get(1, 1));
    }

    @Test
    void testSliceShouldChangeWhenSourceChanges() throws InvalidIndexes
    {
        testSource.set(1, 2, 100);

        assertEquals(100, testSlice.get(0, 1));
    }

    @Test
    void testSliceShouldNotChangeWhenSourceIsTransposed()
    {
        Matrix expected = createTestMatrix(new double[][]{
                {6, 7, 8},
                {10, 11, 12}
        });

        testSource.transpose();

        assertEquals(expected, testSlice);
    }

    @Test
    void testTransposition()
    {
        Matrix expected = createTestMatrix(
                new double[][]{
                        {6, 10},
                        {7, 11},
                        {8, 12}
                }
        );

        testSlice.transpose();

        assertEquals(expected, testSlice);
    }

    @Test
    void testTransposeShouldSliceNotChangeWhenTransposedTwice()
    {
        Matrix expected = createTestMatrix(
                new double[][]{
                        {6, 7, 8},
                        {10, 11, 12}
                }
        );

        testSlice.transpose();
        testSlice.transpose();

        assertEquals(expected, testSlice);
    }

    @Test
    void testCopyShouldCreateCopyThatReflectsSourceChanges() throws InvalidIndexes
    {
        Array copy = testSlice.getCopy();
        testSource.set(1, 1, 100);

        Matrix expected = createTestMatrix(
                new double[][]{
                        {100, 7, 8},
                        {10, 11, 12}
                }
        );
        assertEquals(expected, copy);
    }

    @Test
    void testCreateSliceShouldCreateSliceThatReflectsSourceChanges() throws InvalidIndexes
    {
        Matrix secondSlice = testSlice.createSlice(1, 1, 0, 0);
        testSource.set(2, 1, 100);

        assertEquals(100, secondSlice.get(0, 0));
    }

    @ParameterizedTest
    @CsvSource({
            "-1, 2, 1, 2",
            "2, 1, 1, 2",
            "1, 2, 3, 2",
            "1, 8, 1, 2",
            "1, 2, 1, 8",
    })
    void testCreateInstanceShouldFailGivenInvalidRanges(
            int startRow,
            int endRow,
            int startColumn,
            int endColumn
    )
    {
        assertThrows(InvalidIndexes.class,
                () -> testSource.createSlice(startRow, endRow, startColumn, endColumn));
    }

    private Matrix createTestMatrix(double[][] array)
    {
        return new ArrayMatrix(array);
    }
}
