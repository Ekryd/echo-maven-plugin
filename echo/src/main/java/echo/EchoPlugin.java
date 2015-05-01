package echo;

import echo.output.EchoOutput;
import echo.output.EchoOutputWrapper;
import echo.output.PluginLog;
import echo.parameter.PluginParameters;
import echo.util.FileUtil;

/**
 * The concrete implementation of the echo plugin functionality
 *
 * @author bjorn
 * @since 2013-08-08
 */
class EchoPlugin {
    private final PluginLog mavenPluginLog;
    private final EchoOutputWrapper echoOutput;
    private final FileUtil fileUtil;
    private final MessageExtractor messageExtractor;
    private final CharacterOutput characterOutput;

    private final boolean writeMessageToFile;

    /**
     * Creates an new instance of the EchoPlugin
     *
     * @param mavenPluginLog      wrapper for the maven internal plugin logger
     * @param pluginParameters the user-supplied plugin parameters
     * @param echoOutput       the utility class to output to standard output (in Maven)
     */
    public EchoPlugin(PluginLog mavenPluginLog, PluginParameters pluginParameters, EchoOutput echoOutput) {
        this.mavenPluginLog = mavenPluginLog;
        this.echoOutput = new EchoOutputWrapper(echoOutput, pluginParameters);
        this.fileUtil = new FileUtil(pluginParameters, mavenPluginLog);
        this.messageExtractor = new MessageExtractor(pluginParameters, fileUtil);
        this.characterOutput = new CharacterOutput(pluginParameters);

        this.writeMessageToFile = pluginParameters.getToFile() != null;
    }

    /** Output the message */
    public void echo() {
        String messageWithCorrectNewlines = messageExtractor.getFormattedMessage();

        if (characterOutput.isWriteOutput()) {
            String characterArray = characterOutput.getOutput(messageExtractor.getOriginalMessage());
            mavenPluginLog.info(characterArray);
        }


        if (writeMessageToFile) {
            fileUtil.saveToFile(messageWithCorrectNewlines);
        } else {
            echoOutput.output(messageWithCorrectNewlines);
        }
    }

}
