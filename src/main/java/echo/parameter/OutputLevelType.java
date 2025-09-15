package echo.parameter;

import echo.exception.FailureException;
import java.util.Locale;
import java.util.Set;

/** Defines the different levels a message can be output to. INFO is the default level */
public enum OutputLevelType {
  FAIL(Set.of("FAIL", "FATAL")),
  ERROR(Set.of("ERROR")),
  WARNING(Set.of("WARNING", "WARN")),
  INFO(Set.of("INFO")),
  DEBUG(Set.of("DEBUG", "TRACE"));

  private final Set<String> matchName;

  private static final String ERROR_LEVEL_MSG =
      "level must be either FAIL, ERROR, WARNING, INFO or DEBUG.";

  OutputLevelType(Set<String> matchName) {
    this.matchName = matchName;
  }

  /** Converts a string to the corresponding context */
  static OutputLevelType fromString(String level) {
    if (level == null) {
      throw new FailureException(ERROR_LEVEL_MSG + " Was: null");
    }
    var upperCaseLevel = level.toUpperCase(Locale.getDefault());
    for (var outputLevelType : values()) {
      if (outputLevelType.matchName.contains(upperCaseLevel)) {
        return outputLevelType;
      }
    }
    throw new FailureException(ERROR_LEVEL_MSG + " Was: " + level);
  }
}
