package enums;

/**
 * Enum class rlisting supported screenshot taking types.<br>
 * FAILED - only take screenshot upon a failed scenario<br>
 * PASSED - only take screenshot upon a passed scenario<br>
 * ALL - only take screenshot upon a scenario ireelevant to its status
 *
 * @author Andras Fuge
 */
public enum ScreenshotMode {
    FAILED,
    PASSED,
    ALL
}
