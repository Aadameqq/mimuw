import lib.Array;
import lib.BasicScalar;
import lib.InvalidIndexes;
import lib.Scalar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ScalarTests
{
    private Scalar testScalar;

    @BeforeEach
    void setUp()
    {
        testScalar = createTestScalar(2.3);
    }

    @Test
    void testNegation()
    {
        Scalar expected = createTestScalar(-2.3);

        assertEquals(expected, testScalar.getNegation());

        testScalar.negate();
        assertEquals(expected, testScalar);
    }

    @Test
    void testNegationShouldReturnZeroGivenZeroScalar()
    {
        Scalar test = createTestScalar(0);
        Scalar expected = createTestScalar(0);

        assertEquals(expected, test.getNegation());

        test.negate();
        assertEquals(expected, test);
    }

    @Test
    void testAssignShouldChangeValueGivenScalar()
    {
        Scalar test = createTestScalar(-5.3);

        testScalar.assign(test);
        assertEquals(test, testScalar);
    }

    @Test
    void testCopyShouldCreateCopyEqualToOriginal()
    {
        Array copy = testScalar.getCopy();

        assertEquals(testScalar, copy);
    }

    @Test
    void testSliceChangeShouldChangeSource()
    {
        Scalar slice = testScalar.createSlice();
        Scalar test = createTestScalar(10);

        slice.assign(test);

        assertEquals(test, testScalar);
    }

    @Test
    void testSliceShouldReflectSourceChange()
    {
        Scalar slice = testScalar.createSlice();
        Scalar test = createTestScalar(10);

        testScalar.assign(test);

        assertEquals(test, slice);
    }

    @ParameterizedTest
    @CsvSource({"-5.3, 5.3, 0", "2.1, 3.2, 5.3"})
    void testSumWithScalar(double first, double second, double result)
    {
        Scalar scalar1 = createTestScalar(first);
        Scalar scalar2 = createTestScalar(second);

        Scalar expected = createTestScalar(result);

        assertEquals(expected, scalar1.getSum(scalar2));

        scalar1.add(scalar2);

        assertEquals(expected, scalar1);
    }

    @ParameterizedTest
    @CsvSource({
            "2.0, 3.0, 6.0",
            "2.0, -3.0, -6.0",
            "4.0, 0, 0"
    })
    void testProductWithScalar(double first, double second, double result)
    {
        Scalar scalar1 = createTestScalar(first);
        Scalar scalar2 = createTestScalar(second);

        Scalar expected = createTestScalar(result);

        assertEquals(expected, scalar1.getProduct(scalar2));

        scalar1.multiplyBy(scalar2);

        assertEquals(expected, scalar1);
    }

    @Test
    void testToString()
    {
        assertEquals("[2.3]\n", testScalar.toString());
    }

    @Test
    void testGetValueShouldFailGivenIncorrectAmountOfIndexes()
    {
        assertThrows(InvalidIndexes.class, () -> testScalar.getValue(1));
    }

    @Test
    void testSetValueShouldFailGivenIncorrectAmountOfIndexes()
    {
        assertThrows(InvalidIndexes.class, () -> testScalar.setValue(2.0, 1));
    }

    @Test
    void testSetValueShouldChangeScalarValue() throws InvalidIndexes
    {
        testScalar.setValue(6.0);
        assertEquals(createTestScalar(6.0), testScalar);
    }

    private Scalar createTestScalar(double value)
    {
        return new BasicScalar(value);
    }
}
