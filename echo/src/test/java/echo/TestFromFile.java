package echo;

import echo.exception.FailureException;
import echo.output.EchoOutput;
import echo.output.Logger;
import echo.parameter.PluginParameters;
import echo.parameter.PluginParametersBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Matchers;

import java.io.IOException;
import java.net.URL;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author bjorn
 * @since 2013-09-09
 */
public class TestFromFile {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Logger logger = mock(Logger.class);
    private final EchoOutput echoOutput = mock(EchoOutput.class);

    @Test
    public void noInputShouldThrowException() {
        PluginParameters parameters = new PluginParametersBuilder().setMessage(null, null).createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(logger, parameters, echoOutput);

        expectedException.expect(FailureException.class);
        expectedException.expectMessage("There was nothing to output. Specify either message or fromFile");

        echoPlugin.echo();
    }

    @Test
    public void doubleInputShouldThrowException() {
        PluginParameters parameters = new PluginParametersBuilder().setMessage("Björn", "Gurka").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(logger, parameters, echoOutput);

        expectedException.expect(FailureException.class);
        expectedException.expectMessage("Specify either message or fromFile, not both");

        echoPlugin.echo();
    }

    @Test
    public void fileNotFoundShouldThrowException() {
        PluginParameters parameters = new PluginParametersBuilder().setMessage(null, "Gurka_doesNotExist").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(logger, parameters, echoOutput);

        expectedException.expect(FailureException.class);
        expectedException.expectMessage(startsWith("Could not find "));
        expectedException.expectMessage(endsWith("Gurka_doesNotExist or Gurka_doesNotExist in classpath"));

        echoPlugin.echo();
    }

    @Test
    public void foundFileShouldOutputToInfo() {
        PluginParameters parameters = new PluginParametersBuilder().setMessage(null, "messageText.txt").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(logger, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info("Björn");
    }

    @Test
    public void urlFromWebShouldReturnText() {
        if (noConnectionToInternet()) {
            return;
        }

        PluginParameters parameters = new PluginParametersBuilder().setMessage(null, "http://opensource.org/licenses/gpl-license").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(logger, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info(Matchers.startsWith("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML+RDFa 1.0//EN"));
    }

    private boolean noConnectionToInternet() {
        try {
            new URL("http://opensource.org/licenses/gpl-license").openStream();
        } catch (IOException e) {
            System.err.println("Cannot connect to Internet, skipping this test!!!");
            return true;
        }
        return false;
    }

}
