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
  public void shouldCreateLineItemWithValues(){
    Map<Integer, List> rowValues = new HashMap<Integer, List>();
    List itemOne = new ArrayList();
    itemOne.add(0, "program#1");

    List itemTwo = new ArrayList();
    itemTwo.add(0, "program#2");

    rowValues.put(0, itemOne);
    rowValues.put(1, itemTwo);

    List<LineItem> createdItems = instatiator.createItems(rowValues);

    LineItem lineItemOne = createdItems.get(0);
    LineItem lineItemTwo = createdItems.get(1);

    assertThat(lineItemOne.getFieldOne(), is("program#1"));
    assertThat(lineItemTwo.getFieldOne(), is("program#2"));
  }

  @Test
  public void shouldInvokeSetters(){
    ItemWithSetter expected = new ItemWithSetter();
    expected.setSomething("something something");
    ItemInstatiator instatiator = new ItemInstatiator(ItemWithSetter.class);

    ItemWithSetter created = (ItemWithSetter) instatiator.createItem(asList("something something"));

    assertThat(created, is(expected));
  }

  @Test
  public void shouldSetRowNum(){
    List itemValues = new ArrayList();
    itemValues.add(0, "some-value");

    LineItem createdItem = (LineItem)instatiator.createItem(itemValues,101);

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
