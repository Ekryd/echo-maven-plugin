package echo.exception;

import org.apache.maven.plugin.MojoFailureException;

/** Converts FailureExceptions to MojoFailureExceptions. The MojoFailureException will stop the maven build */
public final class ExceptionHandler {
    private final FailureException fex;

    /** Creates an ExceptionHandler with the exception to handle */
    public ExceptionHandler(FailureException fex) {
        this.fex = fex;
    }

    /** Throw a MojoFailureException from the failure exception */
    public void throwMojoFailureException() throws MojoFailureException {
        if (fex.getCause() != null) {
            throw new MojoFailureException(fex.getMessage(), fex.getCause());
        } else {
            throw new MojoFailureException(fex.getMessage());
        }
    }
}