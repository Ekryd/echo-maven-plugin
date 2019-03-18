package echo;

import echo.exception.FailureException;
import echo.output.EchoOutput;
import echo.output.PluginLog;
import echo.parameter.PluginParameters;
import echo.parameter.PluginParametersBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Matchers;

import java.io.File;
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

    private final PluginLog pluginLog = mock(PluginLog.class);
    private final EchoOutput echoOutput = mock(EchoOutput.class);

    @Test
    public void noInputShouldThrowException() {
        PluginParameters parameters = new PluginParametersBuilder().setMessage(null, null).createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

        expectedException.expect(FailureException.class);
        expectedException.expectMessage("There was nothing to output. Specify either message or fromFile");

        echoPlugin.echo();
    }

    @Test
    public void doubleInputShouldThrowException() {
        PluginParameters parameters = new PluginParametersBuilder().setMessage("Björn", "Gurka").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

        expectedException.expect(FailureException.class);
        expectedException.expectMessage("Specify either message or fromFile, not both");

        echoPlugin.echo();
    }

    @Test
    public void fileNotFoundShouldThrowException() {
        PluginParameters parameters = new PluginParametersBuilder().setMessage(null, "Gurka_doesNotExist").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

        expectedException.expect(FailureException.class);
        expectedException.expectMessage(startsWith("Could not find "));
        expectedException.expectMessage(endsWith("Gurka_doesNotExist or Gurka_doesNotExist in classpath"));

        echoPlugin.echo();
    }

    @Test
    public void foundFileInClassPathShouldOutputToInfo() {
        PluginParameters parameters = new PluginParametersBuilder().setMessage(null, "messageText.txt").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info("Björn");
    }

    @Test
    public void foundFileInAbsolutePathShouldOutputReadingLocation() {
        String absolutePath = new File("src/test/resources/messageText.txt").getAbsolutePath();
        PluginParameters parameters = new PluginParametersBuilder().setMessage(null, absolutePath).createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
        echoPlugin.echo();

        verify(pluginLog).debug("Reading input from " + absolutePath);
    }

    @Test
    public void foundFileInSubModuleShouldOutputReadingLocation() {
        String fileName = "test/resources/messageText.txt";
        File basePath = new File("src");
        PluginParameters parameters = new PluginParametersBuilder().setMessage(null, fileName).setFile(basePath, null, false, false) .createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
        echoPlugin.echo();

        verify(pluginLog).debug("Reading input from " + new File(basePath, fileName).getAbsolutePath());
    }

    @Test
    public void foundFileInClassPathShouldOutputReadingLocation() {
        PluginParameters parameters = new PluginParametersBuilder().setMessage(null, "messageText.txt").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
        echoPlugin.echo();

        String absolutePath = new File("target/test-classes/messageText.txt").getAbsolutePath();
        verify(pluginLog).debug("Reading input from " + absolutePath);
    }

    @Test
    public void urlFromWebShouldReturnText() {
        if (noConnectionToInternet()) {
            return;
        }

        PluginParameters parameters = new PluginParametersBuilder().setMessage(null, "https://www.nsf.gov/").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info(Matchers.startsWith("<!DOCTYPE html"));
    }

    private boolean noConnectionToInternet() {
        try {
            new URL("https://www.nsf.gov/").openStream();
        } catch (IOException e) {
            System.err.println("Cannot connect to Internet, skipping this test!!!");
            return true;
        }
        return false;
    }

}
