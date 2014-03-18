package extab.spike;

import extab.spike.exceptions.PointAnnotationNotFoundException;
import geeksaint.point.ExcelColumnType;
import lombok.Getter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class AnnotationProcessorTest {

  private AnnotationProcessor processor;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Getter
  @ExcelTable(name=" LineItem", comments ="A line item", skipRows = 2, skipColumns = 3)
  public static class  LineItem{
    @ExcelColumn(order=1, format="some format") private String fieldOne;
    @ExcelColumn(order=2, format="other format") private void setFieldTwo(String arg){}
  }

  @ExcelTable(name=" LineItemWithBadFields", comments ="A line item with bad fields")
  public static class  LineItemWithBadFields {
    @Getter
    @ExcelColumn(order=1) private String fieldOne;
    @ExcelColumn(order=2) private void setBadField(String value){badField = value+"-updated";}
    @ExcelColumn(order=3) private void aBadMethod(String value, Object some){}
    @ExcelColumn(order=4) private void anotherBadMethod(String value, Object some){}
    private String badField;
    private void badMethod(String some){}
  }

  @Before
  public void setUp() throws Exception {
    processor = new AnnotationProcessor();
  }

  @Test
  public void shouldReadOffsets(){
    assertThat(processor.getSkipRows( LineItem.class), is(2));
    assertThat(processor.getSkipColumns( LineItem.class), is(3));
  }

  @Test
  public void shouldThrowExceptionIfTargetClassNotAnnotatedForName(){
    thrown.expect(PointAnnotationNotFoundException.class);
    processor.getTableName(java.lang.String.class);
  }

  @Test
  public void shouldReadName(){
    assertThat(processor.getTableName( LineItem.class), is(" LineItem"));
  }

  @Test
  public void shouldReadComments(){
    assertThat(processor.getTableComments( LineItem.class), is("A line item"));
  }

  @Test
  public void shouldReadFieldAnnotations() throws NoSuchFieldException {
    assertThat(processor.getFieldColumnOrder( LineItem.class, "fieldOne"), is(1));
    assertThat(processor.getFieldColumnType( LineItem.class, "fieldOne"), is(ExcelColumnType.STRING));
  }
  @Test
  public void shouldReadMethodAnnotations() throws NoSuchFieldException {
    assertThat(processor.getMethodColumnOrder( LineItem.class, "setFieldTwo"), is(2));
    assertThat(processor.getMethodColumnType( LineItem.class, "setFieldTwo"), is(ExcelColumnType.STRING));
  }

//  @Test
//  public void shouldThrowExceptionIfTargetMethodDoesNotHaveOnlyOneStringArgument(){
//    thrown.expect(InvalidPointAnnotationException.class);
//    processor.getMethodColumnOrder( LineItemWithBadFields.class, "aBadMethod");
//  }

//  @Test
//  public void shouldThrowExceptionIfTargetMethodDoesNotHaveAStringArgument(){
//    thrown.expect(InvalidPointAnnotationException.class);
//    processor.getMethodColumnOrder( LineItemWithBadFields.class, "anotherBadMethod");
//  }

  @Test
  public void shouldReturnNullIfTheFieldIsNotAnnotated() throws NoSuchFieldException {
    assertThat(processor.getFieldColumnOrder( LineItemWithBadFields.class, "badField"), is(nullValue()));
    assertThat(processor.getFieldColumnType( LineItemWithBadFields.class, "badField"), is(nullValue()));
  }

  @Test
  public void shouldReadColumnFormatType() throws NoSuchFieldException {
    assertThat(processor.getFieldColumnFormat(LineItem.class, "fieldOne"), is("some format"));
    assertThat(processor.getMethodColumnFormat(LineItem.class, "setFieldTwo"), is("other format"));
  }

  @Test
  public void shouldReturnNullIfTheMethodIsNotAnnotated() throws NoSuchFieldException {
    assertThat(processor.getMethodColumnOrder(LineItemWithBadFields.class, "badMethod"), is(nullValue()));
    assertThat(processor.getMethodColumnType(LineItemWithBadFields.class, "badMethod"), is(nullValue()));
  }

  @Test
  public void shouldReturnZeroRowsAndColumnsToSkipIfClassIsNotAnnotated(){
    class NotAnnotatedClass{}
    assertThat(processor.getSkipRows( NotAnnotatedClass.class), is(0));
    assertThat(processor.getSkipColumns( NotAnnotatedClass.class), is(0));
  }
}
