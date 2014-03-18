package geeksaint.point;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

public enum ExcelFileType {
  XLS {
    @Override
    Workbook getWorkbook(InputStream inputStream) throws IOException {
      POIFSFileSystem fs = new POIFSFileSystem(inputStream);
      return new HSSFWorkbook(fs);
    }
  },

  XLSX {
    @Override
    Workbook getWorkbook(InputStream inputStream) throws IOException {
      Workbook workbook = new XSSFWorkbook(inputStream);
      return workbook;
    }
  };
  public static ExcelFileType getFileType(String fileName){
    if(fileName == null || fileName.length()==0) throw new IllegalArgumentException("File name cant be null or empty.");

    int extensionBeginIndex = fileName.lastIndexOf(".")+1;
    String extension = fileName.substring(extensionBeginIndex);
    return extension.toLowerCase().equals("xls") ? XLS : XLSX;
  }

  abstract Workbook getWorkbook(InputStream inputStream1) throws IOException;
}