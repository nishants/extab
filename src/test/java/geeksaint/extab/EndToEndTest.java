package geeksaint.extab;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EndToEndTest {
  private final String allStringsPersonFileXLS = "/data/Person.xls";
  private final String allStringsPersonSkipFileXLS = "/data/PersonSkips.xls";
  private final String allStringsPersonFileXLSX = "/data/Person.xlsx";
  private final String allStringsManyPersonFileXLSX = "/data/ManyPerson.xlsx";
  private final String personsFileWithDateAndNumberFormats = "/data/PersonWithDateAndNumberFormat.xls";
  private Person personOne;
  private Person personTwo;
  private Person personThree;

  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode
  @ToString
  public static class Person{
    @ExcelColumn(order=1) private String name;
    @ExcelColumn(order=2) private String age;
    @ExcelColumn(order=3) private void setBirthDate(String arg) throws ParseException {birthDate = parseDate(arg);}

    private Date birthDate;
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode
  @ToString
  @ExcelTable(name="PersonSkip",skipRows = 2, skipColumns = 3)
  public static class PersonSkip{
    @ExcelColumn(order=1) private String name;
    @ExcelColumn(order=2) private String age;
    @ExcelColumn(order=3) private void setBirthDate(String arg) throws ParseException {birthDate = parseDate(arg);}

    private Date birthDate;
  }

  private static Date parseDate(String arg) throws ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
    return dateFormat.parse(arg);
  }

  @Test
  public void shouldParseAnXLSFileIntoModel() throws IOException, ParseException {
    Person personOne = new Person("one-one", "33", parseDate("01/31/2014"));
    Person personTwo = new Person("two-one", "44", parseDate("01/31/2014"));
    Person personThree = new Person("three-one", "55", parseDate("01/31/2014"));
    InputStream inputStream = getClass().getResourceAsStream(allStringsPersonFileXLS);
    List<Person> items = asList(personOne, personTwo, personThree);

    List<Person> parsed = ExTab.readXls(inputStream, Person.class);

    assertThat(parsed, is(items));
  }

  @Test
  public void shouldParseAnXLSXFileIntoModel() throws IOException, ParseException {
    personOne = new Person("one-one", "33", parseDate("01/31/2014"));
    personTwo = new Person("two-one", "44", parseDate("01/31/2014"));
    personThree = new Person("three-one", "55", parseDate("01/31/2014"));
    List<Person> items = asList(personOne, personTwo, personThree);

    InputStream inputStream = getClass().getResourceAsStream(allStringsPersonFileXLSX);

    List<Person> parsed = ExTab.readXlsx(inputStream, Person.class);

    assertThat(parsed, is(items));
  }

  @Test
  public void shouldSkipRowsAndColumns() throws IOException, ParseException {
    PersonSkip personOne = new PersonSkip("one-one", "33", parseDate("01/31/2014"));
    PersonSkip personTwo = new PersonSkip("two-one", "44", parseDate("01/31/2014"));
    PersonSkip personThree = new PersonSkip("three-one", "55", parseDate("01/31/2014"));
    List<PersonSkip> items = asList(personOne, personTwo, personThree);

    InputStream inputStream = getClass().getResourceAsStream(allStringsPersonSkipFileXLS);

    List<PersonSkip> parsed = ExTab.readXls(inputStream, PersonSkip.class);

    assertThat(parsed, is(items));
  }

  @Test
  public void shouldParseASimpleFileWith15000InLessThan5Seconds() throws IOException, ParseException {
    InputStream inputStream = getClass().getResourceAsStream(allStringsManyPersonFileXLSX);

    long beginTime = getTimeInMillis();
    List<Person> parsed = ExTab.readXlsx(inputStream, Person.class);
    long timeTaken = getTimeInMillis() - beginTime;

    assert timeTaken < 5000l : format("Time taken to parse was %d ms, more than 5 seconds ", timeTaken);
    System.out.printf("Time taken to parse was %dms, for %d records", (timeTaken), parsed.size());
  }

  private long getTimeInMillis() {
    return Calendar.getInstance().getTimeInMillis();
  }

  @Test
  public void shouldParseAFormattedColumn() throws IOException, ParseException {
    Person expected = new Person("Mr Singh", "27", parseDate("02/28/2014"));

    InputStream inputStream = getClass().getResourceAsStream(personsFileWithDateAndNumberFormats);
    List<Person> parsed = ExTab.readXls(inputStream, Person.class);

    assertThat(parsed.get(0), is(expected));
  }
}
