package echo;

import echo.output.EchoOutput;
import echo.output.Logger;
import echo.parameter.PluginParameters;
import echo.parameter.PluginParametersBuilder;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author bjorn
 * @since 2013-09-09
 */
public class TestMessage {

    @Test
    public void stringWithSpecialCharactersShouldBeOutput() {
        EchoPlugin echoPlugin = new EchoPlugin();
        Logger logger = mock(Logger.class);
        EchoOutput echoOutput = mock(EchoOutput.class);

        PluginParameters parameters = new PluginParametersBuilder().setMessage("Björn", null).createPluginParameters();
        echoPlugin.setup(logger, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info("Björn");
    }

}
