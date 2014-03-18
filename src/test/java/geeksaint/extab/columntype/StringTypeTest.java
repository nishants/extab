package geeksaint.extab.columntype;

import geeksaint.extab.ExcelColumnType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StringTypeTest {

  private final static double A_VALID_CELL_STYLE_DATA_FORMAT = 1 - Double.MIN_VALUE;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void shouldThrowExceptionIfCellIsNul(){
    thrown.expect(IllegalArgumentException.class);
    ExcelColumnType.STRING.getCellValue(null,"");
  }

  @Test
  public void shouldReturnEmptyStringIfCellTypeIsBlank(){
    Cell cell = mock(Cell.class);
    when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_BLANK);
    assertThat((String) ExcelColumnType.STRING.getCellValue(cell,""), is(""));
  }

  @Test
  public void shouldReadStringValuesFromACell(){
    Cell cell = mock(Cell.class);

    when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_STRING);
    when(cell.getStringCellValue()).thenReturn("cell-value");

    assertThat((String) ExcelColumnType.STRING.getCellValue(cell,""), is("cell-value"));
  }

  @Test
  public void shouldReadDateValuesFromACell(){
    Cell cell = mock(Cell.class);
    Date date = new Date();

    dateStyleCell(cell, date);

    assertThat((String) ExcelColumnType.STRING.getCellValue(cell,""), is(date.toString()));
  }

  private void dateStyleCell(Cell cell, Date date) {
    when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_NUMERIC);
    when(cell.getNumericCellValue()).thenReturn(A_VALID_CELL_STYLE_DATA_FORMAT);
    when(cell.getDateCellValue()).thenReturn(date);

    CellStyle cellStyle = mock(CellStyle.class);
    when(cell.getCellStyle()).thenReturn(cellStyle);
    when(cellStyle.getDataFormat()).thenReturn(CellStyle.ALIGN_GENERAL);
    when(cellStyle.getDataFormatString()).thenReturn("m/d/y");
  }

  @Test
  public void shouldReadValuesFromNumericCell(){
    Cell cell = mock(Cell.class);

    when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_NUMERIC);
    when(cell.getNumericCellValue()).thenReturn(9989.9989);

    assertThat((String) ExcelColumnType.STRING.getCellValue(cell,""), is("9989.9989"));
  }

  @Test
  public void shouldTrimZeroAfterDecimalForNumericCell(){
    Cell cell = mock(Cell.class);

    when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_NUMERIC);
    when(cell.getNumericCellValue()).thenReturn(1.0);

    assertThat((String) ExcelColumnType.STRING.getCellValue(cell, ""), is("1"));
  }

  @Test
  public void shouldThrowExceptionIfCellIsNotNumericStringOrDate(){
    Cell cell = mock(Cell.class);

    when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_ERROR);

    thrown.expect(UnsupportedOperationException.class);
    ExcelColumnType.STRING.getCellValue(cell,"");
  }
}
