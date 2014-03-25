package geeksaint.extab.exceptions;

public class InvalidTargetTypeException extends RuntimeException {
  public InvalidTargetTypeException(Throwable exception) {
    super(exception);
  }

  public InvalidTargetTypeException(String message) {
    super(message);
  }
}
