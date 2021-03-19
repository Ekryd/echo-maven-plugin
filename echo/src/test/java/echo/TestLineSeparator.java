package echo;

import echo.exception.FailureException;
import echo.output.EchoOutput;
import echo.output.PluginLog;
import echo.parameter.PluginParameters;
import echo.parameter.PluginParametersBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author bjorn
 * @since 2013-09-09
 */
class TestLineSeparator {
    private final PluginLog pluginLog = mock(PluginLog.class);

    @Test
    void formattingTextWithNLShouldResultInLineBreaks() {
        EchoOutput echoOutput = mock(EchoOutput.class);

        PluginParameters parameters = new PluginParametersBuilder().setMessage("ABC\n\n\nDEF\r\r\rGHI\r\n\r\n\r\n", null)
                .setFormatting("UTF-8", "\n").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info("ABC\n\n\nDEF\n\n\nGHI\n\n\n");
    }

    @Test
    void formattingXmlWithCRShouldResultInOneLineBreaks() {
        EchoOutput echoOutput = mock(EchoOutput.class);

        PluginParameters parameters = new PluginParametersBuilder().setMessage("ABC\n\n\nDEF\r\r\rGHI\r\n\r\n\r\n", null)
                .setFormatting("UTF-8", "\r").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info("ABC\r\r\rDEF\r\r\rGHI\r\r\r");
    }

    @Test
    void formattingXmlWithCRNLShouldResultInOneLineBreaks() {
        EchoOutput echoOutput = mock(EchoOutput.class);

        PluginParameters parameters = new PluginParametersBuilder().setMessage("ABC\n\n\nDEF\r\r\rGHI\r\n\r\n\r\n", null)
                .setFormatting("UTF-8", "\r\n").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info("ABC\r\n\r\n\r\nDEF\r\n\r\n\r\nGHI\r\n\r\n\r\n");
    }

    @Test
    void formattingWithIllegalLineBreaksShouldThrowException() {

        final Executable testMethod = () -> new PluginParametersBuilder()
                .setMessage("ABC\n\n\nDEF\r\r\rGHI\r\n\r\n\r\n", null)
                .setFormatting("UTF-8", "\t")
                .createPluginParameters();

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertAll(
                () -> assertThat(thrown.getMessage(), is(equalTo("LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were [9]"))),
                () -> verifyNoMoreInteractions(pluginLog)
        );
    }
}
