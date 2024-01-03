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
	 * In WebTable column verify if dates are getting displayed in ASC or DESC order
	 * @param tableObject - Test Object for WebTable referred by OR
	 * @param columnName - Column Name in which Dates are present
	 */
	@Keyword(keywordObject = "Dates")
	public static void verifySortingOfDatesColumn(TestObject tableObject, String columnName, String sortingOrder) throws Exception {
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
				KeywordUtil.markFailed("Exception in verifySortingOfDatesColumn. Column not found:: " + columnName);
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
		} catch (NoSuchElementException e) {
			// Ignore "Element not found" exception and give Column name not present exception when user enters incorrect column name
		} catch (Exception e) {
			KeywordUtil.logInfo("Exception in verifySortingOfDatesColumn. Error fetching column elements:: " + e.getMessage());
			throw e;
		}
	}

	public static void verifySortingOrderOfDates(List<Date> dates, String sortingOrder) {
		boolean datesInCorrectOrder = true;
		for (int i = 1; i < dates.size(); i++) {
			if (sortingOrder.equalsIgnoreCase("ascending")) {
				if (dates.get(i).before(dates.get(i - 1))) {
					datesInCorrectOrder = false;
					break;
				}
			} else if (sortingOrder.equalsIgnoreCase("descending")) {
				if (dates.get(i).after(dates.get(i - 1))) {
					datesInCorrectOrder = false;
					break;
				}
			} else {
				KeywordUtil.markFailed("Exception in verifySortingOfDatesColumn. Invalid sorting order specified:: " + sortingOrder);
				return;
			}
		}

		if (datesInCorrectOrder) {
			KeywordUtil.logInfo("Dates are in " + sortingOrder + " order");
		} else {
			KeywordUtil.markFailedAndStop("Exception in verifySortingOrderOfDates. Dates are not in " + sortingOrder + " order");
		}
	}
}