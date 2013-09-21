package echo;

import echo.exception.FailureException;
import echo.output.EchoOutput;
import echo.parameter.PluginParameters;
import echo.parameter.PluginParametersBuilder;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author bjorn
 * @since 2013-09-20
 */
public class TextNewline {

    @Test
    public void illegalNewlineShouldThrowException() {
        try {
            new PluginParametersBuilder().setFormatting(null, "\t").createPluginParameters();
            fail();
        } catch (FailureException fex) {
            assertThat(fex.getMessage(), is("LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were [9]"));
        }
    }

    @Test
    public void newlineShouldBeReplacedWithLineSeparator1() {
        EchoOutput echoOutput = mock(EchoOutput.class);

        PluginParameters parameters = new PluginParametersBuilder().setMessage("Hex\nover\rthe\r\nphone", null).setFormatting(null, "\n").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(null, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info("Hex\nover\nthe\nphone");
    }

    @Test
    public void newlineShouldBeReplacedWithLineSeparator2() {
        EchoOutput echoOutput = mock(EchoOutput.class);

        PluginParameters parameters = new PluginParametersBuilder().setMessage("Hex\nover\rthe\r\nphone", null).setFormatting(null, "\r").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(null, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info("Hex\rover\rthe\rphone");
    }

    @Test
    public void newlineShouldBeReplacedWithLineSeparator3() {
        EchoOutput echoOutput = mock(EchoOutput.class);

        PluginParameters parameters = new PluginParametersBuilder().setMessage("Hex\nover\rthe\r\nphone", null).setFormatting(null, "\r\n").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(null, parameters, echoOutput);
        echoPlugin.echo();

        verify(echoOutput).info("Hex\r\nover\r\nthe\r\nphone");
    }
}
