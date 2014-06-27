ExTab
=====
This is an API to map tables in excel files to java objects. It uses Apache POI to parse the excel files and provide a JPA styled annotation 
based mapping of objects to table rows.


###Reading a file

####1. Create a model class

  ``` java
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
	```

#### 2. Create an excel file with following data
	|Joe Hoe	  |420	|Lapatabad	|28-02-13	|83.435|
	|Woe Hoe	  |840	|Kyapatabad	|01-12-09	|75.432|
	|Jessa Hoe	|1680	|Napatabad	|31-01-13	|114.232|
	|Vessa Hoe	|3360	|Kyapatabad	|11-01-12	|43|

#### 3. Read file
	``` java
	 List<Employee> parsed = ExcelTableReader.read("/Users/batman/data/Employee.xlsx", Employee.class);
	 ```

##### @ExcelColumn
  It maps a setter method or field with particular columns in the excel tables.
  Examples :

  1. **@ExcelColumn(order = 1, type = STRING)**
  ..* Maps the field to the first column(one based index) in the excel table.
  ..* All values in the column are expected to be of type text.

  2. **@ExcelColumn(order = 4, type = DATE, format = "MM/dd/yyyy")**
  ..* Maps the field to the fourth column(one based index) in the excel table.
  ..* All values in the column are expected to be of type date, formatted as "MM/dd/yyyy".
  ..* For valid patterns of date formats refer java.text.SimpeDateFormat.


