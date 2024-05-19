package echo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import echo.exception.FailureException;
import echo.output.EchoOutput;
import echo.output.PluginLog;
import echo.parameter.PluginParametersBuilder;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TestLineSeparator {
  private final PluginLog pluginLog = mock(PluginLog.class);
  private final EchoOutput echoOutput = mock(EchoOutput.class);

  static Stream<Arguments> formattingTestData() {
    return Stream.of(
        Arguments.of("\n", "ABC\n\n\nDEF\n\n\nGHI\n\n\n"),
        Arguments.of("\r", "ABC\r\r\rDEF\r\r\rGHI\r\r\r"),
        Arguments.of("\r\n", "ABC\r\n\r\n\r\nDEF\r\n\r\n\r\nGHI\r\n\r\n\r\n"));
  }

  @ParameterizedTest
  @MethodSource("formattingTestData")
  void formattingTextShouldResultInLineBreaks(String formatting, String expected) {
    var parameters =
        new PluginParametersBuilder()
            .setMessage("ABC\n\n\nDEF\r\r\rGHI\r\n\r\n\r\n", null)
            .setFormatting("UTF-8", formatting)
            .createPluginParameters();
    var echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
    echoPlugin.echo();

    verify(echoOutput).info(expected);
  }

  @Test
  void formattingWithIllegalLineBreaksShouldThrowException() {
    Executable testMethod =
        () ->
            new PluginParametersBuilder()
                .setMessage("ABC\n\n\nDEF\r\r\rGHI\r\n\r\n\r\n", null)
                .setFormatting("UTF-8", "\t")
                .createPluginParameters();

    var thrown = assertThrows(FailureException.class, testMethod);

    assertAll(
        () ->
            assertThat(
                thrown.getMessage(),
                is(
                    equalTo(
                        "LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were [9]"))),
        () -> verifyNoMoreInteractions(pluginLog));
  }
}
