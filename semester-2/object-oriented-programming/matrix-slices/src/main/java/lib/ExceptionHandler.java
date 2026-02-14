package lib;

public class ExceptionHandler
{
    public static void handle(String message, Exception e)
    {
        System.err.println(message);
        e.printStackTrace(System.err);
        System.exit(1);
    }
}
