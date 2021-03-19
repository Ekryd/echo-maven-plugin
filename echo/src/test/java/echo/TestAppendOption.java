package echo;

import echo.output.EchoOutput;
import echo.output.PluginLog;
import echo.parameter.PluginParameters;
import echo.parameter.PluginParametersBuilder;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author bjorn
 * @since 2014-02-27
 */
class TestAppendOption {
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
    void explicitAppendFlagShouldAppendToFile() throws IOException {
        PluginParameters parameters = new PluginParametersBuilder().setMessage("Björn", null).setFile(new File("."), "test.txt", true, false).createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

        try {
            // Echo twice
            echoPlugin.echo();
            echoPlugin.echo();

            verifyNoInteractions(echoOutput);

            String output = FileUtils.readFileToString(new File(fileName), "UTF-8");
            assertThat(output, is("BjörnBjörn"));
        } finally {
            FileUtils.deleteQuietly(new File(fileName));
        }
    }

    @Test
    void noAppendFlagShouldOverwriteFile() {
        PluginParameters parameters;
        EchoPlugin echoPlugin;
        String output;

        try {
            parameters = new PluginParametersBuilder().setMessage("One", null).setFile(new File("."), "test.txt", false, false).createPluginParameters();
            echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
            echoPlugin.echo();

            output = FileUtils.readFileToString(new File(fileName), "UTF-8");
            assertThat(output, is("One"));

            parameters = new PluginParametersBuilder().setMessage("Two", null).setFile(new File("."), "test.txt", false, false).createPluginParameters();
            echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
            echoPlugin.echo();

            verifyNoInteractions(echoOutput);

            output = FileUtils.readFileToString(new File(fileName), "UTF-8");
            assertThat(output, is("Two"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileUtils.deleteQuietly(new File(fileName));
        }
    }
}
