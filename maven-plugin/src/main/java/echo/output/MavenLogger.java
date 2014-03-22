package echo.output;

import org.apache.maven.plugin.logging.Log;

/**
 * Wraps the Maven internal plugin logger to decouple from logging framework
 *
 * @author bjorn
 * @since 2012-12-22
 */
public class MavenLogger implements Logger {
    private final Log log;

    /** Create new MavenLogger wrapper */
    public MavenLogger(Log log) {
        this.log = log;
    }

    @Override
    public void info(String content) {
        log.info(content);
    }

    @Override
    public void debug(Throwable throwable) {
        if (log.isDebugEnabled()) {
            log.debug(throwable);
        }
    }

    @Override
    public void debug(String content) {
        if (log.isDebugEnabled()) {
            log.debug(content);
        }
    }
}
