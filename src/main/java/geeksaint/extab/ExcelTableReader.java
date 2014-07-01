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

  protected List<T> readDocument() {
    List<T> values = new ArrayList<T>();
    Sheet sheet = workBook.getSheetAt(0);
    Iterator<Row> rowIterator = sheet.rowIterator();
    skipRows(rowsToSkip, rowIterator);
    while (rowIterator.hasNext()) {
      values.add(rowReader.getData(rowIterator.next()));
    }
    return values;
  }

  private void skipRows(int rowsToSkip, Iterator<Row> rowIterator) {
    for (int i = 0; (i < rowsToSkip) && rowIterator.hasNext(); i++) {
      rowIterator.next();
    }
  }

  public static <T> List<T> read(String fileName, Class<T> rowType) throws IOException {
    Workbook workBook = fileTypeOf(fileName).getWorkbook(new FileInputStream(fileName));
    RowDefinition rowDefinition = new RowDefinition(rowType, new AnnotationProcessor());
    ExcelRowReader<T> rowReader = new ExcelRowReader<T>(rowDefinition, new ItemInstatiator(rowType));

    return (new ExcelTableReader<T>(workBook, rowReader, rowDefinition)).readDocument();
  }

  public static <T> List<T> readXls(InputStream inputStream, Class<T> rowType) throws IOException {
    Workbook workBook = XLS.getWorkbook(inputStream);
    RowDefinition rowDefinition = definitionFor(rowType);
    ExcelRowReader<T> rowReader = new ExcelRowReader<T>(rowDefinition, new ItemInstatiator(rowType));

    return (new ExcelTableReader<T>(workBook, rowReader, rowDefinition)).readDocument();
  }

  public static <T> List<T> readXlsx(InputStream inputStream, Class<T> rowType) throws IOException {
    Workbook workBook = XLSX.getWorkbook(inputStream);
    RowDefinition rowDefinition = new RowDefinition(rowType, new AnnotationProcessor());
    ExcelRowReader<T> rowReader = new ExcelRowReader<T>(rowDefinition, instatiatorOf(rowType));

    return (new ExcelTableReader<T>(workBook, rowReader, rowDefinition)).readDocument();
  }

  private static <T> ItemInstatiator instatiatorOf(Class<T> rowType) {
    return new ItemInstatiator(rowType);
  }

  private static <T> RowDefinition definitionFor(Class<T> rowType) {
    return new RowDefinition(rowType, new AnnotationProcessor());
  }
}
