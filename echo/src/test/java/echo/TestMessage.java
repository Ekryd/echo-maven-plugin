package echo;

import echo.output.EchoOutput;
import echo.output.PluginLog;
import echo.parameter.PluginParameters;
import echo.parameter.PluginParametersBuilder;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

/**
 * @author bjorn
 * @since 2013-09-09
 */
class TestMessage {
    private final PluginLog pluginLog = mock(PluginLog.class);

    @Test
    void stringWithSpecialCharactersShouldBeOutput() {
        EchoOutput echoOutput = mock(EchoOutput.class);

        PluginParameters parameters = new PluginParametersBuilder().setMessage("Björn", null).createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info("Björn");
    }

    @Test
    void emptyMessageShouldOutputNothing() {
        EchoOutput echoOutput = mock(EchoOutput.class);

        PluginParameters parameters = new PluginParametersBuilder().setMessage("", null).createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
        echoPlugin.echo();

        verifyNoInteractions(echoOutput);
    }

}
