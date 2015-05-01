package echo.parameter;

import echo.exception.FailureException;

import java.util.Arrays;

/**
 * Encapsulates end of line character logic.
 *
 * @author bjorn
 */
public class LineSeparator {
    private final String formattedLineSeparator;
    private final String lineSeparatorString;

    /**
     * Creates a line separator and makes sure that it is either &#92;n, &#92;r or &#92;r&#92;n
     *
     * @param lineSeparatorString The line separator characters
     */
    LineSeparator(final String lineSeparatorString) {
        this.lineSeparatorString = lineSeparatorString;
        this.formattedLineSeparator = lineSeparatorString.replaceAll("\\\\r", "\r").replaceAll("\\\\n", "\n");
    }

    void checkLineSeparator() {
        if (isIllegalString()) {
            throw new FailureException(
                    "LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were "
                            + Arrays.toString(lineSeparatorString.getBytes()));
        }
    }

    private boolean isIllegalString() {
        return !("\n".equalsIgnoreCase(formattedLineSeparator) ||
                "\r".equalsIgnoreCase(formattedLineSeparator) ||
                "\r\n".equalsIgnoreCase(formattedLineSeparator));
    }

    public String getFormattedLineSeparator() {
        return formattedLineSeparator;
    }
}
