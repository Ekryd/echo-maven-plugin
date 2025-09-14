package echo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import echo.exception.FailureException;
import echo.output.MavenEchoOutput;
import echo.output.MavenPluginLog;
import echo.parameter.PluginParametersBuilder;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class TestEncoding {

  private final MavenPluginLog pluginLog = mock(MavenPluginLog.class);
  private final MavenEchoOutput echoOutput = mock(MavenEchoOutput.class);

  private String fileName = null;

  @BeforeEach
  void setupForSaveToFile() {
    doAnswer(
            invocation -> {
              var content = invocation.getArguments()[0].toString();
              if (content.startsWith("Saving output to ")) {
                fileName = content.substring(17);
              }
              return null;
            })
        .when(pluginLog)
        .info(anyString());
  }

  @Test
  void illegalEncodingShouldThrowExceptionWhenReadFromFile() {
    var parameters =
        new PluginParametersBuilder()
            .setMessage(null, "messageEncoding.txt")
            .setFormatting("Gurka", "\n")
            .createPluginParameters();
    var echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

    Executable testMethod = echoPlugin::echo;

    var thrown = assertThrows(FailureException.class, testMethod);

    assertThat(thrown.getMessage(), is(equalTo("Unsupported encoding: Gurka")));
  }

  @Test
  void illegalEncodingShouldThrowExceptionWhenWriteToFile() {
    var parameters =
        new PluginParametersBuilder()
            .setMessage("TestMessage", null)
            .setFormatting("Gurka", "\n")
            .setFile(new File("."), "out.txt", false, false)
            .createPluginParameters();
    var echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

    Executable testMethod = echoPlugin::echo;

    var thrown = assertThrows(FailureException.class, testMethod);

    assertThat(thrown.getMessage(), is(equalTo("Unsupported encoding: Gurka")));
  }

  @Test
  void differentEncodingShouldWorkFromFile() {
    var parameters =
        new PluginParametersBuilder()
            .setMessage(null, "messageEncoding.txt")
            .setFormatting("iso-8859-1", "\n")
            .createPluginParameters();
    var echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

    echoPlugin.echo();

    verify(echoOutput).info("Björn");
  }

  @Test
  void differentEncodingShouldBeIgnoredFromInput() {
    var parameters =
        new PluginParametersBuilder()
            .setMessage("Björn", null)
            .setFormatting("iso-8859-1", "\n")
            .createPluginParameters();
    var echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

    echoPlugin.echo();

    verify(echoOutput).info("Björn");
  }

  @Test
  void outputUtf8ShouldWorkToStdOut() {
    var parameters =
        new PluginParametersBuilder()
            .setMessage("ä©", null)
            .setFormatting("UTF-8", "\n")
            .createPluginParameters();
    var echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
    echoPlugin.echo();

    verify(echoOutput).info("ä©");
  }

  @Test
  void outputUtf8ShouldWorkToFile() throws IOException {
    var parameters =
        new PluginParametersBuilder()
            .setMessage("ä©", null)
            .setFile(new File("."), "test.txt", false, false)
            .setFormatting("UTF-8", "\n")
            .createPluginParameters();
    var echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

    try {
      echoPlugin.echo();

      verifyNoInteractions(echoOutput);

      var output = FileUtils.readFileToString(new File(fileName), "UTF-8");
      assertThat(output, is("ä©"));
    } finally {
      FileUtils.deleteQuietly(new File(fileName));
    }
  }

  @Test
  void outputUtf16ShouldWorkToFile() throws IOException {
    var parameters =
        new PluginParametersBuilder()
            .setMessage("&#169;", null)
            .setFile(new File("."), "test.txt", false, false)
            .setFormatting("UTF16", "\n")
            .createPluginParameters();
    var echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

    try {
      echoPlugin.echo();

      verifyNoInteractions(echoOutput);

      var output = FileUtils.readFileToString(new File(fileName), "UTF16");
      assertThat(output, is("&#169;"));
    } finally {
      FileUtils.deleteQuietly(new File(fileName));
    }
  }
}
