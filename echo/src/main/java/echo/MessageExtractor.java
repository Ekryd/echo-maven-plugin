package echo;

import echo.exception.FailureException;
import echo.output.NewlineFormatter;
import echo.parameter.PluginParameters;
import echo.util.FileUtil;

import java.io.IOException;

/**
 * @author bjorn
 * @since 2013-09-23
 */
public class MessageExtractor {
    private final FileUtil fileUtil;
    private final NewlineFormatter newlineFormatter;
    
    private final String message;
    private final String fromFile;

    public MessageExtractor(PluginParameters pluginParameters, FileUtil fileUtil) {
        this.fileUtil = fileUtil;
        this.newlineFormatter = new NewlineFormatter(pluginParameters);
        
        this.message = pluginParameters.message;
        this.fromFile = pluginParameters.fromFile;
    }

    public String getFormattedMessage() {
        return newlineFormatter.format(extractMessage());
    }

    private String extractMessage() {
        checkMissingMessage();
        if (message != null) {
            return message;
        }
        try {
            return fileUtil.getFromFile();
        } catch (IOException ex) {
            throw new FailureException(ex.getMessage(), ex);
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

}
