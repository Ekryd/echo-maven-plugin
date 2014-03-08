package echo;

import echo.output.EchoOutput;
import echo.output.Logger;
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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author bjorn
 * @since 2014-02-27
 */
public class TestAppendOption {
    private Logger logger = mock(Logger.class);
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
        }).when(logger).info(anyString());
    }

    @Test
    public void explicitAppendFlagShouldAppendToFile() throws IOException {
        PluginParameters parameters = new PluginParametersBuilder().setMessage("Björn", null).setFile(new File("."), "test.txt", true, false).createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(logger, parameters, echoOutput);

        try {
            // Echo twice
            echoPlugin.echo();
            echoPlugin.echo();

            verifyZeroInteractions(echoOutput);

            String output = FileUtils.readFileToString(new File(fileName), "UTF-8");
            assertThat(output, is("BjörnBjörn"));
        } finally {
            FileUtils.deleteQuietly(new File(fileName));
        }
    }

    @Test
    public void noAppendFlagShouldOverwriteFile() {
        PluginParameters parameters;
        EchoPlugin echoPlugin;
        String output;

        try {
            parameters = new PluginParametersBuilder().setMessage("One", null).setFile(new File("."), "test.txt", false, false).createPluginParameters();
            echoPlugin = new EchoPlugin(logger, parameters, echoOutput);
            echoPlugin.echo();

            output = FileUtils.readFileToString(new File(fileName), "UTF-8");
            assertThat(output, is("One"));

            parameters = new PluginParametersBuilder().setMessage("Two", null).setFile(new File("."), "test.txt", false, false).createPluginParameters();
            echoPlugin = new EchoPlugin(logger, parameters, echoOutput);
            echoPlugin.echo();

            verifyZeroInteractions(echoOutput);

            output = FileUtils.readFileToString(new File(fileName), "UTF-8");
            assertThat(output, is("Two"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            FileUtils.deleteQuietly(new File(fileName));
        }
    }
}
