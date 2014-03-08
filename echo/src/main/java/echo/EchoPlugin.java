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
class EchoPlugin {
    private final Logger mavenLogger;
    private final EchoOutputWrapper echoOutput;
    private final FileUtil fileUtil;
    private final MessageExtractor messageExtractor;
    private final CharacterOutput characterOutput;

    private final boolean writeMessageToFile;

    public EchoPlugin(Logger mavenLogger, PluginParameters pluginParameters, EchoOutput echoOutput) {
        this.mavenLogger = mavenLogger;
        this.echoOutput = new EchoOutputWrapper(echoOutput, pluginParameters);
        this.fileUtil = new FileUtil(pluginParameters, mavenLogger);
        this.messageExtractor = new MessageExtractor(pluginParameters, fileUtil);
        this.characterOutput = new CharacterOutput(pluginParameters);
        
        this.writeMessageToFile = pluginParameters.toFile != null;
    }

    public void echo() {
        if (characterOutput.isWriteOutput()) {
            String characterArray = characterOutput.getOutput();
            mavenLogger.info(characterArray);
        }

        String messageWithCorrectNewlines = messageExtractor.getFormattedMessage();

        if (writeMessageToFile) {
            fileUtil.saveToFile(messageWithCorrectNewlines);
        } else {
            echoOutput.output(messageWithCorrectNewlines);
        }
    }

}
