package geeksaint.extab;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Demo {
  private final String demoExcelFile = "/data/Employee.xlsx";

  @Test
  public void shouldParseAnXLSFileIntoModel() throws IOException, ParseException {
    Employee employeeOne    = new Employee("Joe hoe",	  420l,	 "Lapatabad",	  83.435,	 parseDate("02/28/2013"), 1);
    Employee employeeTwo    = new Employee("Woe hoe",	  840l,	 "Kyapatabad",	75.432,	 parseDate("12/01/2009"), 2);
    Employee employeeThree  = new Employee("Jessa hoe",	1680l, "Napatabad",	  114.232, parseDate("01/31/2013"), 3);
    Employee employeeFour   = new Employee("Vessa hoe",	3360l, "KaPatabad",	  43.000,  parseDate("01/11/2012"), 4);

    InputStream inputStream = getClass().getResourceAsStream(demoExcelFile);
    List<Employee> items = asList(employeeOne, employeeTwo, employeeThree, employeeFour);

    List<Employee> parsed = ExcelTableReader.readXlsx(inputStream, Employee.class);

    assertThat(parsed, is(items));
  }

  private static Date parseDate(String arg) throws ParseException {
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    dateFormat.setLenient(true);
    return dateFormat.parse(arg);
  }

}
