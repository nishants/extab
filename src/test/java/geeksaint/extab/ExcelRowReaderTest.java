package geeksaint.extab;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Before;
import org.junit.Test;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExcelRowReaderTest {

  private ExcelColumnType columnOneType;
  private ExcelColumnType columnTwoType;
  private ExcelColumnType columnThreeType;
  private ColumnDefinition[] rowDefinitionArr;
  private RowDefinition rowDefinition;
  private Row row;
  private Cell cellOne;
  private Cell cellTwo;
  private Cell cellThree;

  @Before
  public void setUp() throws Exception {
    columnOneType = mock(ExcelColumnType.class);
    columnTwoType = mock(ExcelColumnType.class);
    columnThreeType = mock(ExcelColumnType.class);
    rowDefinitionArr = new ColumnDefinition[]{
        new ColumnDefinition(1, columnOneType, ""),
        new ColumnDefinition(1, columnTwoType, ""),
        new ColumnDefinition(1, columnThreeType, "")
    };
    rowDefinition = mock(RowDefinition.class);

    row = mock(Row.class);
    cellOne = mock(Cell.class);
    cellTwo = mock(Cell.class);
    cellThree = mock(Cell.class);

    when(rowDefinition.getRowDefinition()).thenReturn(rowDefinitionArr);
    when(columnOneType.getCellValue(cellOne, "")).thenReturn("one");
    when(columnTwoType.getCellValue(cellTwo, "")).thenReturn("two");
    when(columnThreeType.getCellValue(cellThree, "")).thenReturn("three");
  }

  @Test
  public void shouldReturnRowData(){
    ItemInstatiator instatiator = mock(ItemInstatiator.class);

    Object object = new Object();
    when(instatiator.createItem(asList("one", "two", "three"), 101)).thenReturn(object);
    when(row.getRowNum()).thenReturn(100);
    when(row.getCell(0)).thenReturn(cellOne);
    when(row.getCell(1)).thenReturn(cellTwo);
    when(row.getCell(2)).thenReturn(cellThree);

    ExcelRowReader reader = new ExcelRowReader(rowDefinition, instatiator);
    assertThat(reader.getData(row), is(object));
  }

  @Test
  public void shouldSkipColumns(){
    ItemInstatiator instatiator = mock(ItemInstatiator.class);
    Object object = new Object();

    when(row.getRowNum()).thenReturn(0);
    when(row.getCell(2)).thenReturn(cellOne);
    when(row.getCell(3)).thenReturn(cellTwo);
    when(row.getCell(4)).thenReturn(cellThree);

    when(instatiator.createItem(asList("one", "two", "three"), 1)).thenReturn(object);
    when(rowDefinition.getColumnsToSkip()).thenReturn(2);

    ExcelRowReader reader = new ExcelRowReader(rowDefinition, instatiator);
    assertThat(reader.getData(row), is(object));
  }
}
