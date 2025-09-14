package echo.output;

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;

/** Wraps the Maven standard output to decouple from output functionality */
public class MavenEchoOutput {
  private final Log wrappedLog;

  /** Creates an MavenEchoOutput wrapper */
  public MavenEchoOutput(Log wrappedLog) {
    this.wrappedLog = wrappedLog;
  }

  /** The message will be output with a failure level (exception will occur) */
  public void fail(String content) {
    this.throwAsUnchecked(new MojoFailureException(content));
  }

  /** The message will be output with error level */
  public void error(String content) {
    wrappedLog.error(content);
  }

  /** The message will be output with warning level */
  public void warning(String content) {
    wrappedLog.warn(content);
  }

  /** The message will be output with info level (default level) */
  public void info(String content) {
    wrappedLog.info(content);
  }

  /**
   * The message will be output with debug level (maven debugging must be turned on to see message)
   */
  public void debug(String content) {
    wrappedLog.debug(content);
  }

  @SuppressWarnings("unchecked")
  private <E extends Throwable> void throwAsUnchecked(Exception e) throws E {
    throw (E) e;
  }
}
