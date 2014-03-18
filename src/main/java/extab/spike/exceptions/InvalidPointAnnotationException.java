package extab.spike.exceptions;

public class InvalidPointAnnotationException extends RuntimeException{

  public InvalidPointAnnotationException(Throwable cause){
    this(cause, cause.getMessage());
  }

  public InvalidPointAnnotationException(Throwable cause, String message) {
    super(message, cause);
  }

  public InvalidPointAnnotationException(String message) {
    super(message);
  }
}
