package geeksaint.extab;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.util.ArrayList;
import java.util.List;

public class ExcelRowReader<T> {
  private final Class<T> claz;
  private final ColumnDefinition[] rowDefinition;
  private final ItemInstatiator instatiator;
  private final int columnsToSkip;

  protected ExcelRowReader(RowDefinition rowDefinition, ItemInstatiator instatiator){
    this.instatiator = instatiator;
    this.rowDefinition = rowDefinition.getRowDefinition();
    this.claz = rowDefinition.getRowType();
    columnsToSkip = rowDefinition.getColumnsToSkip();
  }

  public T getData(Row row){
    return (T)instatiator.createItem(getRowData(row), row.getRowNum() + 1);
  }

  private List getRowData(Row row) {
    List values = new ArrayList<String>();
      for(int i =0; i<rowDefinition.length; i++){
      String format = rowDefinition[i].getFormat();
      ExcelColumnType columnType = rowDefinition[i].getColumnType();
      Cell cell = row.getCell(i + columnsToSkip);
      Object cellValue = columnType.getCellValue(cell, format);
      values.add(cellValue);
    }
    return values;
  }
}
