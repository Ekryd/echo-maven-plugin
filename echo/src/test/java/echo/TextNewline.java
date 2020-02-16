package echo;

import echo.exception.FailureException;
import echo.output.EchoOutput;
import echo.parameter.PluginParameters;
import echo.parameter.PluginParametersBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author bjorn
 * @since 2013-09-20
 */
public class TextNewline {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void illegalNewlineShouldThrowException() {
        expectedException.expect(FailureException.class);
        expectedException.expectMessage("LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were [9]");

        new PluginParametersBuilder().setFormatting(null, "\t").createPluginParameters();
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
