package geeksaint.extab;

import geeksaint.extab.exceptions.PointAnnotationNotFoundException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class AnnotationProcessor {
  private final Class claz;

  public static AnnotationProcessor process(Class claz){
    return new AnnotationProcessor(claz);
  }
  private AnnotationProcessor(Class claz) {
    this.claz = claz;
  }


  public String getTableName(){
    ExcelTable annotation = getTableAnnotation();
    return annotation.name();
  }

  public String getTableComments() {
    ExcelTable annotation = getTableAnnotation();
    return annotation.comments();
  }

  public Integer getSkipRows() {
    ExcelTable annotation = null;
    try{
    annotation = getTableAnnotation();
    }catch(PointAnnotationNotFoundException e){
      return 0;
    }
    return annotation.skipRows();
  }

  public Integer getSkipColumns() {
    ExcelTable annotation = null;
    try{
      annotation = getTableAnnotation();
    }catch(PointAnnotationNotFoundException e){
      return 0;
    }
    return annotation.skipColumns();
  }

  public Integer getFieldColumnOrder(String fieldName) throws NoSuchFieldException {
    ExcelColumn columnAnnotation = getColumnAnnotationOnField(fieldName);
    return (columnAnnotation == null) ? null : columnAnnotation.order();
  }

  public Integer getMethodColumnOrder(String methodName) {
    ExcelColumn columnAnnotation = getColumnAnnotationOnMethod(methodName);
    return (columnAnnotation == null) ? null : columnAnnotation.order();
  }

  public ExcelColumnType getFieldColumnType(String fieldName) throws NoSuchFieldException {
    ExcelColumn columnAnnotation = getColumnAnnotationOnField(fieldName);
    return (columnAnnotation == null) ? null : columnAnnotation.type();
  }

  public ExcelColumnType getMethodColumnType(String methodName) {
    ExcelColumn columnAnnotation = getColumnAnnotationOnMethod(methodName);
    return (columnAnnotation == null) ? null : columnAnnotation.type();
  }

  private ExcelTable getTableAnnotation() {
    ExcelTable annotation = (ExcelTable) claz.getAnnotation(ExcelTable.class);
    if(annotation == null) throw new PointAnnotationNotFoundException();;
    return annotation;
  }

  private ExcelColumn getColumnAnnotationOnField(String fieldName) throws NoSuchFieldException {
    Field field = claz.getDeclaredField(fieldName);
    return getAnnotationOn(field);
  }

  private ExcelColumn getColumnAnnotationOnMethod(String methodName) {
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

  public String getFieldColumnFormat(String fieldName) throws NoSuchFieldException {
    ExcelColumn columnAnnotation = getColumnAnnotationOnField(fieldName);
    return (columnAnnotation == null) ? null : columnAnnotation.format();
  }

  public String getMethodColumnFormat(String methodName) {
      ExcelColumn columnAnnotation = getColumnAnnotationOnMethod(methodName);
      return (columnAnnotation == null) ? null : columnAnnotation.format();
  }
}
