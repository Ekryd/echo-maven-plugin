package echo.util;

import echo.exception.FailureException;
import echo.output.Logger;
import echo.parameter.PluginParameters;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URL;

/**
 * Used to interface with file system
 *
 * @author Bjorn
 */
public class FileUtil {
    private final Logger mavenLogger;
    private final String encoding;
    private final String fromFile;
    private final String toFile;
    private final boolean appendToFile;
    private final boolean forceOverwrite;

    public FileUtil(PluginParameters parameters, Logger mavenLogger) {
        this.mavenLogger = mavenLogger;
        this.encoding = parameters.encoding;
        this.fromFile = parameters.fromFile;
        this.toFile = parameters.toFile;
        this.appendToFile = parameters.appended;
        this.forceOverwrite = parameters.force;
    }

//    private void checkBackupFileAccess() {
//        if (backupFile.exists() && !backupFile.delete()) {
//            throw new FailureException("Could not remove old backup file, filename: " + newName);
//        }
//    }

//    private void createBackupFile() {
//        FileInputStream source = null;
//        FileOutputStream newFile = null;
//        try {
//            source = new FileInputStream(pomFile);
//            newFile = new FileOutputStream(backupFile);
//            IOUtils.copy(source, newFile);
//        } catch (IOException e) {
//            throw new FailureException("Could not create backup file to filename: " + newName, e);
//        } finally {
//            IOUtils.closeQuietly(newFile);
//            IOUtils.closeQuietly(source);
//        }
//    }


    /**
     * Saves text output
     *
     * @param message The text to save
     */
    public void saveToFile(final String message) {
        File saveFile = new File(toFile);
        String absolutePath = saveFile.getAbsolutePath();
        mavenLogger.info("Saving output to " + absolutePath);
        
        try {
            modifyFileIfNonWritable(saveFile);
            checkForNonWriteableFile(saveFile);
            FileUtils.write(saveFile, message, encoding, appendToFile);
        } catch (IOException e) {
            throw new FailureException("Could not save file: " + absolutePath, e);
        } 
    }

    private void modifyFileIfNonWritable(File saveFile) {
            if (saveFile.isFile() && saveFile.exists() && !saveFile.canWrite()) {
                if (forceOverwrite) {
                    saveFile.setWritable(true);
                } else {
                    throw new FailureException("Cannot write to read-only file " + saveFile.getAbsolutePath());
                }
            }
    }

    private void checkForNonWriteableFile(File saveFile) {
        if (saveFile.isDirectory()) {
            throw new FailureException("File "+saveFile.getAbsolutePath()+" exists but is a directory");
        }
    }

    /**
     * Retrieves the default sort order for sortpom
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
            String output = IOUtils.toString(inputStream, encoding);
            return output;
        } catch (UnsupportedEncodingException ex) {
            throw new FailureException("Unsupported encoding: " + ex.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    private InputStream getFileFromRelativeOrClassPath(String file) throws IOException {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException fnfex) {
            // try classpath
            try {
                URL resource = this.getClass().getClassLoader().getResource(file);
                if (resource == null) {
                    throw new IOException("Cannot find resource");
                }
                inputStream = resource.openStream();
            } catch (IOException e1) {
                throw new FileNotFoundException(String.format("Could not find %s or %s in classpath", new File(
                        file).getAbsolutePath(), file));
            }
        }
        return inputStream;
    }

}
