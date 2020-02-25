package echo;

import echo.exception.FailureException;
import echo.output.EchoOutput;
import echo.output.PluginLog;
import echo.parameter.PluginParameters;
import echo.parameter.PluginParametersBuilder;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * @author bjorn
 * @since 2013-09-09
 */
public class TestToFile {

    private final PluginLog pluginLog = mock(PluginLog.class);
    private final EchoOutput echoOutput = mock(EchoOutput.class);
    private String fileName = null;

    @Before
    public void setup() {
        doAnswer(invocation -> {
            String content = invocation.getArguments()[0].toString();
            if (content.startsWith("Saving output to ")) {
                fileName = content.substring(17);
            }
            return null;
        }).when(pluginLog).info(anyString());
    }

    @Test
    public void saveToNonExistingToFileShouldWork() throws IOException {
        PluginParameters parameters = new PluginParametersBuilder().setMessage("Björn", null).setFile(new File("."), "test.txt", false, false).createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

        try {
            echoPlugin.echo();

            verifyZeroInteractions(echoOutput);

            String output = FileUtils.readFileToString(new File(fileName), "UTF-8");
            assertThat(output, is("Björn"));
        } finally {
            FileUtils.deleteQuietly(new File(fileName));
        }
    }

    @Test
    public void emptyMessageShouldCreateEmptyFile() throws IOException {
        PluginParameters parameters = new PluginParametersBuilder().setMessage("", null).setFile(new File("."), "test.txt", false, false).createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

        try {
            echoPlugin.echo();

            verifyZeroInteractions(echoOutput);

            String output = FileUtils.readFileToString(new File(fileName), "UTF-8");
            assertThat(output, is(""));
        } finally {
            FileUtils.deleteQuietly(new File(fileName));
        }
    }

    @Test
    public void saveToNonExistingDirectoryShouldWork() throws IOException {
        PluginParameters parameters = new PluginParametersBuilder().setMessage("Björn", null).setFile(new File("."), "gurka/test.txt", false, false).createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

        try {
            echoPlugin.echo();

            verifyZeroInteractions(echoOutput);

            String output = FileUtils.readFileToString(new File(fileName), "UTF-8");
            assertThat(output, is("Björn"));
        } finally {
            FileUtils.deleteQuietly(new File(fileName));
            FileUtils.deleteQuietly(new File(fileName).getParentFile());
        }
    }

    @Test
    public void saveToExistingDirectoryShouldThrowException() {
        PluginParameters parameters;
        EchoPlugin echoPlugin;

        try {
            parameters = new PluginParametersBuilder().setMessage("Björn", null).setFile(new File("."), "gurka/test.txt", false, false).createPluginParameters();
            echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

            //Create directory
            echoPlugin.echo();

            parameters = new PluginParametersBuilder().setMessage("Björn", null).setFile(new File("."), "gurka", false, false).createPluginParameters();
            echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

            echoPlugin.echo();

            fail("Should not work");
        } catch (FailureException e) {
            assertThat(e.getMessage(), startsWith("File "));
            assertThat(e.getMessage(), endsWith("gurka exists but is a directory"));
        } finally {
            FileUtils.deleteQuietly(new File(fileName));
        }
    }


}
