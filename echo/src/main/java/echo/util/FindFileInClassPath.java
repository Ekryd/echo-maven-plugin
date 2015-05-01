package echo.util;

import echo.output.PluginLog;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Try to retrieve content from the java class path. Usually placed under src/main/resources
 *
 * @author bjorn
 * @since 2014-03-19
 */
class FindFileInClassPath {

    private final PluginLog mavenPluginLog;

    private InputStream inputStream;
    private String absoluteFilePath;

    /**
     * Creates a new instance of the class
     *
     * @param mavenPluginLog Wrapper for Maven internal plugin logger
     */
    public FindFileInClassPath(PluginLog mavenPluginLog) {
        this.mavenPluginLog = mavenPluginLog;
    }

    /** Try to open a stream to the file location in the class path */
    public void openFile(String fileName) {
        try {
            URL resource = this.getClass().getClassLoader().getResource(fileName);
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
