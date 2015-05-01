package echo;

import echo.output.EchoOutput;
import echo.output.PluginLog;
import echo.parameter.PluginParameters;
import echo.parameter.PluginParametersBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author bjorn
 * @since 2013-11-13
 */
public class TestCharacterOutput {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private PluginLog pluginLog = mock(PluginLog.class);
    private final EchoOutput echoOutput = mock(EchoOutput.class);

    @Test
    public void characterDebugOutputShouldOutputToInfoLevel() {
        PluginParameters parameters = new PluginParametersBuilder().setMessage("Gurka", null).setLevel("DEBUG").setDebug(true).createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

        echoPlugin.echo();

        verify(pluginLog).info("[['G' , 71 ],['u' , 117 ],['r' , 114 ],['k' , 107 ],['a' , 97 ]]");
    }

}
