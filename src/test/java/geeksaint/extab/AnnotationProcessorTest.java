package geeksaint.extab;

import geeksaint.extab.exceptions.PointAnnotationNotFoundException;
import lombok.Getter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static geeksaint.extab.AnnotationProcessor.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class AnnotationProcessorTest {

  private AnnotationProcessor processorForLineItem;
  private AnnotationProcessor processorForLineItemWithBadFields;

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
    processorForLineItem = process(LineItem.class);
    processorForLineItemWithBadFields = process(LineItemWithBadFields.class);
  }

  @Test
  public void shouldReadOffsets(){
    assertThat(processorForLineItem.getSkipRows(), is(2));
    assertThat(processorForLineItem.getSkipColumns(), is(3));
  }

  @Test
  public void shouldThrowExceptionIfTargetClassNotAnnotatedForName(){
    thrown.expect(PointAnnotationNotFoundException.class);
    process(String.class).getTableName();
  }

  @Test
  public void shouldReadName(){
    assertThat(processorForLineItem.getTableName(), is(" LineItem"));
  }

  @Test
  public void shouldReadComments(){
    assertThat(processorForLineItem.getTableComments(), is("A line item"));
  }

  @Test
  public void shouldReadFieldAnnotations() throws NoSuchFieldException {
    assertThat(processorForLineItem.getFieldColumnOrder("fieldOne"), is(1));
    assertThat(processorForLineItem.getFieldColumnType("fieldOne"), is(ExcelColumnType.STRING));
  }
  @Test
  public void shouldReadMethodAnnotations() throws NoSuchFieldException {
    assertThat(processorForLineItem.getMethodColumnOrder("setFieldTwo"), is(2));
    assertThat(processorForLineItem.getMethodColumnType("setFieldTwo"), is(ExcelColumnType.STRING));
  }

//  @Test
//  public void shouldThrowExceptionIfTargetMethodDoesNotHaveOnlyOneStringArgument(){
//    thrown.expect(InvalidPointAnnotationException.class);
//    processorForLineItem.getMethodColumnOrder( LineItemWithBadFields.class, "aBadMethod");
//  }

//  @Test
//  public void shouldThrowExceptionIfTargetMethodDoesNotHaveAStringArgument(){
//    thrown.expect(InvalidPointAnnotationException.class);
//    processorForLineItem.getMethodColumnOrder( LineItemWithBadFields.class, "anotherBadMethod");
//  }

  @Test
  public void shouldReturnNullIfTheFieldIsNotAnnotated() throws NoSuchFieldException {
    assertThat(processorForLineItemWithBadFields.getFieldColumnOrder("badField"), is(nullValue()));
    assertThat(processorForLineItemWithBadFields.getFieldColumnType("badField"), is(nullValue()));
  }

  @Test
  public void shouldReadColumnFormatType() throws NoSuchFieldException {
    assertThat(processorForLineItem.getFieldColumnFormat("fieldOne"), is("some format"));
    assertThat(processorForLineItem.getMethodColumnFormat("setFieldTwo"), is("other format"));
  }

  @Test
  public void shouldReturnNullIfTheMethodIsNotAnnotated() throws NoSuchFieldException {
    assertThat(processorForLineItem.getMethodColumnOrder("badMethod"), is(nullValue()));
    assertThat(processorForLineItem.getMethodColumnType("badMethod"), is(nullValue()));
  }

  @Test
  public void shouldReturnZeroRowsAndColumnsToSkipIfClassIsNotAnnotated(){
    class NotAnnotatedClass{}
    AnnotationProcessor processor = process(NotAnnotatedClass.class);
    assertThat(processor.getSkipRows(), is(0));
    assertThat(processor.getSkipColumns(), is(0));
  }
}
