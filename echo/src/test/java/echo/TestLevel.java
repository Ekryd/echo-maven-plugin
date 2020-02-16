package echo;

import echo.exception.FailureException;
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
 * @since 2013-09-09
 */
public class TestLevel {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private final PluginLog pluginLog = mock(PluginLog.class);
    private final EchoOutput echoOutput = mock(EchoOutput.class);


    @Test
    public void illegalLevelShouldThrowException() {
        expectedException.expect(FailureException.class);
        expectedException.expectMessage("level must be either FAIL, ERROR, WARNING, INFO or DEBUG. Was: Something");

        new PluginParametersBuilder().setLevel("Something").createPluginParameters();
    }

    @Test
    public void nullLevelShouldThrowException() {
        expectedException.expect(FailureException.class);
        expectedException.expectMessage("level must be either FAIL, ERROR, WARNING, INFO or DEBUG. Was: null");

        new PluginParametersBuilder().setLevel(null).createPluginParameters();
    }

    @Test
    public void infoLevelShouldBeDefault() {
        PluginParameters parameters = new PluginParametersBuilder().setMessage("Björn", null).createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info("Björn");
    }

    @Test
    public void failLevelShouldOutputOnFailLevel() {
        PluginParameters parameters = new PluginParametersBuilder().setLevel("fAIl").setMessage("Björn", null).createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).fail("Björn");
    }

    @Test
    public void errorLevelShouldOutputOnErrorLevel() {
        PluginParameters parameters = new PluginParametersBuilder().setLevel("ErRoR").setMessage("Björn", null).createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).error("Björn");
    }

    @Test
    public void warnLevelShouldOutputOnWarningLevel() {
        PluginParameters parameters = new PluginParametersBuilder().setLevel("WARNiNg").setMessage("Björn", null).createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).warning("Björn");
    }


    @Test
    public void infoLevelShouldOutputOnInfoLevel() {
        PluginParameters parameters = new PluginParametersBuilder().setLevel("infO").setMessage("Björn", null).createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info("Björn");
    }


    @Test
    public void debugLevelShouldOutputOnDebugLevel() {
        PluginParameters parameters = new PluginParametersBuilder().setLevel("deBug").setMessage("Björn", null).createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).debug("Björn");
    }

}
