package echo.output;

import org.apache.maven.plugin.logging.Log;

/**
 * Wraps the Maven internal plugin logger to decouple from logging framework
 *
 * @author bjorn
 * @since 2012-12-22
 */
public class MavenPluginLog implements PluginLog {
    private final Log wrappedLog;

    /** Create new MavenLogger wrapper */
    public MavenPluginLog(Log wrappedLog) {
        this.wrappedLog = wrappedLog;
    }

    @Override
    public void info(String content) {
        wrappedLog.info(content);
    }

    @Override
    public void debug(Throwable throwable) {
        if (wrappedLog.isDebugEnabled()) {
            wrappedLog.debug(throwable);
        }
    }

    @Override
    public void debug(String content) {
        if (wrappedLog.isDebugEnabled()) {
            wrappedLog.debug(content);
        }
    }
}
