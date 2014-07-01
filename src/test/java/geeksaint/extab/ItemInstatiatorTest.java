package geeksaint.extab;

import geeksaint.extab.exceptions.InvalidAnnotationTargetException;
import geeksaint.extab.exceptions.InvalidTargetTypeException;
import lombok.EqualsAndHashCode;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import lombok.Getter;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ItemInstatiatorTest {

  private final Class targetClass = LineItem.class;
  private ItemInstatiator instatiator;
  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @ExcelTable(name="Item with setter")
  @EqualsAndHashCode
  public static class ItemWithSetter{
    private String something;

    @ExcelColumn(order=1)
    private void setSomething(String arg){something = arg;}

    @RowNum
    private int rowNum;

    public String getSomething(){
      return something;
    }

  }

  @ExcelTable(name="LineItem")
  @EqualsAndHashCode
  public static class LineItem{
    @Getter
    @ExcelColumn(order=1) private String fieldOne;
    private String badField;

    @Getter
    @RowNum
    private int rowNumberInSheet;
  }

  public class BadLineItem{
    public BadLineItem(int i){}
  }

  @Before
  public void setUp() throws Exception {
    instatiator = new ItemInstatiator(targetClass);
  }

  @Test
  public void shouldSetRowNumAndFieldValues(){
    List itemValues = new ArrayList();
    itemValues.add(0, "some-value");

    LineItem createdItem = (LineItem)instatiator.createItem(itemValues, 101);

    assertThat(createdItem.getFieldOne(), is("some-value"));
    assertThat(createdItem.getRowNumberInSheet(), is(101));
  }

  @Test
  public void shouldRaiseExcptionIfTheTargetTypeHasNoDefaultConstructor(){
    List itemValues = new ArrayList();
    itemValues.add(0, "some-value");

    expectedException.expect(InvalidTargetTypeException.class);
    expectedException.expectMessage("Please ensure the target class has public default constructor and is not abstract type");

    new ItemInstatiator(BadLineItem.class).createItem(itemValues, 101);

  }
}
