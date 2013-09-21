package echo.output;

import echo.parameter.PluginParameters;

/**
 * @author bjorn
 * @since 2013-09-20
 */
public class NewlineFormatter {
    private static final String NEWLINES_REG_EX = "\\r\\n|\\r|\\n";
    private final String lineSeparator;

    public NewlineFormatter(PluginParameters pluginParameters) {
        this.lineSeparator = pluginParameters.lineSeparator.toString();
    }

    public String format(String output) {
        return output.replaceAll(NEWLINES_REG_EX, lineSeparator);
    }
}
