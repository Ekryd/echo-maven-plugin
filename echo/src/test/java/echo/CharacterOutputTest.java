package echo;

import echo.output.EchoOutput;
import echo.output.PluginLog;
import echo.parameter.PluginParameters;
import echo.parameter.PluginParametersBuilder;
import org.junit.jupiter.api.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @author bjorn
 * @since 2014-01-26
 */
class CharacterOutputTest {
    private final PluginLog pluginLog = mock(PluginLog.class);
    private final EchoOutput echoOutput = mock(EchoOutput.class);

    @Test
    void noDebugShouldReturnZeroString() {
        CharacterOutput characterOutput = new CharacterOutput(new PluginParametersBuilder().setDebug(false).createPluginParameters());
        assertEquals("", characterOutput.getOutput(""));
    }

    @Test
    void emptyStringShouldBeEmptyBrackets() {
        CharacterOutput characterOutput = new CharacterOutput(new PluginParametersBuilder().setDebug(true).createPluginParameters());
        assertEquals("[]", characterOutput.getOutput(""));
    }

    @Test
    void specialCharactersShouldBeOutput() {
        PluginParameters pluginParameters = new PluginParametersBuilder().setDebug(true).createPluginParameters();
        CharacterOutput characterOutput = new CharacterOutput(pluginParameters);
        assertThat(characterOutput.getOutput("\u00f6\u00e4\u00e5\u00d6\u00c4\u00c5"), is("[['ö' , 246 ],['ä' , 228 ],['å' , 229 ],['Ö' , 214 ],['Ä' , 196 ],['Å' , 197 ]]"));
    }
    
        @Test
    void foundFileInClassPathShouldOutputToInfo() {
        PluginParameters parameters = new PluginParametersBuilder().setMessage(null, "messageText.txt").setDebug(true).createPluginParameters();
        EchoPlugin echoPlugin = new EchoPlugin(pluginLog, parameters, echoOutput);
        echoPlugin.echo();

        verify(pluginLog).info("[['B' , 66 ],['j' , 106 ],['ö' , 246 ],['r' , 114 ],['n' , 110 ]]");
        verify(echoOutput).info("Björn");
    }

}
