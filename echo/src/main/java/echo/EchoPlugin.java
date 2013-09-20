package echo;

import echo.exception.FailureException;
import echo.output.EchoOutput;
import echo.output.EchoOutputWrapper;
import echo.output.Logger;
import echo.parameter.PluginParameters;
import echo.util.FileUtil;

import java.io.IOException;

/**
 * @author bjorn
 * @since 2013-08-08
 */
public class EchoPlugin {
    private Logger mavenLogger;
    private PluginParameters pluginParameters;
    private EchoOutputWrapper echoOutput;
    private FileUtil fileUtil;

    public void setup(Logger mavenLogger, PluginParameters pluginParameters, EchoOutput echoOutput) {
        this.mavenLogger = mavenLogger;
        this.pluginParameters = pluginParameters;
        this.echoOutput = new EchoOutputWrapper(echoOutput, pluginParameters.level);
        this.fileUtil = new FileUtil(pluginParameters);
    }

    public void echo() {
        String message = extractMessage();
        if (pluginParameters.characterOutput) {
            String characterArray = new CharacterOutput(message).getOutput();
            mavenLogger.info(characterArray);
        }
        echoOutput.output(message);
    }

    private String extractMessage() {
        if (pluginParameters.message == null && pluginParameters.fromFile == null) {
            throw new FailureException("There was nothing to output. Specify either message or fromFile");
        }
        if (pluginParameters.message != null && pluginParameters.fromFile != null) {
            throw new FailureException("Specify either message or fromFile, not both");
        }
        if (pluginParameters.message != null) {
            return pluginParameters.message;
        }
        try {
            return fileUtil.getFromFile();
        } catch (IOException ioex) {
            throw new FailureException(ioex.getMessage(), ioex);
        }
    }
}
