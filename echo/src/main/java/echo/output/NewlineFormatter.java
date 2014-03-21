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

    /**
     * Creates a new instance of NewlineSeparator
     *
     * @param pluginParameters The user-supplied plugin parameters
     */
    public NewlineFormatter(PluginParameters pluginParameters) {
        this.lineSeparator = pluginParameters.getLineSeparator().getFormattedLineSeparator();
    }

    /** Format the message with the desired end of line characters */
    public String format(String output) {
        return output.replaceAll(NEWLINES_REG_EX, lineSeparator);
    }
}
