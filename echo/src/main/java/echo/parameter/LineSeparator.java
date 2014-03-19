package echo.parameter;

import echo.exception.FailureException;

import java.util.Arrays;

/**
 * Encapsulates end of line character logic.
 *
 * @author bjorn
 */
public class LineSeparator {
    private final String string;

    /**
     * Creates a line separator and makes sure that it is either &#92;n, &#92;r or &#92;r&#92;n
     *
     * @param lineSeparatorString The line separator characters
     */
    LineSeparator(final String lineSeparatorString) {
        string = lineSeparatorString.replaceAll("\\\\r", "\r").replaceAll("\\\\n", "\n");
        if (isIllegalString()) {
            throw new FailureException(
                    "LineSeparator must be either \\n, \\r or \\r\\n, but separator characters were "
                            + Arrays.toString(lineSeparatorString.getBytes()));
        }
    }

    private boolean isIllegalString() {
        return !(string.equalsIgnoreCase("\n") ||
                string.equalsIgnoreCase("\r") ||
                string.equalsIgnoreCase("\r\n"));
    }

    @Override
    public String toString() {
        return string;
    }

}
