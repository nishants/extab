package geeksaint.extab;

import geeksaint.extab.exceptions.InvalidDataException;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.String.format;

public enum ExcelColumnType {
  STRING {
    @Override
    public Object getCellValue(Cell cell, String format) {
      //Ignores format
      return parseAsStringCell(cell);
    }
  },

  DATE {
    @Override
    public Object getCellValue(Cell cell, String format) {
      return parseAsDateCell(cell, format);
    }
  },

  LONG {
    @Override
    public Object getCellValue(Cell cell, String format) {
      //Ignores format
      return getLongValue(cell);
    }
  },

  DOUBLE {
    @Override
    public Object getCellValue(Cell cell, String format) {
      //Ignores format
      return getDoubleValue(cell);
    }
  };

  private static Object getDoubleValue(Cell cell) {
    checkNull(cell);
    if (isBlank(cell)) return null;
    if (isNumeric(cell)) return readAsDouble(cell);
    if (isString(cell)) {
      try {
        return Double.parseDouble(cell.getStringCellValue());
      } catch (NumberFormatException e) {
        throw new InvalidDataException(format("Cell in row %d, column %d '%s', is not a valid number.",
            cell.getRowIndex(), cell.getColumnIndex(), cell.getStringCellValue()), e);
      }
    }
    return unknownCellTypeException(cell, "DOUBLE");
  }

  private static Object getLongValue(Cell cell) {
    checkNull(cell);
    if (isBlank(cell)) return null;
    if (isNumeric(cell)) return readAsLong(cell);
    if (isString(cell)) {
      try {
        return Long.parseLong(cell.getStringCellValue());
      } catch (NumberFormatException e) {
        throw new InvalidDataException(format("Cell in row %d, column %d '%s', is not a whole number.",
            cell.getRowIndex(), cell.getColumnIndex(), cell.getStringCellValue()), e);
      }
    }
    return unknownCellTypeException(cell, "LONG");
  }

  private static Double readAsDouble(Cell cell) {
    return new Double(cell.getNumericCellValue());
  }

  private static long readAsLong(Cell cell) {
    return (readAsDouble(cell).longValue());
  }

  public abstract Object getCellValue(Cell cell, String format);

  private static Object parseAsStringCell(Cell cell) {
    checkNull(cell);

    if (isBlank(cell)) return "";
    if (isString(cell)) return cell.getStringCellValue();
    if (isDate(cell)) return cell.getDateCellValue().toString();
    if (isNumeric(cell)) return getNumberAsString(cell);

    return unknownCellTypeException(cell, "STRING");
  }

  private static Object parseAsDateCell(Cell cell, String format) {
    checkNull(cell);

    if (isBlank(cell)) return null;
    if (isDate(cell)) return cell.getDateCellValue();
    if (isString(cell)) {
      checkFormat(cell, format);
      try {
        return parseDate(cell.getStringCellValue(), format);
      } catch (ParseException e) {
        throw new InvalidDataException(format("Cell in row %d, column %d '%s', is not formatted as '%s'",
            cell.getRowIndex(), cell.getColumnIndex(), cell.getStringCellValue(), format), e);
      }
    }
    return unknownCellTypeException(cell, "DATE");
  }

  private static void checkFormat(Cell cell, String format) {
    if (format == null || format.length() == 0) {
      throw new InvalidDataException(format("No format specified for cell in row %d, column %d with value '%s'",
          cell.getRowIndex(), cell.getColumnIndex(), cell.getStringCellValue()));
    }
  }

  private static Date parseDate(String value, String format) throws ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    dateFormat.setLenient(false);
    return dateFormat.parse(value);
  }

  private static void checkNull(Cell cell) {
    if (cell == null) throw new IllegalArgumentException(format("Cell was null"));
  }

  private static boolean isNumeric(Cell cell) {
    return cell.getCellType() == Cell.CELL_TYPE_NUMERIC;
  }

  private static boolean isString(Cell cell) {
    return cell.getCellType() == Cell.CELL_TYPE_STRING;
  }

  private static boolean isBlank(Cell cell) {
    return cell.getCellType() == Cell.CELL_TYPE_BLANK;
  }

  private static boolean isDate(Cell cell) {
    return isNumeric(cell) && isDateFormatted(cell);
  }

  private static boolean isDateFormatted(Cell cell) {
    return HSSFDateUtil.isCellDateFormatted(cell);
  }

  private static String getNumberAsString(Cell cell) {
    String value = Double.valueOf(cell.getNumericCellValue()).toString();
    return value.replaceFirst("\\.0*$", "");
  }

  private static Object unknownCellTypeException(Cell cell, String expectedType) {
    throw new UnsupportedOperationException(
        format("Cannot read '%s' value form cell in row %d, column %d of type '%d'",
            expectedType, cell.getRowIndex(),
            cell.getColumnIndex(), cell.getCellType()));
  }
}
