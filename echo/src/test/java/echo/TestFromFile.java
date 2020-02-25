package echo;

import echo.exception.FailureException;
import echo.output.EchoOutput;
import echo.output.PluginLog;
import echo.parameter.PluginParameters;
import echo.parameter.PluginParametersBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Matchers;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author bjorn
 * @since 2013-09-09
 */
public class TestFromFile {

    private final PluginLog pluginLog = mock(PluginLog.class);
    private final EchoOutput echoOutput = mock(EchoOutput.class);

    @Test
    public void noInputShouldThrowException() {

        PluginParameters parameters = new PluginParametersBuilder().setMessage(null, null).createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

        final Executable testMethod = echoPlugin::echo;

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertThat(thrown.getMessage(), is(equalTo("There was nothing to output. Specify either message or fromFile")));
    }

    @Test
    public void doubleInputShouldThrowException() {

        PluginParameters parameters = new PluginParametersBuilder().setMessage("Björn", "Gurka").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

        final Executable testMethod = echoPlugin::echo;

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertThat(thrown.getMessage(), is(equalTo("Specify either message or fromFile, not both")));
    }

    @Test
    public void fileNotFoundShouldThrowException() {

        PluginParameters parameters = new PluginParametersBuilder().setMessage(null, "Gurka_doesNotExist").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

        final Executable testMethod = echoPlugin::echo;

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertAll(
                () -> assertThat(thrown.getMessage(), startsWith("Could not find ")),
                () -> assertThat(thrown.getMessage(), endsWith("Gurka_doesNotExist or Gurka_doesNotExist in classpath"))
        );
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

        String absolutePath = new File("target/test-classes/messageText.txt").toURI().getPath();
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
