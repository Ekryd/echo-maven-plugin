package echo.parameter;

import echo.exception.FailureException;

/**
 * Defines the different levels a message can be output to. INFO is the default level
 *
 * @author bjorn
 * @since 2012-08-11
 */
public enum OutputLevelType {
    FAIL, ERROR, WARNING, INFO, DEBUG;
    private static final String ERROR_LEVEL_MSG = "level must be either FAIL, ERROR, WARNING, INFO or DEBUG.";

    /** Converts a string to the corresponding context */
    static OutputLevelType fromString(String level) {
        if (level == null) {
            throw new FailureException(ERROR_LEVEL_MSG + " Was: null");
        }
        for (OutputLevelType outputLevelType : values()) {
            if (outputLevelType.name().equalsIgnoreCase(level)) {
                return outputLevelType;
            }
        }
        throw new FailureException(ERROR_LEVEL_MSG + " Was: " + level);
    }
}
