package geeksaint.extab;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import lombok.Setter;
import lombok.Getter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class AnnotationReaderTest {

  private AnnotationReader processor;

  @Before
  public void setUp() throws Exception {
    processor = new AnnotationReader(LineItem.class);
  }

  @Setter
  @Getter
  @ExcelTable(fromRow = 2, fromColumn = 3)
  public static class  LineItem{
    @ExcelColumn(order=1) private String fieldOne;
    @ExcelColumn(order=1, format="some-format") private String fieldTwo;
    @ExcelColumn(order=2) private void setFieldThree(String arg){}
    @ExcelColumn(order=2, format="other-format") private void setFieldFour(String arg){}
  }

  @Test
  public void shouldReadOffsets() throws AnnotationNotFoundException {
    assertThat(processor.readFromRow(), is(2));
    assertThat(processor.readFromColumn(), is(3));
  }

  @Test
  public void shouldReturnZeroRowsAndColumnsToSkipIfClassIsNotAnnotated() throws AnnotationNotFoundException {
    @ExcelTable class NotAnnotatedClass{}
    processor = new AnnotationReader(NotAnnotatedClass.class);

    assertThat(processor.readFromRow(), is(0));
    assertThat(processor.readFromColumn(), is(0));
  }

  @Test
  public void shouldReadFieldAnnotations() throws InvalidAnnotationTargetException {
    assertThat(processor.getColumnOrderForField("fieldOne"), is(1));
    assertThat(processor.getColumnTypeForField("fieldOne"), is(ExcelColumnType.STRING));
  }

  @Test
  public void shouldReadMethodAnnotations() throws InvalidAnnotationTargetException {
    assertThat(processor.getColumnOrderForMethod("setFieldThree"), is(2));
    assertThat(processor.getColumnTypeForMethod("setFieldThree"), is(ExcelColumnType.STRING));
  }

  @Ignore
  @Test
  public void shouldReturnNullIfTheFieldIsNotAnnotated() throws InvalidAnnotationTargetException {
    assertThat(processor.getColumnOrderForField("badField"), is(nullValue()));
    assertThat(processor.getColumnTypeForField("badField"), is(nullValue()));
  }

  @Test
  public void shouldReadColumnFormatType() throws InvalidAnnotationTargetException {
    assertThat(processor.getColumnFormatForField("fieldTwo"), is("some-format"));
    assertThat(processor.getColumnFormatForMethod("setFieldFour"), is("other-format"));
  }

  @Test
  public void shouldReturnFalseIfTargetTypeIsNotAnnotated(){
    class NotAnnotatedClass{}
    processor = new AnnotationReader(NotAnnotatedClass.class);
    assertThat(processor.isAnnotated(), is(false));
  }

  @Test
  public void shouldReturnTrueIfTargetTypeIsAnnotated(){
    assertThat(processor.isAnnotated(), is(true));
  }
}
