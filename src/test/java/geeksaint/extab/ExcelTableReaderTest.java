package geeksaint.extab;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExcelTableReaderTest {

  private Workbook workbook;
  private Sheet sheet;
  private Row rowOne;
  private Row rowTwo;
  private ExcelTableReader<Item> excelTableReader;
  private Item itemOne;
  private Item itemTwo;
  private ExcelRowReader<Item> rowReader;
  private RowDefinition rowDefinition;
  private Item itemThree;
  private Item itemFour;
  private Row rowThree;
  private Row rowFour;

  public static class Item{};

  @Before
  public void setUp() throws Exception {
    rowDefinition = mock(RowDefinition.class);
    workbook = mock(Workbook.class);
    sheet = mock(Sheet.class);

    rowOne = mock(Row.class);
    rowTwo = mock(Row.class);
    rowThree = mock(Row.class);
    rowFour = mock(Row.class);

    rowReader = mock(ExcelRowReader.class);

    itemOne = new Item();
    itemTwo = new Item();
    itemThree = new Item();
    itemFour = new Item();

    when(workbook.getSheetAt(0)).thenReturn(sheet);
    when(rowReader.getData(rowOne)).thenReturn(itemOne);
    when(rowReader.getData(rowTwo)).thenReturn(itemTwo);
    when(rowReader.getData(rowThree)).thenReturn(itemThree);
    when(rowReader.getData(rowFour)).thenReturn(itemFour);
  }

  @Test
  public void shouldReadADocument(){
    Iterator<Row> rowIterator = asList(rowOne, rowTwo).iterator();
    when(rowDefinition.getRowsToSkip()).thenReturn(0);
    when(sheet.rowIterator()).thenReturn(rowIterator);

    excelTableReader = new ExcelTableReader<Item>(workbook, rowReader, rowDefinition);

    assertThat(excelTableReader.readDocument(), is(asList(itemOne, itemTwo)));
  }

  @Test
  public void shouldSkipRowsSpecified(){
    Iterator<Row> rowIterator = asList(rowOne, rowTwo, rowThree, rowFour).iterator();

    when(rowDefinition.getRowsToSkip()).thenReturn(2);
    when(sheet.rowIterator()).thenReturn(rowIterator);

    excelTableReader = new ExcelTableReader<Item>(workbook, rowReader, rowDefinition);

    assertThat(excelTableReader.readDocument(), is(asList(itemThree, itemFour)));
  }

  @Test
  public void shouldReturnEmptyListIfRowsToSkipIsGreaterThanTotalRows(){
    Iterator<Row> rowIterator = asList(rowOne, rowTwo, rowThree, rowFour).iterator();

    when(rowDefinition.getRowsToSkip()).thenReturn(5);
    when(sheet.rowIterator()).thenReturn(rowIterator);

    excelTableReader = new ExcelTableReader<Item>(workbook, rowReader, rowDefinition);

    assertThat(excelTableReader.readDocument(), is(Collections.EMPTY_LIST ));
  }
}
