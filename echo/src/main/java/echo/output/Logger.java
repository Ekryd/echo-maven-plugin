package echo.output;

/**
 * @author bjorn
 * @since 2012-12-21
 */
public interface Logger {
    /**
     * Send a message to the log in the <b>info</b> error level.
     *
     * @param content info message
     */
    void info(String content);
}
