package extab.spike;

import geeksaint.point.ExcelColumnType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface ExcelColumn {
  public int order();
  public ExcelColumnType type() default ExcelColumnType.STRING;
  public String format() default "";
}