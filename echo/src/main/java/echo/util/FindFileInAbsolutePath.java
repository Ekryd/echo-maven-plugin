package echo.util;

import echo.output.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Try to retrieve content from an absolute file path. Example /usr/bin/content.txt
 *
 * @author bjorn
 * @since 2014-03-19
 */
class FindFileInAbsolutePath {

    private final Logger mavenLogger;

    private FileInputStream inputStream;
    private String absoluteFilePath;

    /**
     * Creates a new instance of the class
     *
     * @param mavenLogger Wrapper for Maven internal plugin logger
     */
    public FindFileInAbsolutePath(Logger mavenLogger) {
        this.mavenLogger = mavenLogger;
    }

    /** Try to open a stream to the file location */
    public void openFile(File absoluteFilePath) {
        try {
            this.inputStream = new FileInputStream(absoluteFilePath);
            this.absoluteFilePath = absoluteFilePath.getAbsolutePath();
        } catch (FileNotFoundException fex) {
            mavenLogger.debug(fex);
            inputStream = null;
        }
    }

    /** Return true if stream is opened to file path */
    public boolean isFound() {
        return inputStream != null;
    }

    public String getAbsoluteFilePath() {
        return absoluteFilePath;
    }

    /** Return stream to file path content */
    public FileInputStream getInputStream() {
        return inputStream;
    }


}
