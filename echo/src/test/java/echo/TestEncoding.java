package echo;

import echo.exception.FailureException;
import echo.output.EchoOutput;
import echo.output.Logger;
import echo.parameter.PluginParameters;
import echo.parameter.PluginParametersBuilder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author bjorn
 * @since 2013-09-09
 */
public class TestEncoding {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private Logger logger = mock(Logger.class);
    private final EchoOutput echoOutput = mock(EchoOutput.class);

    @Test
    public void illegalEncodingShouldThrowException() {
        PluginParameters parameters = new PluginParametersBuilder().setMessage(null, "messageEncoding.txt").setFormatting("Gurka", "\n").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(logger, parameters, echoOutput);

        expectedException.expect(FailureException.class);
        expectedException.expectMessage("Unsupported encoding: Gurka");

        echoPlugin.echo();
    }

    @Test
    public void differentEncodingShouldWorkFromFile() {
        PluginParameters parameters = new PluginParametersBuilder().setMessage(null, "messageEncoding.txt").setFormatting("iso-8859-1", "\n").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(logger, parameters, echoOutput);

        echoPlugin.echo();

        verify(echoOutput).info("Björn");
    }

    @Test
    public void differentEncodingShouldBeIgnoredFromInput() {
        PluginParameters parameters = new PluginParametersBuilder().setMessage(new String("Björn"), null).setFormatting("iso-8859-1", "\n").createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(logger, parameters, echoOutput);

        echoPlugin.echo();

        verify(echoOutput).info("Björn");
    }

}
