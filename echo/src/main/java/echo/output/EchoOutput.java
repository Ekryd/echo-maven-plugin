package echo.output;

/**
 * @author bjorn
 * @since 2013-09-09
 */
public interface EchoOutput {
    void fail(String content);

    void error(String content);

    void warning(String content);

    void info(String content);

    void debug(String content);
}
