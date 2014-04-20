package echo;

import echo.output.EchoOutput;
import echo.output.EchoOutputWrapper;
import echo.output.Logger;
import echo.parameter.PluginParameters;
import echo.util.FileUtil;

/**
 * The concrete implementation of the echo plugin functionality
 *
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

    /**
     * Creates an new instance of the EchoPlugin
     *
     * @param mavenLogger      wrapper for the maven internal plugin logger
     * @param pluginParameters the user-supplied plugin parameters
     * @param echoOutput       the utility class to output to standard output (in Maven)
     */
    public EchoPlugin(Logger mavenLogger, PluginParameters pluginParameters, EchoOutput echoOutput) {
        this.mavenLogger = mavenLogger;
        this.echoOutput = new EchoOutputWrapper(echoOutput, pluginParameters);
        this.fileUtil = new FileUtil(pluginParameters, mavenLogger);
        this.messageExtractor = new MessageExtractor(pluginParameters, fileUtil);
        this.characterOutput = new CharacterOutput(pluginParameters);

        this.writeMessageToFile = pluginParameters.getToFile() != null;
    }

    /** Output the message */
    public void echo() {
        String messageWithCorrectNewlines = messageExtractor.getFormattedMessage();

        if (characterOutput.isWriteOutput()) {
            String characterArray = characterOutput.getOutput(messageExtractor.getOriginalMessage());
            mavenLogger.info(characterArray);
        }


        if (writeMessageToFile) {
            fileUtil.saveToFile(messageWithCorrectNewlines);
        } else {
            echoOutput.output(messageWithCorrectNewlines);
        }
    }

}
