package extab.spike;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import static geeksaint.point.ExcelColumnType.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Employee {
  @ExcelColumn(order = 1, type = STRING)
  private String name;

  @ExcelColumn(order = 2, type = LONG)
  private Long employeeId;

  private String address;

  @ExcelColumn(order = 5, type = DOUBLE)
  private Double salary;

  @ExcelColumn(order = 4, type = DATE, format = "MM/dd/yyyy")
  private Date joiningDate;

  @ExcelColumn(order = 3, type = STRING)
  private void setAddress(String addr) {
    address = addr;
  }

  @RowNum
  private int itemRowNumber;
}
