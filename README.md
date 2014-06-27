extab
=====
This is an API to map tables in excel files to java objects. It uses Apache POI to parse the excel files and provide a JPA style annotation 
based mapping of objects to table rows.


#Reading a file

## 1. Using annotation
   ``` java
  public static class Person{
    @ExcelColumn(order=1) private String name;
    @ExcelColumn(order=2) private String age;
    @ExcelColumn(order=3) private void setBirthDate(String arg) throws ParseException {birthDate = parseDate(arg);}

    private Date birthDate;
  }