package geeksaint.point.geeksaint.junit;

import org.apache.poi.ss.formula.functions.T;
import org.hamcrest.Description;
import org.hamcrest.StringDescription;

import java.util.List;

public class Support {

  public static void assertListEquals(List<T> expectedList, List<T> actulaList){
    StringBuilder description = new StringBuilder();
    if(expectedList.size() != actulaList.size()){
      description.append(String.format("Expected size : %nActual Size : %s", expectedList.size(), actulaList.size()));
    }
  }

  private void failedAssertion(String messageFormat, Object...params){
//    failedAssertion();

  }

}
