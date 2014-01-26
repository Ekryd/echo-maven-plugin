package echo;

import echo.exception.FailureException;
import echo.output.EchoOutput;
import echo.output.Logger;
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
    public ExpectedException expectedException = ExpectedException.none();

    private Logger logger = mock(Logger.class);
    private final EchoOutput echoOutput = mock(EchoOutput.class);


    @Test
    public void illegalLevelShouldThrowException() {
        expectedException.expect(FailureException.class);
        expectedException.expectMessage("level must be either ERROR, WARNING, INFO, VERBOSE or DEBUG. Was: SOmething");

        new PluginParametersBuilder().setLevel("SOmething").createPluginParameters();
    }

    @Test
    public void infoLevelShouldBeDefault() {
        PluginParameters parameters = new PluginParametersBuilder().setMessage("Björn", null).createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(logger, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info("Björn");
    }

    @Test
    public void errorLevelShouldOutputOnErrorLevel() throws Exception {
        PluginParameters parameters = new PluginParametersBuilder().setLevel("ErRoR").setMessage("Björn", null).createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(logger, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).error("Björn");
    }

    @Test
    public void warnLevelShouldOutputOnWarningLevel() throws Exception {
        PluginParameters parameters = new PluginParametersBuilder().setLevel("WARNiNg").setMessage("Björn", null).createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(logger, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).warning("Björn");
    }


    @Test
    public void infoLevelShouldOutputOnInfoLevel() throws Exception {
        PluginParameters parameters = new PluginParametersBuilder().setLevel("infO").setMessage("Björn", null).createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(logger, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info("Björn");
    }


    @Test
    public void debugLevelShouldOutputOnDebugLevel() throws Exception {
        PluginParameters parameters = new PluginParametersBuilder().setLevel("deBug").setMessage("Björn", null).createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(logger, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).debug("Björn");
    }


    @Test
    public void verboseLevelShouldOutputOnVerbosesLevel() throws Exception {
        PluginParameters parameters = new PluginParametersBuilder().setLevel("verBOSE").setMessage("Björn", null).createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(logger, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).verbose("Björn");
    }


}
