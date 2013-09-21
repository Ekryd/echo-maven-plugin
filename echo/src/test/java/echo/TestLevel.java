package echo;

import echo.exception.FailureException;
import echo.output.EchoOutput;
import echo.output.Logger;
import echo.parameter.PluginParameters;
import echo.parameter.PluginParametersBuilder;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author bjorn
 * @since 2013-09-09
 */
public class TestLevel {

    private Logger logger = mock(Logger.class);
    private final EchoOutput echoOutput = mock(EchoOutput.class);


    @Test
    public void illegalLevelShouldThrowException() {
        try {
            new PluginParametersBuilder().setLevel("SOmething").createPluginParameters();
            fail();
        } catch (FailureException e) {
            assertThat(e.getMessage(), is("level must be either ERROR, WARNING, INFO, VERBOSE or DEBUG. Was: SOmething"));
        }

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
