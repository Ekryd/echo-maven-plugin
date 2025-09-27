package echo.output;

import org.apache.maven.plugin.logging.Log;

/** Wraps the Maven internal plugin logger to decouple from logging framework */
public class MavenPluginLog {
  private final Log wrappedLog;

  /** Create new MavenLogger wrapper */
  public MavenPluginLog(Log wrappedLog) {
    this.wrappedLog = wrappedLog;
  }

  /**
   * Send a message to the log in the <b>info</b> level.
   *
   * @param content info message
   */
  public void info(String content) {
    wrappedLog.info(content);
  }

  /**
   * Log the throwable to the debug level.
   *
   * @param throwable the "exception" to log
   */
  public void debug(Throwable throwable) {
    if (wrappedLog.isDebugEnabled()) {
      wrappedLog.debug(throwable);
    }
  }

  /** Log the content to the debug level. */
  public void debug(String content) {
    if (wrappedLog.isDebugEnabled()) {
      wrappedLog.debug(content);
    }
  }
}
