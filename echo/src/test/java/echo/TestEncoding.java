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
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author bjorn
 * @since 2013-09-09
 */
class TestEncoding {

    private final PluginLog pluginLog = mock(PluginLog.class);
    private final EchoOutput echoOutput = mock(EchoOutput.class);

    private String fileName = null;

    @BeforeEach
    void setupForSaveToFile() {
        doAnswer(invocation -> {
            String content = invocation.getArguments()[0].toString();
            if (content.startsWith("Saving output to ")) {
                fileName = content.substring(17);
            }
            return null;
        }).when(pluginLog).info(anyString());
    }

    @Test
    void illegalEncodingShouldThrowExceptionWhenReadFromFile() {

        PluginParameters parameters = new PluginParametersBuilder()
                .setMessage(null, "messageEncoding.txt")
                .setFormatting("Gurka", "\n")
                .createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

        final Executable testMethod = echoPlugin::echo;

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertThat(thrown.getMessage(), is(equalTo("Unsupported encoding: Gurka")));
    }

    @Test
    void illegalEncodingShouldThrowExceptionWhenWriteToFile() {

        PluginParameters parameters = new PluginParametersBuilder()
                .setMessage("TestMessage", null)
                .setFormatting("Gurka", "\n")
                .setFile(new File("."), "out.txt", false, false)
                .createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

        final Executable testMethod = echoPlugin::echo;

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertThat(thrown.getMessage(), is(equalTo("Unsupported encoding: Gurka")));
    }

    @Test
    void differentEncodingShouldWorkFromFile() {
        PluginParameters parameters = new PluginParametersBuilder()
                .setMessage(null, "messageEncoding.txt")
                .setFormatting("iso-8859-1", "\n").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

        echoPlugin.echo();

        verify(echoOutput).info("Björn");
    }

    @Test
    void differentEncodingShouldBeIgnoredFromInput() {
        PluginParameters parameters = new PluginParametersBuilder()
                .setMessage("Björn", null)
                .setFormatting("iso-8859-1", "\n").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

        echoPlugin.echo();

        verify(echoOutput).info("Björn");
    }

    @Test
    void outputUtf8ShouldWorkToStdOut() {
        EchoOutput echoOutput = mock(EchoOutput.class);

        PluginParameters parameters = new PluginParametersBuilder()
                .setMessage("\u00e4\u00a9", null)
                .setFormatting("UTF-8", "\n").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info("\u00e4\u00a9");
    }

    @Test
    void outputUtf8ShouldWorkToFile() throws IOException {
        PluginParameters parameters = new PluginParametersBuilder()
                .setMessage("\u00e4\u00a9", null)
                .setFile(new File("."), "test.txt", false, false)
                .setFormatting("UTF-8", "\n").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

        try {
            echoPlugin.echo();

            verifyNoInteractions(echoOutput);

            String output = FileUtils.readFileToString(new File(fileName), "UTF-8");
            assertThat(output, is("\u00e4\u00a9"));
        } finally {
            FileUtils.deleteQuietly(new File(fileName));
        }
    }

    @Test
    void outputUtf16ShouldWorkToFile() throws IOException {
        PluginParameters parameters = new PluginParametersBuilder()
                .setMessage("&#169;", null)
                .setFile(new File("."), "test.txt", false, false)
                .setFormatting("UTF16", "\n").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

        try {
            echoPlugin.echo();

            verifyNoInteractions(echoOutput);

            String output = FileUtils.readFileToString(new File(fileName), "UTF16");
            assertThat(output, is("&#169;"));
        } finally {
            FileUtils.deleteQuietly(new File(fileName));
        }
    }

}
