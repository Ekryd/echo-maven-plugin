package echo;

import echo.output.MavenEchoOutput;
import echo.output.MavenPluginLog;
import echo.parameter.PluginParameters;
import echo.parameter.PluginParametersBuilder;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

class TestAppendOption {
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
  void explicitAppendFlagShouldAppendToFile() throws IOException {
    var parameters =
        new PluginParametersBuilder()
            .setMessage("Björn", null)
            .setFile(new File("."), "test.txt", true, false)
            .createPluginParameters();
    var echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);

    try {
      // Echo twice
      echoPlugin.echo();
      echoPlugin.echo();

      verifyNoInteractions(echoOutput);

      var output = FileUtils.readFileToString(new File(fileName), "UTF-8");
      assertThat(output, is("BjörnBjörn"));
    } finally {
      FileUtils.deleteQuietly(new File(fileName));
    }
  }

  @Test
  void noAppendFlagShouldOverwriteFile() throws IOException {
    PluginParameters parameters;
    EchoPlugin echoPlugin;
    String output;

    try {
      parameters =
          new PluginParametersBuilder()
              .setMessage("One", null)
              .setFile(new File("."), "test.txt", false, false)
              .createPluginParameters();
      echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
      echoPlugin.echo();

      output = FileUtils.readFileToString(new File(fileName), "UTF-8");
      assertThat(output, is("One"));

      parameters =
          new PluginParametersBuilder()
              .setMessage("Two", null)
              .setFile(new File("."), "test.txt", false, false)
              .createPluginParameters();
      echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
      echoPlugin.echo();

      verifyNoInteractions(echoOutput);

      output = FileUtils.readFileToString(new File(fileName), "UTF-8");
      assertThat(output, is("Two"));
    } finally {
      FileUtils.deleteQuietly(new File(fileName));
    }
  }
}
