package echo.parameter;

import echo.exception.FailureException;
import java.util.Locale;
import java.util.function.Function;

/** Defines the different levels a message can be output to. INFO is the default level */
public enum OutputLevelType {
  FAIL(name -> "FAIL".equals(name) || "FATAL".equals(name)),
  ERROR("ERROR"::equals),
  WARNING(name -> "WARNING".equals(name) || "WARN".equals(name)),
  INFO("INFO"::equals),
  DEBUG(name -> "DEBUG".equals(name) || "TRACE".equals(name));

  private final Function<String, Boolean> matchName;

  private static final String ERROR_LEVEL_MSG =
      "level must be either FAIL, ERROR, WARNING, INFO or DEBUG.";

  OutputLevelType(Function<String, Boolean> matchName) {
    this.matchName = matchName;
  }

  /** Converts a string to the corresponding context */
  static OutputLevelType fromString(String level) {
    if (level == null) {
      throw new FailureException(ERROR_LEVEL_MSG + " Was: null");
    }
    var upperCaseLevel = level.toUpperCase(Locale.getDefault());
    for (var outputLevelType : values()) {
      if (Boolean.TRUE.equals(outputLevelType.matchName.apply(upperCaseLevel))) {
        return outputLevelType;
      }
    }
    throw new FailureException(ERROR_LEVEL_MSG + " Was: " + level);
  }
}
