package geeksaint.point.columntype;

import extab.spike.exceptions.InvalidDataException;
import geeksaint.point.ExcelColumnType;
import org.apache.poi.ss.usermodel.Cell;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.text.ParseException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LongTypeTest {
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void shouldThrowExceptionIfCellIsNul(){
    thrown.expect(IllegalArgumentException.class);
    ExcelColumnType.LONG.getCellValue(null,"");
  }

  @Test
  public void shouldThrowExceptionIfCellIsNotStringOrNumber(){
    Cell cell = mock(Cell.class);

    when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_ERROR);

    thrown.expect(UnsupportedOperationException.class);
    ExcelColumnType.LONG.getCellValue(cell, "");
  }

  @Test
  public void shouldReturnNullIfCellTypeIsBlank(){
    Cell cell = mock(Cell.class);
    when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_BLANK);
    assertThat(ExcelColumnType.LONG.getCellValue(cell, ""), is(nullValue()));
  }

  @Test
  public void shouldParseNumberFromAStringCell() throws ParseException {
    Cell cell = mock(Cell.class);

    when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_STRING);
    when(cell.getStringCellValue()).thenReturn("983625");

    assertThat((Long) ExcelColumnType.LONG.getCellValue(cell, ""), is(new Long(983625)));
  }

  @Test
  public void shouldThrowExceptionIfCellIsStringButNotParseableNumber(){
    Cell cell = mock(Cell.class);

    when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_STRING);
    when(cell.getStringCellValue()).thenReturn("xx");

    thrown.expect(InvalidDataException.class);
    ExcelColumnType.LONG.getCellValue(cell,"");
  }

  @Test
  public void shouldReadLongValuesFromNumericCell(){
    Cell cell = mock(Cell.class);

    when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_NUMERIC);
    when(cell.getNumericCellValue()).thenReturn(983625.0);

    assertThat((Long) ExcelColumnType.LONG.getCellValue(cell, ""), is(new Long(983625)));
  }

  @Test
  public void shouldTruncateDecimalValuesIfNumericCellHasDecimalValue(){
    Cell cell = mock(Cell.class);

    when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_NUMERIC);
    when(cell.getNumericCellValue()).thenReturn(983625.2974);

    assertThat((Long) ExcelColumnType.LONG.getCellValue(cell, ""), is(new Long(983625)));
  }
}
