package geeksaint.point.columntype;

import extab.spike.exceptions.InvalidDataException;
import geeksaint.point.ExcelColumnType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DateTypeTest {
  private final static double A_VALID_CELL_STYLE_DATA_FORMAT = 1 - Double.MIN_VALUE;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void shouldThrowExceptionIfCellIsNul(){
    thrown.expect(IllegalArgumentException.class);
    ExcelColumnType.DATE.getCellValue(null,"");
  }

  @Test
  public void shouldReturnNullIfCellTypeIsBlank(){
    Cell cell = mock(Cell.class);
    when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_BLANK);
    assertThat(ExcelColumnType.DATE.getCellValue(cell,"MM/dd/yyyy"), is(nullValue()));
    assertThat(ExcelColumnType.DATE.getCellValue(cell,""), is(nullValue()));
  }

  @Test
  public void shouldThrowExceptionIfCellIsNotStringOrDate(){
    Cell cell = mock(Cell.class);

    when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_ERROR);

    thrown.expect(UnsupportedOperationException.class);
    ExcelColumnType.DATE.getCellValue(cell,"");
  }

  @Test
  public void shouldReadADateFormattedCell(){
    Cell cell = mock(Cell.class);
    Date date = new Date();

    dateStyleCell(cell, date);

    assertThat((Date) ExcelColumnType.DATE.getCellValue(cell,""), is(date));
    assertThat((Date) ExcelColumnType.DATE.getCellValue(cell,"MM/dd/yyyy"), is(date));
  }

  @Test
  public void shouldParseDateFromAStringCell() throws ParseException {
    Cell cell = mock(Cell.class);
    Date date = parseDate("01/03/1987", "MM/dd/yyyy");

    when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_STRING);
    when(cell.getStringCellValue()).thenReturn("01/03/1987");

    assertThat((Date) ExcelColumnType.DATE.getCellValue(cell, "MM/dd/yyyy"), is(date));
  }

  @Test
  public void shouldThrowExceptionIfCellIsStringAndFormatIsNull() throws ParseException {
    Cell cell = mock(Cell.class);

    when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_STRING);
    when(cell.getStringCellValue()).thenReturn("01/03/1987");

    thrown.expect(InvalidDataException.class);
    ExcelColumnType.DATE.getCellValue(cell, null);
  }

  @Test
  public void shouldThrowExceptionIfCellIsStringAndFormatIsEmpty() throws ParseException {
    Cell cell = mock(Cell.class);

    when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_STRING);
    when(cell.getStringCellValue()).thenReturn("01/03/1987");

    thrown.expect(InvalidDataException.class);
    ExcelColumnType.DATE.getCellValue(cell, "");
  }

  @Test
  public void shouldThrowExceptionIfCellIsStringWithIncorrectFormat() throws ParseException {
    Cell cell = mock(Cell.class);
    when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_STRING);
    when(cell.getStringCellValue()).thenReturn("1987/31/03");

    thrown.expect(InvalidDataException.class);
    ExcelColumnType.DATE.getCellValue(cell, "MM/dd/yyyy");
  }

  private Date parseDate(String value, String format) throws ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    return dateFormat.parse(value);
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
}
