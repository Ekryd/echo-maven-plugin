package echo.output;

import echo.parameter.OutputLevelType;
import echo.parameter.PluginParameters;

/**
 * @author bjorn
 * @since 2013-09-18
 */
public class EchoOutputWrapper {
    private final EchoOutput echoOutput;
    private final OutputLevelType level;


    public EchoOutputWrapper(EchoOutput echoOutput, PluginParameters pluginParameters) {
        this.echoOutput = echoOutput;
        this.level = pluginParameters.level;
    }

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
