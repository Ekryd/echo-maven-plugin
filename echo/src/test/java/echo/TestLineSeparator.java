package echo;

import echo.exception.FailureException;
import echo.output.EchoOutput;
import echo.output.PluginLog;
import echo.parameter.PluginParameters;
import echo.parameter.PluginParametersBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.*;

/**
 * @author bjorn
 * @since 2013-09-09
 */
public class TestLineSeparator {
    private final PluginLog pluginLog = mock(PluginLog.class);

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void formattingTextWithNLShouldResultInLineBreaks() {
        EchoOutput echoOutput = mock(EchoOutput.class);

        PluginParameters parameters = new PluginParametersBuilder().setMessage("ABC\n\n\nDEF\r\r\rGHI\r\n\r\n\r\n", null)
                .setFormatting("UTF-8", "\n").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info("ABC\n\n\nDEF\n\n\nGHI\n\n\n");
    }

    @Test
    public void formattingXmlWithCRShouldResultInOneLineBreaks() {
        EchoOutput echoOutput = mock(EchoOutput.class);

        PluginParameters parameters = new PluginParametersBuilder().setMessage("ABC\n\n\nDEF\r\r\rGHI\r\n\r\n\r\n", null)
                .setFormatting("UTF-8", "\r").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info("ABC\r\r\rDEF\r\r\rGHI\r\r\r");
    }

    @Test
    public void formattingXmlWithCRNLShouldResultInOneLineBreaks() {
        EchoOutput echoOutput = mock(EchoOutput.class);

        PluginParameters parameters = new PluginParametersBuilder().setMessage("ABC\n\n\nDEF\r\r\rGHI\r\n\r\n\r\n", null)
                .setFormatting("UTF-8", "\r\n").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info("ABC\r\n\r\n\r\nDEF\r\n\r\n\r\nGHI\r\n\r\n\r\n");
    }

    @Test
    public void formattingWithIllegalLineBreaksShouldThrowException() {
        EchoOutput echoOutput = mock(EchoOutput.class);

        expectedException.expect(FailureException.class);
        expectedException.expectMessage("LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were [9]");

        PluginParameters parameters = new PluginParametersBuilder().setMessage("ABC\n\n\nDEF\r\r\rGHI\r\n\r\n\r\n", null)
                .setFormatting("UTF-8", "\t").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
        echoPlugin.echo();

        verifyNoMoreInteractions(pluginLog);
    }
}
