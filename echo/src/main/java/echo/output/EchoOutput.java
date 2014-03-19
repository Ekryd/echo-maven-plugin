package echo.output;

/**
 * Output a message to standard output with a specific level
 *
 * @author bjorn
 * @since 2013-09-09
 */
public interface EchoOutput {
    /** The message will be output with an failure level (exception will occur) */
    void fail(String content);

    /** The message will be output with error level */
    void error(String content);

    /** The message will be output with warning level */
    void warning(String content);

    /** The message will be output with info level (default level) */
    void info(String content);

    /** The message will be output with debug level (maven debugging must be turned on to see message) */
    void debug(String content);
}
