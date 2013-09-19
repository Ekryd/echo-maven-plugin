package echo;

import echo.output.EchoOutput;
import echo.output.EchoOutputWrapper;
import echo.output.Logger;
import echo.parameter.PluginParameters;

/**
 * @author bjorn
 * @since 2013-08-08
 */
public class EchoPlugin {
    private Logger mavenLogger;
    private PluginParameters pluginParameters;
    private EchoOutputWrapper echoOutput;

    public void setup(Logger mavenLogger, PluginParameters pluginParameters, EchoOutput echoOutput) {

        this.mavenLogger = mavenLogger;
        this.pluginParameters = pluginParameters;
        this.echoOutput = new EchoOutputWrapper(echoOutput, pluginParameters.level);
    }

    public void echo() {
        if (pluginParameters.characterOutput) {
            String characterArray = new CharacterOutput(pluginParameters.message).getOutput();
            mavenLogger.info(characterArray);
        }
        echoOutput.output(pluginParameters.message);
    }
}
