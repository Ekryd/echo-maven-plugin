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
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * @author bjorn
 * @since 2013-09-09
 */
class TestToFile {

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
    void saveToNonExistingToFileShouldWork() throws IOException {
        PluginParameters parameters = new PluginParametersBuilder().setMessage("Björn", null).setFile(new File("."), "test.txt", false, false).createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

        try {
            echoPlugin.echo();

            verifyNoInteractions(echoOutput);

            String output = FileUtils.readFileToString(new File(fileName), "UTF-8");
            assertThat(output, is("Björn"));
        } finally {
            FileUtils.deleteQuietly(new File(fileName));
        }
    }

    @Test
    void emptyMessageShouldCreateEmptyFile() throws IOException {
        PluginParameters parameters = new PluginParametersBuilder().setMessage("", null).setFile(new File("."), "test.txt", false, false).createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

        try {
            echoPlugin.echo();

            verifyNoInteractions(echoOutput);

            String output = FileUtils.readFileToString(new File(fileName), "UTF-8");
            assertThat(output, is(""));
        } finally {
            FileUtils.deleteQuietly(new File(fileName));
        }
    }

    @Test
    void saveToNonExistingDirectoryShouldWork() throws IOException {
        PluginParameters parameters = new PluginParametersBuilder().setMessage("Björn", null).setFile(new File("."), "gurka/test.txt", false, false).createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

        try {
            echoPlugin.echo();

            verifyNoInteractions(echoOutput);

            String output = FileUtils.readFileToString(new File(fileName), "UTF-8");
            assertThat(output, is("Björn"));
        } finally {
            FileUtils.deleteQuietly(new File(fileName));
            FileUtils.deleteQuietly(new File(fileName).getParentFile());
        }
    }

    @Test
    void saveToExistingDirectoryShouldThrowException() {

        try {
            final PluginParameters parameters1 = new PluginParametersBuilder().setMessage("Björn", null).setFile(new File("."), "gurka/test.txt", false, false).createPluginParameters();
            final EchoPlugin echoPlugin1 = new EchoPlugin(pluginLog, parameters1, echoOutput);

            //Create directory
            echoPlugin1.echo();

            final PluginParameters parameters2 = new PluginParametersBuilder().setMessage("Björn", null).setFile(new File("."), "gurka", false, false).createPluginParameters();
            final EchoPlugin echoPlugin2 = new EchoPlugin(pluginLog, parameters2, echoOutput);

            final Executable testMethod = echoPlugin2::echo;

            final FailureException thrown = assertThrows(FailureException.class, testMethod);

            assertAll(
                    () -> assertThat(thrown.getMessage(), startsWith("File ")),
                    () -> assertThat(thrown.getMessage(), endsWith("gurka exists but is a directory"))
            );

        } finally {
            FileUtils.deleteQuietly(new File(fileName));
        }
    }


}
