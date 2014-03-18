package extab.spike.exceptions;

public class InvalidDataException extends RuntimeException {
  public InvalidDataException(String message, Throwable e) {
    super(message, e);
  }

  public InvalidDataException(String format) {
    super(format);
  }
}
