package echo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import echo.exception.FailureException;
import echo.output.EchoOutput;
import echo.parameter.PluginParametersBuilder;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TestNewline {
  private final EchoOutput echoOutput = mock(EchoOutput.class);

  @Test
  void illegalNewlineShouldThrowException() {
    Executable testMethod =
        () -> new PluginParametersBuilder().setFormatting(null, "\t").createPluginParameters();

    var thrown = assertThrows(FailureException.class, testMethod);

    assertThat(
        thrown.getMessage(),
        is(
            equalTo(
                "LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were [9]")));
  }

  static Stream<Arguments> formattingTestData() {
    return Stream.of(
        Arguments.of("\n", "Hex\nover\nthe\nphone"),
        Arguments.of("\r", "Hex\rover\rthe\rphone"),
        Arguments.of("\r\n", "Hex\r\nover\r\nthe\r\nphone"));
  }

  @ParameterizedTest
  @MethodSource("formattingTestData")
  void newlineShouldBeReplacedWithLineSeparator(String formatting, String expected) {
    var parameters =
        new PluginParametersBuilder()
            .setMessage("Hex\nover\rthe\r\nphone", null)
            .setFormatting(null, formatting)
            .createPluginParameters();
    var echoPlugin = new EchoPlugin(null, parameters, echoOutput);
    echoPlugin.echo();

    verify(echoOutput).info(expected);
  }
}
