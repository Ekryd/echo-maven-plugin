package echo;

import echo.exception.FailureException;
import echo.output.EchoOutput;
import echo.output.Logger;
import echo.parameter.PluginParameters;
import echo.parameter.PluginParametersBuilder;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
 * @since 2013-09-09
 */
public class TestEncoding {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Logger logger = mock(Logger.class);
    private final EchoOutput echoOutput = mock(EchoOutput.class);

    private String fileName = null;

    @Before
    public void setupForSaveToFile() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String content = invocation.getArguments()[0].toString();
//                System.out.println(content);
                if (content.startsWith("Saving output to ")) {
                    fileName = content.substring(17);
                }
                return null;
            }
        }).when(logger).info(anyString());
    }

    @Test
    public void illegalEncodingShouldThrowException() {
        PluginParameters parameters = new PluginParametersBuilder()
                .setMessage(null, "messageEncoding.txt")
                .setFormatting("Gurka", "\n")
                .createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(logger, parameters, echoOutput);

        expectedException.expect(FailureException.class);
        expectedException.expectMessage("Unsupported encoding: Gurka");

        echoPlugin.echo();
    }

    @Test
    public void differentEncodingShouldWorkFromFile() {
        PluginParameters parameters = new PluginParametersBuilder()
                .setMessage(null, "messageEncoding.txt")
                .setFormatting("iso-8859-1", "\n").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(logger, parameters, echoOutput);

        echoPlugin.echo();

        verify(echoOutput).info("Björn");
    }

    @Test
    public void differentEncodingShouldBeIgnoredFromInput() {
        PluginParameters parameters = new PluginParametersBuilder()
                .setMessage(new String("Björn"), null)
                .setFormatting("iso-8859-1", "\n").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(logger, parameters, echoOutput);

        echoPlugin.echo();

        verify(echoOutput).info("Björn");
    }

    @Test
    public void outputUtf8ShouldWorkToStdOut() {
        EchoOutput echoOutput = mock(EchoOutput.class);

        PluginParameters parameters = new PluginParametersBuilder()
                .setMessage("\u00e4\u00a9", null)
                .setFormatting("UTF-8", "\n").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(logger, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info("\u00e4\u00a9");
    }

    @Test
    public void outputUtf8ShouldWorkToFile() throws IOException {
        PluginParameters parameters = new PluginParametersBuilder()
                .setMessage("\u00e4\u00a9", null)
                .setFile("test.txt", false, false)
                .setFormatting("UTF-8", "\n").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(logger, parameters, echoOutput);

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
                .setFile("test.txt", false, false)
                .setFormatting("UTF16", "\n").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(logger, parameters, echoOutput);

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
