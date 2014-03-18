package geeksaint.extab;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ColumnDefinition implements Comparable<ColumnDefinition>{
  private Integer order;
  private ExcelColumnType columnType;
  private String format;

  @Override
  public int compareTo(ColumnDefinition that) {
    return this.getOrder().compareTo(that.getOrder());
  }
}
