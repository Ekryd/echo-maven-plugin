package echo;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.hamcrest.core.StringStartsWith.startsWith;
import static org.junit.jupiter.api.Assertions.assertAll;
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

class TestToFile {

  private final MavenPluginLog pluginLog = mock(MavenPluginLog.class);
  private final MavenEchoOutput echoOutput = mock(MavenEchoOutput.class);
  private String fileName = null;

  @BeforeEach
  void setup() {
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
  void saveToNonExistingToFileShouldWork() throws IOException {
    var parameters =
        new PluginParametersBuilder()
            .setMessage("Björn", null)
            .setFile(new File("."), "test.txt", false, false)
            .createPluginParameters();
    var echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

    try {
      echoPlugin.echo();

      verifyNoInteractions(echoOutput);

      var output = FileUtils.readFileToString(new File(fileName), "UTF-8");
      assertThat(output, is("Björn"));
    } finally {
      FileUtils.deleteQuietly(new File(fileName));
    }
  }

  @Test
  void emptyMessageShouldCreateEmptyFile() throws IOException {
    var parameters =
        new PluginParametersBuilder()
            .setMessage("", null)
            .setFile(new File("."), "test.txt", false, false)
            .createPluginParameters();
    var echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

    try {
      echoPlugin.echo();

      verifyNoInteractions(echoOutput);

      var output = FileUtils.readFileToString(new File(fileName), "UTF-8");
      assertThat(output, is(""));
    } finally {
      FileUtils.deleteQuietly(new File(fileName));
    }
  }

  @Test
  void saveToNonExistingDirectoryShouldWork() throws IOException {
    var parameters =
        new PluginParametersBuilder()
            .setMessage("Björn", null)
            .setFile(new File("."), "gurka/test.txt", false, false)
            .createPluginParameters();
    var echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

    try {
      echoPlugin.echo();

      verifyNoInteractions(echoOutput);

      var output = FileUtils.readFileToString(new File(fileName), "UTF-8");
      assertThat(output, is("Björn"));
    } finally {
      FileUtils.deleteQuietly(new File(fileName));
      FileUtils.deleteQuietly(new File(fileName).getParentFile());
    }
  }

  @Test
  void saveToExistingDirectoryShouldThrowException() {

    try {
      var parameters1 =
          new PluginParametersBuilder()
              .setMessage("Björn", null)
              .setFile(new File("."), "gurka/test.txt", false, false)
              .createPluginParameters();
      var echoPlugin1 = new EchoPlugin(pluginLog, parameters1, echoOutput);

      // Create directory
      echoPlugin1.echo();

      var parameters2 =
          new PluginParametersBuilder()
              .setMessage("Björn", null)
              .setFile(new File("."), "gurka", false, false)
              .createPluginParameters();
      var echoPlugin2 = new EchoPlugin(pluginLog, parameters2, echoOutput);

      Executable testMethod = echoPlugin2::echo;

      var thrown = assertThrows(FailureException.class, testMethod);

      assertAll(
          () -> assertThat(thrown.getMessage(), startsWith("File ")),
          () -> assertThat(thrown.getMessage(), endsWith("gurka exists but is a directory")));

    } finally {
      FileUtils.deleteQuietly(new File(fileName));
    }
  }
}
