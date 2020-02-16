package echo;

import echo.exception.FailureException;
import echo.output.EchoOutput;
import echo.output.PluginLog;
import echo.parameter.PluginParameters;
import echo.parameter.PluginParametersBuilder;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author bjorn
 * @since 2013-09-09
 */
public class TestEncoding {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private final PluginLog pluginLog = mock(PluginLog.class);
    private final EchoOutput echoOutput = mock(EchoOutput.class);

    private String fileName = null;

    @Before
    public void setupForSaveToFile() {
        doAnswer(invocation -> {
            String content = invocation.getArguments()[0].toString();
            if (content.startsWith("Saving output to ")) {
                fileName = content.substring(17);
            }
            return null;
        }).when(pluginLog).info(anyString());
    }

    @Test
    public void illegalEncodingShouldThrowExceptionWhenReadFromFile() {
        PluginParameters parameters = new PluginParametersBuilder()
                .setMessage(null, "messageEncoding.txt")
                .setFormatting("Gurka", "\n")
                .createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

        expectedException.expect(FailureException.class);
        expectedException.expectMessage("Unsupported encoding: Gurka");

        echoPlugin.echo();
    }

    @Test
    public void illegalEncodingShouldThrowExceptionWhenWriteToFile() {
        PluginParameters parameters = new PluginParametersBuilder()
                .setMessage("TestMessage", null)
                .setFormatting("Gurka", "\n")
                .setFile(new File("."), "out.txt", false, false)
                .createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

        expectedException.expect(FailureException.class);
        expectedException.expectMessage("Unsupported encoding: Gurka");

        echoPlugin.echo();
    }

    @Test
    public void differentEncodingShouldWorkFromFile() {
        PluginParameters parameters = new PluginParametersBuilder()
                .setMessage(null, "messageEncoding.txt")
                .setFormatting("iso-8859-1", "\n").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

        echoPlugin.echo();

        verify(echoOutput).info("Björn");
    }

    @Test
    public void differentEncodingShouldBeIgnoredFromInput() {
        PluginParameters parameters = new PluginParametersBuilder()
                .setMessage("Björn", null)
                .setFormatting("iso-8859-1", "\n").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

        echoPlugin.echo();

        verify(echoOutput).info("Björn");
    }

    @Test
    public void outputUtf8ShouldWorkToStdOut() {
        EchoOutput echoOutput = mock(EchoOutput.class);

        PluginParameters parameters = new PluginParametersBuilder()
                .setMessage("\u00e4\u00a9", null)
                .setFormatting("UTF-8", "\n").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info("\u00e4\u00a9");
    }

    @Test
    public void outputUtf8ShouldWorkToFile() throws IOException {
        PluginParameters parameters = new PluginParametersBuilder()
                .setMessage("\u00e4\u00a9", null)
                .setFile(new File("."), "test.txt", false, false)
                .setFormatting("UTF-8", "\n").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

        try {
            echoPlugin.echo();

            verifyZeroInteractions(echoOutput);

            String output = FileUtils.readFileToString(new File(fileName), "UTF-8");
            assertThat(output, is("\u00e4\u00a9"));
        } finally {
            FileUtils.deleteQuietly(new File(fileName));
        }
    }

    @Test
    public void outputUtf16ShouldWorkToFile() throws IOException {
        PluginParameters parameters = new PluginParametersBuilder()
                .setMessage("&#169;", null)
                .setFile(new File("."), "test.txt", false, false)
                .setFormatting("UTF16", "\n").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

        try {
            echoPlugin.echo();

            verifyZeroInteractions(echoOutput);

            String output = FileUtils.readFileToString(new File(fileName), "UTF16");
            assertThat(output, is("&#169;"));
        } finally {
            FileUtils.deleteQuietly(new File(fileName));
        }
    }

}
