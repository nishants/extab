package extab.spike.exceptions;

public class PointAnnotationNotFoundException extends RuntimeException{

  public static final String DEFAULT_MESSAGE = "Class not annotated with @ExcelTable";

  public PointAnnotationNotFoundException() {
    super(DEFAULT_MESSAGE);
  }
}
