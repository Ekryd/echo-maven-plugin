package echo;

import echo.parameter.PluginParameters;
import echo.parameter.PluginParametersBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author bjorn
 * @since 2014-01-26
 */
public class CharacterOutputTest {
    @Test
    public void emptyStringShouldBeEmptyBrackets() {
        CharacterOutput characterOutput = new CharacterOutput(new PluginParametersBuilder().setMessage("", null).createPluginParameters());
        assertEquals("[]", characterOutput.getOutput());
    }

    @Test
    public void specialCharactersShouldBeEmptyBrackets() {
        PluginParameters pluginParameters = new PluginParametersBuilder().setMessage("\u00f6\u00e4\u00e5\u00d6\u00c4\u00c5", null).createPluginParameters();
        CharacterOutput characterOutput = new CharacterOutput(pluginParameters);
        assertEquals("[['ö' , 246 ],['ä' , 228 ],['å' , 229 ],['Ö' , 214 ],['Ä' , 196 ],['Å' , 197 ]]", characterOutput.getOutput());
    }
}
