package echo;

import echo.exception.FailureException;
import echo.output.EchoOutput;
import echo.output.PluginLog;
import echo.parameter.PluginParameters;
import echo.parameter.PluginParametersBuilder;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author bjorn
 * @since 2014-02-27
 */
public class TestForceOption {
    private PluginLog pluginLog = mock(PluginLog.class);
    private final EchoOutput echoOutput = mock(EchoOutput.class);
    private String fileName = null;

    @Before
    public void setup() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String content = invocation.getArguments()[0].toString();
                if (content.startsWith("Saving output to ")) {
                    fileName = content.substring(17);
                }
                return null;
            }
        }).when(pluginLog).info(anyString());
    }

    @Test
    public void explicitForceFlagShouldOverwriteReadOnlyFile() throws IOException {
        PluginParameters parameters;
        EchoPlugin echoPlugin;
        String output;

        try {
            parameters = new PluginParametersBuilder().setMessage("One", null).setFile(new File("."), "test.txt", false, true).createPluginParameters();
            echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
            echoPlugin.echo();

            output = FileUtils.readFileToString(new File(fileName), "UTF-8");
            assertThat(output, is("One"));

            new File(fileName).setReadOnly();

            parameters = new PluginParametersBuilder().setMessage("Two", null).setFile(new File("."), "test.txt", false, true).createPluginParameters();
            echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
            echoPlugin.echo();

            verifyZeroInteractions(echoOutput);

            output = FileUtils.readFileToString(new File(fileName), "UTF-8");
            assertThat(output, is("Two"));
        } catch (FailureException e) {
            assertThat(e.getMessage(), is("Cannot write to read-only file " + fileName));
        } finally {
            FileUtils.deleteQuietly(new File(fileName));
        }
    }

    @Test
    public void noForceFlagShouldThrowExceptionForReadOnlyFile() throws IOException {
        PluginParameters parameters;
        EchoPlugin echoPlugin;
        String output;

        try {
            parameters = new PluginParametersBuilder().setMessage("One", null).setFile(new File("."), "test.txt", false, false).createPluginParameters();
            echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
            echoPlugin.echo();

            output = FileUtils.readFileToString(new File(fileName), "UTF-8");
            assertThat(output, is("One"));

            new File(fileName).setReadOnly();

            parameters = new PluginParametersBuilder().setMessage("Two", null).setFile(new File("."), "test.txt", false, false).createPluginParameters();
            echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
            echoPlugin.echo();

            fail("Read only file should not work");
        } catch (FailureException e) {
            assertThat(e.getMessage(), is("Cannot write to read-only file " + fileName));
        } finally {
            FileUtils.deleteQuietly(new File(fileName));
        }
    }
}
