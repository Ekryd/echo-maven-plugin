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
public class TestFromFile {

    private Logger logger = mock(Logger.class);;
    private final EchoOutput echoOutput = mock(EchoOutput.class);

    @Test
    public void noInputShouldThrowException() {
        EchoPlugin echoPlugin = new EchoPlugin();

        PluginParameters parameters = new PluginParametersBuilder().setMessage(null, null).createPluginParameters();
        echoPlugin.setup(logger, parameters, echoOutput);
        try {
        echoPlugin.echo();
            fail();
        } catch (FailureException fex) {
            assertThat(fex.getMessage(), is("There was nothing to output. Specify either message or fromFile"));
        }
    }

    @Test
    public void doubleInputShouldThrowException() {
        EchoPlugin echoPlugin = new EchoPlugin();

        PluginParameters parameters = new PluginParametersBuilder().setMessage("Björn", "Gurka").createPluginParameters();
        echoPlugin.setup(logger, parameters, echoOutput);
        try {
        echoPlugin.echo();
            fail();
        } catch (FailureException fex) {
            assertThat(fex.getMessage(), is("Specify either message or fromFile, not both"));
        }
    }
    
    @Test
    public void fileNotFoundShouldThrowException() {
        EchoPlugin echoPlugin = new EchoPlugin();

        PluginParameters parameters = new PluginParametersBuilder().setMessage(null, "Gurka_doesNotExist").createPluginParameters();
        echoPlugin.setup(logger, parameters, echoOutput);
        try {
        echoPlugin.echo();
            fail();
        } catch (FailureException fex) {
            assertThat(fex.getMessage(), is("Could not find /Users/bjorn/Workspace/echo-plugin/Gurka_doesNotExist or Gurka_doesNotExist in classpath"));
        }
    }
    
    @Test
    public void foundFileShouldOutputToInfo() {
        EchoPlugin echoPlugin = new EchoPlugin();

        PluginParameters parameters = new PluginParametersBuilder().setMessage(null, "messageText.txt").createPluginParameters();
        echoPlugin.setup(logger, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info("Björn");
    }
    
}
