package echo;

import echo.exception.FailureException;
import echo.output.NewlineFormatter;
import echo.parameter.PluginParameters;
import echo.util.FileUtil;

import java.io.IOException;

/**
 * Retrieves the message to output either from file or as an input parameter. The message is also
 * formatted correctly.
 *
 * @author bjorn
 * @since 2013-09-23
 */
class MessageExtractor {
    private final FileUtil fileUtil;
    private final NewlineFormatter newlineFormatter;

    private final String message;
    private final String fromFile;
    private String originalMessage;

    /**
     * Create a new instance of the MessageExtractor
     *
     * @param pluginParameters The user-supplied plugin parameters
     * @param fileUtil         File system interaction class
     */
    public MessageExtractor(PluginParameters pluginParameters, FileUtil fileUtil) {
        this.fileUtil = fileUtil;
        this.newlineFormatter = new NewlineFormatter(pluginParameters);

        this.message = pluginParameters.getMessage();
        this.fromFile = pluginParameters.getFromFile();
    }

    /** Returns a message the is ready for output */
    public String getFormattedMessage() {
        extractMessage();
        
        return newlineFormatter.format(originalMessage);
    }

    private void extractMessage() {
        checkMissingMessage();
        if (message != null) {
            originalMessage = message;
        } else {
            try {
                originalMessage = fileUtil.getFromFile();
            } catch (IOException ex) {
                throw new FailureException(ex.getMessage(), ex);
            }
        }
    }

    private void checkMissingMessage() {
        if (message == null && fromFile == null) {
            throw new FailureException("There was nothing to output. Specify either message or fromFile");
        }
        if (message != null && fromFile != null) {
            throw new FailureException("Specify either message or fromFile, not both");
        }
    }

    public String getOriginalMessage() {
        return originalMessage;
    }
}
