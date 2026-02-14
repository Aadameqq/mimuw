import lib.*;

public class Main
{
    public static void main(String[] args)
    {
        Scalar testScalar = new BasicScalar(-0.3);

        System.out.println(testScalar);

        Vector vector = new ArrayVector(new double[]{1, 2, 3, 4, 5}, VectorOrientation.VERTICAL);
        System.out.println(vector);

        AbstractMatrix matrix = new ArrayMatrix(new double[][]{
                {1.0, 0.0, 2.0},
                {2.0, 1.0, 3.0},
                {1.0, 1.0, 1.0},
                {2.0, 3.0, 1.0}
        });
        System.out.println(matrix);

        try
        {
            Matrix slice = matrix.createSlice(1, 2, 1, 2);
            System.out.println(slice);
        }
        catch (InvalidIndexes e)
        {
            System.exit(1);
        }

        System.out.println(matrix.getSum(new BasicScalar(1)).getSum(new BasicScalar(2)).getSum(new BasicScalar(3)));
    }
}
