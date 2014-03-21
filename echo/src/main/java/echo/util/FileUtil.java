package echo.util;

import echo.exception.FailureException;
import echo.output.Logger;
import echo.parameter.PluginParameters;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.UnsupportedCharsetException;

import static echo.exception.FailureException.UNSUPPORTED_ENCODING;

/**
 * Used to interface with file system
 *
 * @author Bjorn
 */
public class FileUtil {
    private final Logger mavenLogger;
    private final String encoding;
    private final String fromFile;
    private final File defaultOutputPath;
    private final String toFile;
    private final boolean appendToFile;
    private final boolean forceOverwrite;

    /**
     * Create a new instace of the FileUtil
     *
     * @param parameters  The user-supplied plugin parameters
     * @param mavenLogger Wrapper for Maven internal plugin logger
     */
    public FileUtil(PluginParameters parameters, Logger mavenLogger) {
        this.mavenLogger = mavenLogger;
        this.encoding = parameters.getEncoding();
        this.fromFile = parameters.getFromFile();
        this.defaultOutputPath = parameters.getDefaultOutputPath();
        this.toFile = parameters.getToFile();
        this.appendToFile = parameters.isAppendToFile();
        this.forceOverwrite = parameters.isForce();
    }

    /**
     * Saves text output
     *
     * @param message The text to save
     */
    public void saveToFile(final String message) {
        File saveFile = new File(defaultOutputPath, toFile);
        String absolutePath = saveFile.getAbsolutePath();
        mavenLogger.info("Saving output to " + absolutePath);

        try {
            checkForNonWritableFile(saveFile);
            makeFileWritable(saveFile);
            FileUtils.write(saveFile, message, encoding, appendToFile);
        } catch (UnsupportedEncodingException ex) {
            throw new FailureException(UNSUPPORTED_ENCODING + ex.getMessage(), ex);
        } catch (UnsupportedCharsetException ex) {
            throw new FailureException(UNSUPPORTED_ENCODING + ex.getMessage(), ex);
        } catch (IOException ex) {
            mavenLogger.debug(ex);
            throw new FailureException("Could not save file: " + absolutePath, ex);
        }
    }

    private void checkForNonWritableFile(File saveFile) {
        if (saveFile.isDirectory()) {
            throw new FailureException("File " + saveFile.getAbsolutePath() + " exists but is a directory");
        }
    }

    private void makeFileWritable(File saveFile) {
        if (saveFile.isFile() && saveFile.exists() && !saveFile.canWrite()) {
            if (forceOverwrite) {
                boolean writableStatus = saveFile.setWritable(true);
                if (!writableStatus) {
                    throw new FailureException("Could not make file writable " + saveFile.getAbsolutePath());
                }
            } else {
                throw new FailureException("Cannot write to read-only file " + saveFile.getAbsolutePath());
            }
        }
    }

    /**
     * Retrieves the message from the location in attribute fromFile
     *
     * @return Content of the default sort order file
     */
    public String getFromFile() throws IOException {
        InputStream inputStream = null;
        try {
            UrlWrapper urlWrapper = new UrlWrapper(fromFile);
            if (urlWrapper.isUrl()) {
                inputStream = urlWrapper.openStream();
            } else {
                inputStream = getFileFromRelativeOrClassPath(fromFile);
            }
            return IOUtils.toString(inputStream, encoding);
        } catch (UnsupportedEncodingException ex) {
            throw new FailureException(UNSUPPORTED_ENCODING + ex.getMessage(), ex);
        } catch (UnsupportedCharsetException ex) {
            throw new FailureException(UNSUPPORTED_ENCODING + ex.getMessage(), ex);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    private InputStream getFileFromRelativeOrClassPath(String file) throws IOException {
        FindFileInAbsolutePath findFileInAbsolutePath = new FindFileInAbsolutePath(mavenLogger);
        findFileInAbsolutePath.openFile(file);
        if (findFileInAbsolutePath.isFound()) {
            return findFileInAbsolutePath.getInputStream();
        }

        FindFileInClassPath findFileInClassPath = new FindFileInClassPath(mavenLogger);
        findFileInClassPath.openFile(file);
        if (findFileInClassPath.isFound()) {
            return findFileInClassPath.getInputStream();
        }

        throw new FileNotFoundException(String.format("Could not find %s or %s in classpath",
                new File(file).getAbsolutePath(), file));
    }
}
