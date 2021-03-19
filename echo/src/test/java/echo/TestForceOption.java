package echo;

import echo.exception.FailureException;
import echo.output.EchoOutput;
import echo.output.PluginLog;
import echo.parameter.PluginParameters;
import echo.parameter.PluginParametersBuilder;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author bjorn
 * @since 2014-02-27
 */
class TestForceOption {
    private final PluginLog pluginLog = mock(PluginLog.class);
    private final EchoOutput echoOutput = mock(EchoOutput.class);
    private String fileName = null;

    @BeforeEach
    void setup() {
        doAnswer(invocation -> {
            String content = invocation.getArguments()[0].toString();
            if (content.startsWith("Saving output to ")) {
                fileName = content.substring(17);
            }
            return null;
        }).when(pluginLog).info(anyString());
    }

    @Test
    void explicitForceFlagShouldOverwriteReadOnlyFile() throws IOException {
        PluginParameters parameters;
        EchoPlugin echoPlugin;
        String output;

        try {
            parameters = new PluginParametersBuilder().setMessage("One", null).setFile(new File("."), "test.txt", false, true).createPluginParameters();
            echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
            echoPlugin.echo();

            output = FileUtils.readFileToString(new File(fileName), "UTF-8");
            assertThat(output, is("One"));

            assertTrue(new File(fileName).setReadOnly());

            parameters = new PluginParametersBuilder().setMessage("Two", null).setFile(new File("."), "test.txt", false, true).createPluginParameters();
            echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
            echoPlugin.echo();

            verifyNoInteractions(echoOutput);

            output = FileUtils.readFileToString(new File(fileName), "UTF-8");
            assertThat(output, is("Two"));
        } catch (FailureException e) {
            assertThat(e.getMessage(), is("Cannot write to read-only file " + fileName));
        } finally {
            FileUtils.deleteQuietly(new File(fileName));
        }
    }

    @Test
    void noForceFlagShouldThrowExceptionForReadOnlyFile() throws IOException {

        try {
            final PluginParameters parameters1 = new PluginParametersBuilder().setMessage("One", null).setFile(new File("."), "test.txt", false, false).createPluginParameters();
            final EchoPlugin echoPlugin1 = new EchoPlugin(pluginLog, parameters1, echoOutput);
            echoPlugin1.echo();

            final String output = FileUtils.readFileToString(new File(fileName), "UTF-8");
            assertThat(output, is("One"));

            assertTrue(new File(fileName).setReadOnly());

            final PluginParameters parameters2 = new PluginParametersBuilder().setMessage("Two", null).setFile(new File("."), "test.txt", false, false).createPluginParameters();
            final  EchoPlugin echoPlugin2 = new EchoPlugin(pluginLog, parameters2, echoOutput);

            final Executable testMethod = echoPlugin2::echo;

            final FailureException thrown = assertThrows(FailureException.class, testMethod);

            assertThat(thrown.getMessage(), is(equalTo("Cannot write to read-only file " + fileName)));

        } finally {
            FileUtils.deleteQuietly(new File(fileName));
        }
    }
}
