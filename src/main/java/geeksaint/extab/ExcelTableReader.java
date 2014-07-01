package geeksaint.extab;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static geeksaint.extab.ExcelFileType.*;

public class ExcelTableReader<T> {
  private final int rowsToSkip;
  private Workbook workBook;
  private ExcelRowReader<T> rowReader;

  protected ExcelTableReader(Workbook workBook,
                             ExcelRowReader<T> rowReader,
                             RowDefinition rowDefinition) {
    this.workBook = workBook;
    this.rowsToSkip = rowDefinition.getRowsToSkip();
    this.rowReader = rowReader;
  }

  public List<T> read() {
    List<T> values = new ArrayList<T>();
    Sheet sheet = workBook.getSheetAt(0);
    Iterator<Row> rowIterator = sheet.rowIterator();
    skipRows(rowsToSkip, rowIterator);
    while (rowIterator.hasNext()) {
      values.add(rowReader.getData(rowIterator.next()));
    }
    return values;
  }

  private void skipRows(int rowsToSkip,
                        Iterator<Row> rowIterator) {
    for (int i = 0;
         (i < rowsToSkip) && rowIterator.hasNext();
         i++) {
      rowIterator.next();
    }
  }
}
