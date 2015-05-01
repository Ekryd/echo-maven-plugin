package echo.output;

/**
 * Interface for the internal Maven plugin logger
 *
 * @author bjorn
 * @since 2012-12-21
 */
public interface PluginLog {
    /**
     * Send a message to the log in the <b>info</b> level.
     *
     * @param content info message
     */
    void info(String content);

    /**
     * Log the throwable to the debug level.
     *
     * @param throwable the "exception" to log
     */
    void debug(Throwable throwable);

    /** Log the content to the debug level. */
    void debug(String content);
}
