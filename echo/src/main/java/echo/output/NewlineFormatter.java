package echo.output;

import echo.parameter.PluginParameters;

/**
 * Formats end of line characters according to desired format
 *
 * @author bjorn
 * @since 2013-09-20
 */
public class NewlineFormatter {
    private static final String NEWLINES_REG_EX = "\\r\\n|\\r|\\n";
    private final String lineSeparator;

    public NewlineFormatter(PluginParameters pluginParameters) {
        this.lineSeparator = pluginParameters.getLineSeparator().toString();
    }

    /** Format the message with the desired end of line characters */
    public String format(String output) {
        return output.replaceAll(NEWLINES_REG_EX, lineSeparator);
    }
}
