package earningsUtils
import java.text.SimpleDateFormat
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

public class Dates {
	/**
	 * Verify sorting order of WebTable column with dates i.e. Ascending or Descending
	 * @param tableObject - test Object for WebTable referred from OR
	 * @param columnName - column Name in which Dates are present
	 * @param sortingOrder - sorting order i.e. Ascending or Descending
	 */
	@Keyword(keywordObject = "WebTable")
	public static void verifySortingOfDatesColumn(TestObject tableObject, String columnName, String sortingOrder) {
		try {
			WebElement tableElement = WebUI.findWebElement(tableObject, 2);
			// Find the index of the target column
			int columnIndex = -1;
			List<WebElement> headers = tableElement.findElements(By.tagName("th"));
			for (int i = 0; i < headers.size(); i++) {
				if (headers.get(i).getText().equalsIgnoreCase(columnName)) {
					columnIndex = i;
					break;
				}
			}
			if (columnIndex == -1) {
				KeywordUtil.markFailedAndStop("Searched column not found:: " + columnName);
			}
			List<Date> dates = new ArrayList<>();
			List<WebElement> rows = tableElement.findElements(By.tagName("tr"));
			boolean firstRow = true;
			for (WebElement row : rows) {
				if (firstRow) {
					firstRow = false;
					continue;
				}
				List<WebElement> cells = row.findElements(By.tagName("td"));
				if (cells.size() > columnIndex) {
					String cellText = cells.get(columnIndex).getText();
					Date date = new SimpleDateFormat("yyyy-MM-dd").parse(cellText);
					dates.add(date);
				}
			}
			// Verify sorting order
			verifySortingOrderOfDates(dates, sortingOrder);
		} catch (Exception e) {
			WebUI.comment("Exception :: " + e.getMessage())
			KeywordUtil.markFailedAndStop("Exception while verifying sorting order of WebTable column with dates");
		}
	}

	public static void verifySortingOrderOfDates(List<Date> dates, String sortingOrder) {
		boolean datesInCorrectOrder = true;
		try {
			for (int i = 0; i < dates.size()-1; i++) {
				if (sortingOrder.equalsIgnoreCase("ascending")) {
					if (!(dates.get(i).before(dates.get(i + 1)) || dates.get(i).equals(dates.get(i + 1)))) {
						datesInCorrectOrder = false;
						break;
					}
				} else if (sortingOrder.equalsIgnoreCase("descending")) {
					if (!(dates.get(i).after(dates.get(i + 1)) || dates.get(i).equals(dates.get(i + 1)))) {
						datesInCorrectOrder = false;
						break;
					}
				} else {
					KeywordUtil.markFailedAndStop("Invalid sorting order specified:: " + sortingOrder);
				}
			}

			if (datesInCorrectOrder) {
				KeywordUtil.logInfo("Dates are in " + sortingOrder + " order");
			} else {
				KeywordUtil.markFailedAndStop("Dates are not in " + sortingOrder + " order");
			}
		} catch (Exception e) {
			WebUI.comment("Exception :: " + e.getMessage())
			KeywordUtil.markFailedAndStop("Exception while verifying sorting order of WebTable column with dates");
		}
	}
}