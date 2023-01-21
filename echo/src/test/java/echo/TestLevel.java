package echo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import echo.exception.FailureException;
import echo.output.EchoOutput;
import echo.output.PluginLog;
import echo.parameter.PluginParametersBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class TestLevel {

  private final PluginLog pluginLog = mock(PluginLog.class);
  private final EchoOutput echoOutput = mock(EchoOutput.class);

  @Test
  void illegalLevelShouldThrowException() {

    Executable testMethod =
        () -> new PluginParametersBuilder().setLevel("Something").createPluginParameters();

    var thrown = assertThrows(FailureException.class, testMethod);

    assertThat(
        thrown.getMessage(),
        is(equalTo("level must be either FAIL, ERROR, WARNING, INFO or DEBUG. Was: Something")));
  }

  @Test
  void nullLevelShouldThrowException() {

    Executable testMethod =
        () -> new PluginParametersBuilder().setLevel(null).createPluginParameters();

    var thrown = assertThrows(FailureException.class, testMethod);

    assertThat(
        thrown.getMessage(),
        is(equalTo("level must be either FAIL, ERROR, WARNING, INFO or DEBUG. Was: null")));
  }

  @Test
  void infoLevelShouldBeDefault() {
    var parameters =
        new PluginParametersBuilder().setMessage("Björn", null).createPluginParameters();
    var echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
    echoPlugin.echo();

    verify(echoOutput).info("Björn");
  }

  @Test
  void failLevelShouldOutputOnFailLevel() {
    var parameters =
        new PluginParametersBuilder()
            .setLevel("fAIl")
            .setMessage("Björn", null)
            .createPluginParameters();
    var echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
    echoPlugin.echo();

    verify(echoOutput).fail("Björn");
  }

  @Test
  void errorLevelShouldOutputOnErrorLevel() {
    var parameters =
        new PluginParametersBuilder()
            .setLevel("ErRoR")
            .setMessage("Björn", null)
            .createPluginParameters();
    var echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
    echoPlugin.echo();

    verify(echoOutput).error("Björn");
  }

  @Test
  void warnLevelShouldOutputOnWarningLevel() {
    var parameters =
        new PluginParametersBuilder()
            .setLevel("WARNiNg")
            .setMessage("Björn", null)
            .createPluginParameters();
    var echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
    echoPlugin.echo();

    verify(echoOutput).warning("Björn");
  }

  @Test
  void infoLevelShouldOutputOnInfoLevel() {
    var parameters =
        new PluginParametersBuilder()
            .setLevel("infO")
            .setMessage("Björn", null)
            .createPluginParameters();
    var echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
    echoPlugin.echo();

    verify(echoOutput).info("Björn");
  }

  @Test
  void debugLevelShouldOutputOnDebugLevel() {
    var parameters =
        new PluginParametersBuilder()
            .setLevel("deBug")
            .setMessage("Björn", null)
            .createPluginParameters();
    var echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
    echoPlugin.echo();

    verify(echoOutput).debug("Björn");
  }
}
