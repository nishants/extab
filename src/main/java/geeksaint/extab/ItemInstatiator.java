package geeksaint.extab;

import geeksaint.extab.exceptions.InvalidTargetTypeException;
import geeksaint.extab.exceptions.MethodInvocationException;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemInstatiator {
  private static final String COULD_NOT_INSTANTIATE_MESSAGE = "Please ensure the target class has public default constructor and is not abstract type";
  private final AnnotationProcessor processor;
  private final Class targetClass;
  private final Field rowNumField;
  private List<Method> annotatedMethods = new ArrayList<Method>();
  private List<Field>  annotatedFields  = new ArrayList<Field>();

  public ItemInstatiator(Class targetClass) {
    this.targetClass = targetClass;
    processor = new AnnotationProcessor();
    rowNumField = getRowNumField();
  }

  public List createItems(Map<Integer, List> rows) {
    List createdItems = new ArrayList();
    for(int rowNum : rows.keySet()){
      List rowValues = rows.get(rowNum);
      createdItems.add(rowNum, createItem(rowValues));
    }
    return createdItems;
  }

  public Object createItem(List rowValues) {
    Object item = createNewInstance();
    setFieldValues(rowValues, item);
    invokeMethods(rowValues, item);
    return item;
  }

  //Row number is a one based index( as excel follows the same)
  public Object createItem(List rowValues, int rowNumber) {
    Object item = createNewInstance();
    setFieldValues(rowValues, item);
    invokeMethods(rowValues, item);
    setRowNumber(item, rowNumber);
    return item;
  }

  private void setRowNumber(Object item, Integer value){
    if(rowNumField != null){
      setFieldValue(item, rowNumField, value);
    }
  }

  private Field getRowNumField(){
    for(Field field : targetClass.getDeclaredFields()){
      if(isAnnotated(field, RowNum.class)) return field;
    }
    return null;
  }

  private void invokeMethods(List rowValues, Object item) {
    for(Method method : getAnnotatedMethods()){
      int index = getMethodOrder(method.getName()) - 1;
      Object value = rowValues.get(index);
      invokeMethodWithArgument(item, method, value);
    }
  }

  private void setFieldValues(List rowValues, Object item) {
    for(Field field : getAnnotatedFields()){
      int index = getFieldOrder(field.getName()) - 1;
      Object value = rowValues.get(index);
      setFieldValue(item, field, value);
    }
  }

  private Object createNewInstance() {
    try {
      return targetClass.newInstance();
    } catch (InstantiationException e) {
      throw new InvalidTargetTypeException(COULD_NOT_INSTANTIATE_MESSAGE);
    } catch (IllegalAccessException e) {
      throw new InvalidTargetTypeException(e);
    }
  }

  private void setFieldValue(Object item, Field field, Object value){
    try {
      field.setAccessible(true);
      field.set(item, value);
    } catch (IllegalAccessException e) {}
  }

  private void invokeMethodWithArgument(Object item, Method method, Object value){
    try {
      method.setAccessible(true);
      method.invoke(item, value);
    } catch (IllegalAccessException e) {
      throw new MethodInvocationException(e);
    }
    catch (InvocationTargetException e) {
      throw new MethodInvocationException(e);
    }
  }

  private int getFieldOrder(String fieldName){
    try {
      return processor.getFieldColumnOrder(targetClass, fieldName);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    }
  }

  private int getMethodOrder(String methodName) {
      return processor.getMethodColumnOrder(targetClass, methodName);
  }

  private synchronized List<Method> getAnnotatedMethods() {
    if(!annotatedMethods.isEmpty()) return annotatedMethods;
    for(Method method : targetClass.getDeclaredMethods()){
      if(isAnnotated(method, ExcelColumn.class)) annotatedMethods.add(method);
    }
    return annotatedMethods;
  }

  private synchronized List<Field> getAnnotatedFields() {
    if(!annotatedFields.isEmpty())  return annotatedFields;
    for(Field field : targetClass.getDeclaredFields()){
      if(isAnnotated(field, ExcelColumn.class)) annotatedFields.add(field);
    }
    return annotatedFields;
  }

  private boolean isAnnotated(AccessibleObject member, Class fieldAnnotationClass) {
    return null != member.getAnnotation(fieldAnnotationClass);
  }
}