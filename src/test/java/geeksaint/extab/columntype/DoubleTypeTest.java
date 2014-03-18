package geeksaint.extab.columntype;

import geeksaint.extab.exceptions.InvalidDataException;
import geeksaint.extab.ExcelColumnType;
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

public class DoubleTypeTest {
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void shouldThrowExceptionIfCellIsNul(){
    thrown.expect(IllegalArgumentException.class);
    ExcelColumnType.DOUBLE.getCellValue(null,"");
  }

  @Test
  public void shouldThrowExceptionIfCellIsNotStringOrNumber(){
    Cell cell = mock(Cell.class);

    when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_ERROR);

    thrown.expect(UnsupportedOperationException.class);
    ExcelColumnType.DOUBLE.getCellValue(cell, "");
  }

  @Test
  public void shouldReturnNullIfCellTypeIsBlank(){
    Cell cell = mock(Cell.class);
    when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_BLANK);
    assertThat(ExcelColumnType.DOUBLE.getCellValue(cell, ""), is(nullValue()));
  }

  @Test
  public void shouldParseNumberFromAStringCell() throws ParseException {
    Cell cell = mock(Cell.class);

    when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_STRING);
    when(cell.getStringCellValue()).thenReturn("983625.4354");

    assertThat((Double) ExcelColumnType.DOUBLE.getCellValue(cell, ""), is(new Double(983625.4354)));
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
  public void shouldReadDoubleValuesFromNumericCell(){
    Cell cell = mock(Cell.class);

    when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_NUMERIC);
    when(cell.getNumericCellValue()).thenReturn(983625.3434);

    assertThat((Double) ExcelColumnType.DOUBLE.getCellValue(cell, ""), is(new Double(983625.3434)));
  }

  @Test
  public void shouldKeepPrecision(){
    Cell cell = mock(Cell.class);

    when(cell.getCellType()).thenReturn(Cell.CELL_TYPE_NUMERIC);
    when(cell.getNumericCellValue()).thenReturn(983625.000001);

    assertThat((Double) ExcelColumnType.DOUBLE.getCellValue(cell, ""), is(new Double(983625.000001)));
  }
}
