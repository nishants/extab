package extab.spike;

import extab.spike.exceptions.InvalidPointAnnotationException;
import extab.spike.exceptions.PointAnnotationNotFoundException;
import geeksaint.point.ExcelColumnType;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static java.lang.String.format;

public class AnnotationProcessor {

  private static final String INVALID_METHOD_SIGNATURE_MESSAGE =
      "Method '%1$s' must have only one String argument.Expected method with " +
          "signature '%1$s(String arg)'";

  public String getTableName(Class claz){
    ExcelTable annotation = getTableAnnotation(claz);
    return annotation.name();
  }

  public String getTableComments(Class claz) {
    ExcelTable annotation = getTableAnnotation(claz);
    return annotation.comments();
  }

  public Integer getSkipRows(Class claz) {
    ExcelTable annotation = null;
    try{
    annotation = getTableAnnotation(claz);
    }catch(PointAnnotationNotFoundException e){
      return 0;
    }
    return annotation.skipRows();
  }

  public Integer getSkipColumns(Class claz) {
    ExcelTable annotation = null;
    try{
      annotation = getTableAnnotation(claz);
    }catch(PointAnnotationNotFoundException e){
      return 0;
    }
    return annotation.skipColumns();
  }

  public Integer getFieldColumnOrder(Class claz, String fieldName) throws NoSuchFieldException {
    ExcelColumn columnAnnotation = getColumnAnnotationOnField(claz, fieldName);
    return (columnAnnotation == null) ? null : columnAnnotation.order();
  }

  public Integer getMethodColumnOrder(Class claz, String methodName) {
    ExcelColumn columnAnnotation = getColumnAnnotationOnMethod(claz, methodName);
    return (columnAnnotation == null) ? null : columnAnnotation.order();
  }

  public ExcelColumnType getFieldColumnType(Class claz, String fieldName) throws NoSuchFieldException {
    ExcelColumn columnAnnotation = getColumnAnnotationOnField(claz, fieldName);
    return (columnAnnotation == null) ? null : columnAnnotation.type();
  }

  public ExcelColumnType getMethodColumnType(Class claz, String methodName) {
    ExcelColumn columnAnnotation = getColumnAnnotationOnMethod(claz, methodName);
    return (columnAnnotation == null) ? null : columnAnnotation.type();
  }

  private ExcelTable getTableAnnotation(Class claz) {
    ExcelTable annotation = (ExcelTable) claz.getAnnotation(ExcelTable.class);
    if(annotation == null) throw new PointAnnotationNotFoundException();;
    return annotation;
  }

  private ExcelColumn getColumnAnnotationOnField(Class claz, String fieldName) throws NoSuchFieldException {
    Field field = claz.getDeclaredField(fieldName);
    return getAnnotationOn(field);
  }

  private ExcelColumn getColumnAnnotationOnMethod(Class claz, String methodName) {
    Method method = null;
    try {
      method = claz.getDeclaredMethod(methodName, String.class);
    } catch (NoSuchMethodException e) {
      return null;
    }
    return getAnnotationOn(method);
  }

  private ExcelColumn getAnnotationOn(AccessibleObject member) {
    ExcelColumn annotation = member.getAnnotation(ExcelColumn.class);
    return annotation;
  }

  private void throwInvalidException(String messageFormat, Object params) {
    throw new InvalidPointAnnotationException(format(messageFormat, params));
  }

  public String getFieldColumnFormat(Class claz, String fieldName) throws NoSuchFieldException {
    ExcelColumn columnAnnotation = getColumnAnnotationOnField(claz, fieldName);
    return (columnAnnotation == null) ? null : columnAnnotation.format();
  }

  public String getMethodColumnFormat(Class claz, String methodName) {
      ExcelColumn columnAnnotation = getColumnAnnotationOnMethod(claz, methodName);
      return (columnAnnotation == null) ? null : columnAnnotation.format();
  }
}
