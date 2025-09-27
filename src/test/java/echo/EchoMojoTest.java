package echo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import echo.exception.FailureException;
import echo.output.MavenEchoOutput;
import echo.output.MavenPluginLog;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import refutils.ReflectionHelper;

class EchoMojoTest {

  private EchoMojo echoMojo;

  private final MavenPluginLog mavenLoggerMock = mock(MavenPluginLog.class);
  private final MavenEchoOutput mavenEchoOutputMock = mock(MavenEchoOutput.class);
  private final Log pluginLogMock = mock(Log.class);

  @BeforeEach
  void setUp() {
    echoMojo = new EchoMojo();
    var echoMojoHelper = new ReflectionHelper(echoMojo);
    echoMojoHelper.setField("level", "INFO");
    echoMojoHelper.setField("lineSeparator", "\\n");
    echoMojoHelper.setField(mavenLoggerMock);
    echoMojoHelper.setField(mavenEchoOutputMock);
  }

  @Test
  void setupShouldSetupEchoPluginInstance() throws Exception {
    echoMojo.setup();

    var echoPlugin = new ReflectionHelper(echoMojo).getField(EchoPlugin.class);
    assertThat(echoPlugin, notNullValue());
  }

  @Test
  void exceptionInSetupShouldBeConverted() {

    new ReflectionHelper(echoMojo).setField("level", "Gurka");

    Executable testMethod = () -> echoMojo.setup();

    var thrown = assertThrows(MojoFailureException.class, testMethod);

    assertThat(
        thrown.getMessage(),
        is(equalTo("level must be either FAIL, ERROR, WARNING, INFO or DEBUG. Was: Gurka")));
  }

  @Test
  void mavenEchoShouldRunEchoPlugin() throws Exception {
    var echoPlugin = mock(EchoPlugin.class);

    new ReflectionHelper(echoMojo).setField(echoPlugin);

    echoMojo.echo();

    verify(echoPlugin).echo();
  }

  @Test
  void echoShouldRunTheWholePlugin() throws Exception {
    var reflectionHelper = new ReflectionHelper(echoMojo);
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
  void noMessageShouldGenerateException() {

    Executable testMethod = () -> echoMojo.execute();

    var thrown = assertThrows(MojoFailureException.class, testMethod);

    assertAll(
        () ->
            assertThat(
                thrown.getMessage(),
                is(equalTo("There was nothing to output. Specify either message or fromFile"))),
        () -> verifyNoMoreInteractions(mavenLoggerMock),
        () -> verifyNoMoreInteractions(mavenEchoOutputMock));
  }

  @Test
  void exceptionInEchoShouldBeConverted() {
    var echoPlugin = mock(EchoPlugin.class);

    doThrow(new FailureException("Gurka")).when(echoPlugin).echo();
    new ReflectionHelper(echoMojo).setField(echoPlugin);

    Executable testMethod = () -> echoMojo.echo();

    var thrown = assertThrows(MojoFailureException.class, testMethod);

    assertThat(thrown.getMessage(), is(equalTo("Gurka")));
  }

  @Test
  void skipShouldOnlyOutputMessage() throws Exception {
    new ReflectionHelper(echoMojo).setField("skip", true);
    new ReflectionHelper(echoMojo, AbstractMojo.class).setField(pluginLogMock);

    echoMojo.execute();

    verify(pluginLogMock).info("Skipping echo-maven-plugin");
  }

  @Test
  void executeShouldOutputMessage() throws Exception {
    new ReflectionHelper(echoMojo).setField("message", "Björn");
    new ReflectionHelper(echoMojo, AbstractMojo.class).setField(pluginLogMock);

    echoMojo.execute();

    verify(pluginLogMock).info("Björn");
    verifyNoMoreInteractions(pluginLogMock);
  }
}
