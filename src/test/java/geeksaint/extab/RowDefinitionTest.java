package geeksaint.extab;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RowDefinitionTest {

  @Test
  public void shouldGetRowDefinition() throws NoSuchFieldException {
    @ToString
    @EqualsAndHashCode
    class TestClass{
      @ExcelColumn(order = 1) private int columnOne;
      @ExcelColumn(order = 2) private int columnTwo;
      @ExcelColumn(order = 3) private int columnThree;
      @ExcelColumn(order = 4) private int columnFour;
      @ExcelColumn(order = 5, type=ExcelColumnType.DATE, format="MM/dd/yyyy") private void aMethod(String arg){};
    }

    AnnotationProcessor annotationProcessor = mock(AnnotationProcessor.class);
    when(annotationProcessor.getFieldColumnType("columnOne")).thenReturn(ExcelColumnType.STRING);
    when(annotationProcessor.getFieldColumnOrder("columnOne")).thenReturn(1);
    when(annotationProcessor.getFieldColumnFormat("columnOne")).thenReturn("");

    when(annotationProcessor.getFieldColumnType("columnTwo")).thenReturn(ExcelColumnType.STRING);
    when(annotationProcessor.getFieldColumnOrder("columnTwo")).thenReturn(2);
    when(annotationProcessor.getFieldColumnFormat("columnTwo")).thenReturn("");


    when(annotationProcessor.getFieldColumnType("columnThree")).thenReturn(ExcelColumnType.STRING);
    when(annotationProcessor.getFieldColumnOrder("columnThree")).thenReturn(3);
    when(annotationProcessor.getFieldColumnFormat("columnThree")).thenReturn("");


    when(annotationProcessor.getFieldColumnType("columnFour")).thenReturn(ExcelColumnType.STRING);
    when(annotationProcessor.getFieldColumnOrder("columnFour")).thenReturn(4);
    when(annotationProcessor.getFieldColumnFormat("columnFour")).thenReturn("");

    when(annotationProcessor.getMethodColumnType("aMethod")).thenReturn(ExcelColumnType.DATE);
    when(annotationProcessor.getMethodColumnOrder("aMethod")).thenReturn(5);
    when(annotationProcessor.getMethodColumnFormat("aMethod")).thenReturn("MM/dd/yyyy");

    when(annotationProcessor.getSkipRows()).thenReturn(3);
    when(annotationProcessor.getSkipColumns()).thenReturn(1);

    RowDefinition rowDefinition = new RowDefinition(TestClass.class, annotationProcessor) ;

    ColumnDefinition[] expectedRowDefinition = new ColumnDefinition[]{
      new ColumnDefinition(1, ExcelColumnType.STRING, ""),
      new ColumnDefinition(2, ExcelColumnType.STRING, ""),
      new ColumnDefinition(3, ExcelColumnType.STRING, ""),
      new ColumnDefinition(4, ExcelColumnType.STRING, ""),
      new ColumnDefinition(5, ExcelColumnType.DATE, "MM/dd/yyyy")
    } ;

    ColumnDefinition[] actualMetadata = rowDefinition.getRowDefinition();
    assertArrayEquals(actualMetadata, expectedRowDefinition);
  }

  @Test
  public void shouldHaveSkipValues(){
    class TestSkipClass{}
    AnnotationProcessor annotationProcessor = mock(AnnotationProcessor.class);
    when(annotationProcessor.getSkipRows()).thenReturn(3);
    when(annotationProcessor.getSkipColumns()).thenReturn(1);

    RowDefinition rowDefinition = new RowDefinition(TestSkipClass.class, annotationProcessor) ;

    assertThat(rowDefinition.getRowsToSkip(), is(3));
    assertThat(rowDefinition.getColumnsToSkip(), is(1));
  }
}
