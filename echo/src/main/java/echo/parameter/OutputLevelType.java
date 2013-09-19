package echo.parameter;

import echo.exception.FailureException;

/**
 * @author bjorn
 * @since 2012-08-11
 */
public enum OutputLevelType {
    ERROR, WARNING, INFO, VERBOSE, DEBUG;

    static OutputLevelType fromString(String level) {
        if (level == null) {
            throw new FailureException("verifyFail must be either ERROR, WARNING, INFO, VERBOSE or DEBUG. Was: null");
        }
        for (OutputLevelType outputLevelType : values()) {
            if (outputLevelType.name().equalsIgnoreCase(level))
                return outputLevelType;
        }
        throw new FailureException("verifyFail must be either ERROR, WARNING, INFO, VERBOSE or DEBUG. Was: " + level);
    }
}
