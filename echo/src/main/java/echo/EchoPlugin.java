package echo;

import echo.output.EchoOutput;
import echo.output.EchoOutputWrapper;
import echo.output.Logger;
import echo.parameter.PluginParameters;
import echo.util.FileUtil;

/**
 * @author bjorn
 * @since 2013-08-08
 */
public class EchoPlugin {
    private final Logger mavenLogger;
    private final EchoOutputWrapper echoOutput;
    private final FileUtil fileUtil;
    private final MessageExtractor messageExtractor;

    private final boolean characterOutput;
    private final String toFile;
    private final String message;

    public EchoPlugin(Logger mavenLogger, PluginParameters pluginParameters, EchoOutput echoOutput) {
        this.mavenLogger = mavenLogger;
        this.echoOutput = new EchoOutputWrapper(echoOutput, pluginParameters);
        this.fileUtil = new FileUtil(pluginParameters, mavenLogger);
        this.messageExtractor = new MessageExtractor(pluginParameters, fileUtil);
        
        this.characterOutput = pluginParameters.characterOutput;
        this.toFile = pluginParameters.toFile;
        this.message = pluginParameters.message;
    }

    public void echo() {
        if (characterOutput) {
            String characterArray = new CharacterOutput(message).getOutput();
            mavenLogger.info(characterArray);
        }

        String messageWithCorrectNewlines = messageExtractor.getFormattedMessage();

        if (toFile != null) {
            fileUtil.saveToFile(messageWithCorrectNewlines);
        } else {
            echoOutput.output(messageWithCorrectNewlines);
        }
    }

}
