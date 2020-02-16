package echo.output;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;

/**
 * Wraps the Maven standard output to decouple from output functionality
 *
 * @author bjorn
 * @since 2013-09-09
 */
public class MavenEchoOutput implements EchoOutput {
    private final Log wrappedLog;

    /** Creates an MavenEchoOutput wrapper */
    public MavenEchoOutput(Log wrappedLog) {
        this.wrappedLog = wrappedLog;
    }

    @Override
    public void fail(String content) {
        this.throwAsUnchecked(new MojoFailureException(content));
    }

    @Override
    public void error(String content) {
        wrappedLog.error(content);
    }

    @Override
    public void warning(String content) {
        wrappedLog.warn(content);
    }

    @Override
    public void info(String content) {
        wrappedLog.info(content);
    }

    @Override
    public void debug(String content) {
        wrappedLog.debug(content);
    }

    @SuppressWarnings("unchecked")
    private <E extends Throwable> void throwAsUnchecked(Exception e) throws E {
        throw (E) e;
    }
}
