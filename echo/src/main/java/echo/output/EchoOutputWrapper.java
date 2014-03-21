package echo.output;

import echo.parameter.OutputLevelType;
import echo.parameter.PluginParameters;

/**
 * Will echo a message to standard output (in Maven) with the right message level.
 *
 * @author bjorn
 * @since 2013-09-18
 */
public class EchoOutputWrapper {
    private final EchoOutput echoOutput;
    private final OutputLevelType level;

    /**
     * Creates a new instance of the EchoOutputWrapper class
     *
     * @param echoOutput       the utility class to output to standard output (in Maven)
     * @param pluginParameters The user-supplied plugin parameters
     */
    public EchoOutputWrapper(EchoOutput echoOutput, PluginParameters pluginParameters) {
        this.echoOutput = echoOutput;
        this.level = pluginParameters.getLevel();
    }

    /** Echo the content to standard output (in Maven) */
    public void output(String content) {
        if (content.length() == 0) {
            return;
        }

        switch (level) {
            case FAIL:
                echoOutput.fail(content);
                break;
            case ERROR:
                echoOutput.error(content);
                break;
            case WARNING:
                echoOutput.warning(content);
                break;
            case INFO:
                echoOutput.info(content);
                break;
            case DEBUG:
                echoOutput.debug(content);
                break;
        }
    }
}
