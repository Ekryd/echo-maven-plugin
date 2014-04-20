package echo;

import echo.exception.FailureException;
import echo.output.MavenEchoOutput;
import echo.output.MavenLogger;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import refutils.ReflectionHelper;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author bjorn
 * @since 2013-08-09
 */
public class EchoMojoTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    private EchoMojo echoMojo;

    private final MavenLogger mavenLoggerMock = mock(MavenLogger.class);
    private final MavenEchoOutput mavenEchoOutputMock = mock(MavenEchoOutput.class);

    @Before
    public void setUp() throws Exception {
        echoMojo = new EchoMojo();
        ReflectionHelper echoMojoHelper = new ReflectionHelper(echoMojo);
        echoMojoHelper.setField("level", "INFO");
        echoMojoHelper.setField("lineSeparator", "\\n");
        echoMojoHelper.setField(mavenLoggerMock);
        echoMojoHelper.setField(mavenEchoOutputMock);
    }

    @Test
    public void setupShouldSetupEchoPluginInstance() throws Exception {
        echoMojo.setup();

        EchoPlugin echoPlugin = new ReflectionHelper(echoMojo).getField(EchoPlugin.class);
        assertThat(echoPlugin, notNullValue());
    }

    @Test
    public void exceptionInSetupShouldBeConverted() throws Exception {
        new ReflectionHelper(echoMojo).setField("level", "Gurka");

        expectedException.expect(MojoFailureException.class);
        expectedException.expectMessage(is("level must be either FAIL, ERROR, WARNING, INFO or DEBUG. Was: Gurka"));

        echoMojo.setup();
    }

    @Test
    public void mavenEchoShouldRunEchoPlugin() throws Exception {
        EchoPlugin echoPlugin = mock(EchoPlugin.class);

        new ReflectionHelper(echoMojo).setField(echoPlugin);

        echoMojo.echo();

        verify(echoPlugin).echo();
    }

    @Test
    public void echoShouldRunTheWholePlugin() throws Exception {
        ReflectionHelper reflectionHelper = new ReflectionHelper(echoMojo);
        // Init values
        reflectionHelper.setField("message", "Björn");
        // Init loggers
        reflectionHelper.setField(mavenLoggerMock);
        reflectionHelper.setField(mavenEchoOutputMock);

        echoMojo.setup();
        echoMojo.echo();

        verify(mavenEchoOutputMock).info("Björn");
        verifyNoMoreInteractions(mavenLoggerMock);
        verifyNoMoreInteractions(mavenEchoOutputMock);
    }

    @Test
    public void noMessageShouldGenerateException() throws Exception {
        expectedException.expect(MojoFailureException.class);
        expectedException.expectMessage("There was nothing to output. Specify either message or fromFile");

        echoMojo.execute();

        verifyNoMoreInteractions(mavenLoggerMock);
        verifyNoMoreInteractions(mavenEchoOutputMock);
    }

    @Test
    public void exceptionInEchoSHouldBeConverted() throws Exception {
        EchoPlugin echoPlugin = mock(EchoPlugin.class);

        doThrow(new FailureException("Gurka")).when(echoPlugin).echo();
        new ReflectionHelper(echoMojo).setField(echoPlugin);

        expectedException.expect(MojoFailureException.class);
        expectedException.expectMessage(is("Gurka"));

        echoMojo.echo();
    }
}
