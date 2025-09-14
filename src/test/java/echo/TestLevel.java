package echo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import echo.exception.FailureException;
import echo.output.MavenEchoOutput;
import echo.output.MavenPluginLog;
import echo.parameter.PluginParametersBuilder;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TestLevel {

  private final MavenPluginLog pluginLog = mock(MavenPluginLog.class);
  private final MavenEchoOutput echoOutput = mock(MavenEchoOutput.class);

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
        Arguments.of("fAIl", (BiConsumer<MavenEchoOutput, String>) MavenEchoOutput::fail),
        Arguments.of("FataL", (BiConsumer<MavenEchoOutput, String>) MavenEchoOutput::fail),
        Arguments.of("ErRoR", (BiConsumer<MavenEchoOutput, String>) MavenEchoOutput::error),
        Arguments.of("WARNiNg", (BiConsumer<MavenEchoOutput, String>) MavenEchoOutput::warning),
        Arguments.of("waRN", (BiConsumer<MavenEchoOutput, String>) MavenEchoOutput::warning),
        Arguments.of("infO", (BiConsumer<MavenEchoOutput, String>) MavenEchoOutput::info),
        Arguments.of("deBug", (BiConsumer<MavenEchoOutput, String>) MavenEchoOutput::debug),
        Arguments.of("TRACe", (BiConsumer<MavenEchoOutput, String>) MavenEchoOutput::debug));
  }

  @ParameterizedTest
  @MethodSource("logLevels")
  void failLevelShouldOutputOnFailLevel(
      String level, BiConsumer<MavenEchoOutput, String> expectedCall) {
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
