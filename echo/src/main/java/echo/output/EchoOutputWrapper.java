package echo.output;

import echo.parameter.OutputLevelType;

/**
 * @author bjorn
 * @since 2013-09-18
 */
public class EchoOutputWrapper {
    private final EchoOutput echoOutput;
    private final OutputLevelType level;


    public EchoOutputWrapper(EchoOutput echoOutput, OutputLevelType level) {
        this.echoOutput = echoOutput;
        this.level = level;
    }
    
    public void output(String content) {
        switch (level) {
            case ERROR: echoOutput.error(content);
                break;
            case WARNING: echoOutput.warning(content);
                break;
            case INFO: echoOutput.info(content);
                break;
            case VERBOSE: echoOutput.verbose(content);
                break;
            case DEBUG: echoOutput.debug(content);
                break;
        }
    }
}
