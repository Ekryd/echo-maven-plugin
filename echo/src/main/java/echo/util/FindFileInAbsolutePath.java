package echo.util;

import echo.output.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Try to retrieve content from an absolute file path. Example /usr/bin/content.txt
 *
 * @author bjorn
 * @since 2014-03-19
 */
public class FindFileInAbsolutePath {

    private final Logger mavenLogger;

    private FileInputStream inputStream;

    public FindFileInAbsolutePath(Logger mavenLogger) {
        this.mavenLogger = mavenLogger;
    }

    /** Try to open a stream to the file location */
    public void openFile(String absoluteFilePath) {
        try {
            inputStream = new FileInputStream(absoluteFilePath);
        } catch (FileNotFoundException fex) {
            mavenLogger.debug(fex);
            inputStream = null;
        }
    }

    /** Return true if stream is opened to file path */
    public boolean isFound() {
        return inputStream != null;
    }

    /** Return stream to file path content */
    public FileInputStream getInputStream() {
        return inputStream;
    }
}
