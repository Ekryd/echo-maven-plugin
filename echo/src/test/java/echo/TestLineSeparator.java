package echo;

import echo.output.EchoOutput;
import echo.output.Logger;
import echo.parameter.PluginParameters;
import echo.parameter.PluginParametersBuilder;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author bjorn
 * @since 2013-09-09
 */
public class TestLineSeparator {
    private Logger logger = mock(Logger.class);

    @Test
    public void formattingTextWithNLShouldResultInLineBreaks() throws Exception {
        EchoOutput echoOutput = mock(EchoOutput.class);

        PluginParameters parameters = new PluginParametersBuilder().setMessage("ABC\n\n\nDEF\r\r\rGHI\r\n\r\n\r\n", null)
                .setFormatting("UTF-8", "\n").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(logger, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info("ABC\n\n\nDEF\n\n\nGHI\n\n\n");
    }

    @Test
    public void formattingXmlWithCRShouldResultInOneLineBreaks() throws Exception {
        EchoOutput echoOutput = mock(EchoOutput.class);

        PluginParameters parameters = new PluginParametersBuilder().setMessage("ABC\n\n\nDEF\r\r\rGHI\r\n\r\n\r\n", null)
                .setFormatting("UTF-8", "\r").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(logger, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info("ABC\r\r\rDEF\r\r\rGHI\r\r\r");
    }

    @Test
    public void formattingXmlWithCRNLShouldResultInOneLineBreaks() throws Exception {
        EchoOutput echoOutput = mock(EchoOutput.class);

        PluginParameters parameters = new PluginParametersBuilder().setMessage("ABC\n\n\nDEF\r\r\rGHI\r\n\r\n\r\n", null)
                .setFormatting("UTF-8", "\r\n").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(logger, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info("ABC\r\n\r\n\r\nDEF\r\n\r\n\r\nGHI\r\n\r\n\r\n");
    }
}
