package echo.util;

import echo.output.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Try to retrieve content from the java class path. Usually placed under src/main/resources
 *
 * @author bjorn
 * @since 2014-03-19
 */
public class FindFileInClassPath {

    private final Logger mavenLogger;

    private InputStream inputStream;

    /**
     * Creates a new instance of the class
     *
     * @param mavenLogger Wrapper for Maven internal plugin logger
     */
    public FindFileInClassPath(Logger mavenLogger) {
        this.mavenLogger = mavenLogger;
    }

    /** Try to open a stream to the file location in the class path */
    public void openFile(String fileName) {
        try {
            URL resource = this.getClass().getClassLoader().getResource(fileName);
            if (resource != null) {
                inputStream = resource.openStream();
            }
        } catch (IOException iex) {
            mavenLogger.debug(iex);
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
}
