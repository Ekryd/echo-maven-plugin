package echo.exception;

/**
 * An exception occurring during the execution of the plugin.
 * <br/>
 * Throwing this exception should cause a "BUILD FAILURE" message to be displayed in Maven.
 *
 * @author bjorn
 * @since 2012-12-21
 */
public class FailureException extends RuntimeException {
    public static final String UNSUPPORTED_ENCODING = "Unsupported encoding: ";

    public FailureException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public FailureException(String msg) {
        super(msg);
    }
}
