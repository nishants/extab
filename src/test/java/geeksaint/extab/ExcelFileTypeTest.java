package geeksaint.extab;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ExcelFileTypeTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void shouldReturnCorrectFileType(){
    assertThat(ExcelFileType.fileTypeOf("/some/place/some-file.xls"), is(ExcelFileType.XLS));
    assertThat(ExcelFileType.fileTypeOf("/some/place/some-file.xlsx"), is(ExcelFileType.XLSX));
  }

  @Test
  public void shouldReturnHSSFWorkbookIfFileTypeIsXLS() throws IOException {
    ExcelFileType xlsFileType = ExcelFileType.XLS;
    InputStream inputStream = getClass().getResourceAsStream("/data/EmptyExcel.xls");

    assertThat(xlsFileType.getWorkbook(inputStream), instanceOf(HSSFWorkbook.class));
  }

  @Test
  public void shouldReturnXSSFWorkbookIfFileTypeIsXLSX() throws IOException {
    ExcelFileType xlsFileType = ExcelFileType.XLSX;
    InputStream inputStream = getClass().getResourceAsStream("/data/EmptyExcel.xlsx");

    assertThat(xlsFileType.getWorkbook(inputStream), instanceOf(XSSFWorkbook.class));
  }

  @Test
  public void shouldReturnXLSXFileTypeIfFilenameHasNoExtension(){
    assertThat(ExcelFileType.fileTypeOf("/some/place/some-file-with-no-extension"), is(ExcelFileType.XLSX));
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionIfFileNameIsEmpty(){
    thrown.expect(IllegalArgumentException.class);
    ExcelFileType.fileTypeOf("");
  }

  @Test
  public void shouldThrowIllegalArgumentExceptionIfFileNameIsNull(){
    thrown.expect(IllegalArgumentException.class);
    ExcelFileType.fileTypeOf(null);
  }
}
