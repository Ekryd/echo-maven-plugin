package echo.util;

import echo.output.MavenPluginLog;
import java.io.IOException;
import java.io.InputStream;

/** Try to retrieve content from the java class path. Usually placed under src/main/resources */
class FindFileInClassPath {

  private final MavenPluginLog mavenPluginLog;

  private InputStream inputStream;
  private String absoluteFilePath;

  /**
   * Creates a new instance of the class
   *
   * @param mavenPluginLog Wrapper for Maven internal plugin logger
   */
  public FindFileInClassPath(MavenPluginLog mavenPluginLog) {
    this.mavenPluginLog = mavenPluginLog;
  }

  /** Try to open a stream to the file location in the class path */
  public void openFile(String fileName) {
    try {
      var resource = this.getClass().getClassLoader().getResource(fileName);
      if (resource != null) {
        this.inputStream = resource.openStream();
        this.absoluteFilePath = resource.getPath();
      }
    } catch (IOException iex) {
      mavenPluginLog.debug(iex);
    }
  }

  /** Return true if stream is opened to file content */
  public boolean isFound() {
    return inputStream != null;
  }

  /** Return stream to file content */
  public InputStream getInputStream() {
    return inputStream;
  }

  public String getAbsoluteFilePath() {
    return absoluteFilePath;
  }
}
