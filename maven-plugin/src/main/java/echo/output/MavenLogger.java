package echo.output;

import org.apache.maven.plugin.logging.Log;

/**
 * @author bjorn
 * @since 2012-12-22
 */
public class MavenLogger implements Logger {
    private final Log log;

    public MavenLogger(Log log) {
        this.log = log;
    }

    @Override
    public void info(String content) {
        log.info(content);
    }
}
