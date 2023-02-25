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
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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

  public static Stream<Arguments> logLevels() {
    return Stream.of(
        Arguments.of("fAIl", (BiConsumer<EchoOutput, String>) EchoOutput::fail),
        Arguments.of("FataL", (BiConsumer<EchoOutput, String>) EchoOutput::fail),
        Arguments.of("ErRoR", (BiConsumer<EchoOutput, String>) EchoOutput::error),
        Arguments.of("WARNiNg", (BiConsumer<EchoOutput, String>) EchoOutput::warning),
        Arguments.of("waRN", (BiConsumer<EchoOutput, String>) EchoOutput::warning),
        Arguments.of("infO", (BiConsumer<EchoOutput, String>) EchoOutput::info),
        Arguments.of("deBug", (BiConsumer<EchoOutput, String>) EchoOutput::debug),
        Arguments.of("TRACe", (BiConsumer<EchoOutput, String>) EchoOutput::debug));
  }

  @ParameterizedTest
  @MethodSource("logLevels")
  void failLevelShouldOutputOnFailLevel(String level, BiConsumer<EchoOutput, String> expectedCall) {
    var parameters =
        new PluginParametersBuilder()
            .setLevel(level)
            .setMessage("Björn", null)
            .createPluginParameters();
    var echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
    echoPlugin.echo();

    expectedCall.accept(verify(echoOutput), "Björn");
  }
}
