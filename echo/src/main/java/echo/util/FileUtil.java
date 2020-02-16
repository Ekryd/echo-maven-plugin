package echo.util;

import echo.exception.FailureException;
import echo.output.PluginLog;
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
    private static final String READING_INPUT_FROM = "Reading input from ";
    private final PluginLog mavenPluginLog;
    private final String encoding;
    private final String fromFile;
    private final File basePath;
    private final String toFile;
    private final boolean appendToFile;
    private final boolean forceOverwrite;

    /**
     * Create a new instance of the FileUtil
     *
     * @param parameters     The user-supplied plugin parameters
     * @param mavenPluginLog Wrapper for Maven internal plugin logger
     */
    public FileUtil(PluginParameters parameters, PluginLog mavenPluginLog) {
        this.mavenPluginLog = mavenPluginLog;
        this.encoding = parameters.getEncoding();
        this.fromFile = parameters.getFromFile();
        this.basePath = parameters.getBasePath();
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
        File saveFile = new File(basePath, toFile);
        String absolutePath = saveFile.getAbsolutePath();
        mavenPluginLog.info("Saving output to " + absolutePath);

        try {
            checkForNonWritableFile(saveFile);
            makeFileWritable(saveFile);
            FileUtils.write(saveFile, message, encoding, appendToFile);
        } catch (UnsupportedEncodingException | UnsupportedCharsetException ex) {
            throw new FailureException(UNSUPPORTED_ENCODING + ex.getMessage(), ex);
        } catch (IOException ex) {
            mavenPluginLog.debug(ex);
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
        UrlWrapper urlWrapper = new UrlWrapper(fromFile);
        try (InputStream inputStream = urlWrapper.isUrl() ?
                urlWrapper.openStream() : getFileFromRelativeOrClassPath(basePath, fromFile)) {
            return IOUtils.toString(inputStream, encoding);
        } catch (UnsupportedEncodingException | UnsupportedCharsetException ex) {
            throw new FailureException(UNSUPPORTED_ENCODING + ex.getMessage(), ex);
        }
    }

    private InputStream getFileFromRelativeOrClassPath(File basePath, String file) throws IOException {
        FindFileInAbsolutePath findFileInAbsolutePath = new FindFileInAbsolutePath(mavenPluginLog);

        findFileInAbsolutePath.openFile(new File(file));
        if (findFileInAbsolutePath.isFound()) {
            mavenPluginLog.debug(READING_INPUT_FROM + findFileInAbsolutePath.getAbsoluteFilePath());

            return findFileInAbsolutePath.getInputStream();
        }

        findFileInAbsolutePath.openFile(new File(basePath, file));
        if (findFileInAbsolutePath.isFound()) {
            mavenPluginLog.debug(READING_INPUT_FROM + findFileInAbsolutePath.getAbsoluteFilePath());

            return findFileInAbsolutePath.getInputStream();
        }

        FindFileInClassPath findFileInClassPath = new FindFileInClassPath(mavenPluginLog);
        findFileInClassPath.openFile(file);
        if (findFileInClassPath.isFound()) {
            mavenPluginLog.debug(READING_INPUT_FROM + findFileInClassPath.getAbsoluteFilePath());

            return findFileInClassPath.getInputStream();
        }

        throw new FileNotFoundException(String.format("Could not find %s, %s or %s in classpath",
                new File(file).getAbsolutePath(),
                new File(basePath, file).getAbsolutePath(),
                file));
    }
}
