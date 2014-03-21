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
    private final Log log;

    /** Creates an MavenEchoOutput wrapper */
    public MavenEchoOutput(Log log) {
        this.log = log;
    }

    @Override
    public void fail(String content) {
        this.<RuntimeException>throwAsUnchecked(new MojoFailureException(content));
    }

    @Override
    public void error(String content) {
        log.error(content);
    }

    @Override
    public void warning(String content) {
        log.warn(content);
    }

    @Override
    public void info(String content) {
        log.info(content);
    }

    @Override
    public void debug(String content) {
        log.debug(content);
    }

    @SuppressWarnings("unchecked")
    private <E extends Throwable> void throwAsUnchecked(Exception e) throws E {
        throw (E) e;
    }
}
