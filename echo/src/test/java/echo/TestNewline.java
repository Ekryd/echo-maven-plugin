package echo;

import echo.exception.FailureException;
import echo.output.EchoOutput;
import echo.parameter.PluginParameters;
import echo.parameter.PluginParametersBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author bjorn
 * @since 2013-09-20
 */
class TestNewline {

    @Test
    void illegalNewlineShouldThrowException() {

        final Executable testMethod = () -> new PluginParametersBuilder()
                .setFormatting(null, "\t")
                .createPluginParameters();

        final FailureException thrown = assertThrows(FailureException.class, testMethod);

        assertThat(thrown.getMessage(), is(equalTo("LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were [9]")));
    }

    @Test
    void newlineShouldBeReplacedWithLineSeparator1() {
        EchoOutput echoOutput = mock(EchoOutput.class);

        PluginParameters parameters = new PluginParametersBuilder().setMessage("Hex\nover\rthe\r\nphone", null).setFormatting(null, "\n").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(null, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info("Hex\nover\nthe\nphone");
    }

    @Test
    void newlineShouldBeReplacedWithLineSeparator2() {
        EchoOutput echoOutput = mock(EchoOutput.class);

        PluginParameters parameters = new PluginParametersBuilder().setMessage("Hex\nover\rthe\r\nphone", null).setFormatting(null, "\r").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(null, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info("Hex\rover\rthe\rphone");
    }

    @Test
    void newlineShouldBeReplacedWithLineSeparator3() {
        EchoOutput echoOutput = mock(EchoOutput.class);

        PluginParameters parameters = new PluginParametersBuilder().setMessage("Hex\nover\rthe\r\nphone", null).setFormatting(null, "\r\n").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(null, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info("Hex\r\nover\r\nthe\r\nphone");
    }
}
