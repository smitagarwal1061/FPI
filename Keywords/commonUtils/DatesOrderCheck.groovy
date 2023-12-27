package commonUtils
import java.text.SimpleDateFormat
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI


public class DatesOrderCheck {
	/**
	 * In WebTable column verify if dates are getting displayed in descending order
	 * @param tableObject - Test Object for WebTable referred by OR
	 * @param columnName - Column Name in which Dates are present
	 */
	@Keyword(keywordObject = "WebTable")
	public static List<String> verifyDatesinDescendingOrder(TestObject tableObject, String columnName) throws Exception {
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
			// Throw exception if column not found
			if (columnIndex == -1) {
				KeywordUtil.markFailed("Column not found: " + columnName);
			}
			// Print elements from the specified column
			List<String> columnElements = new ArrayList<>();
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
					columnElements.add(cellText);
				} else {
					KeywordUtil.logInfo("Column missing in this row");
				}
			}
			// Print the column elements
			WebUI.comment("Column elements:");
			for (String element : columnElements) {
				WebUI.comment(element);
			}
			// Convert strings to dates
			List<Date> dates = new ArrayList<>();
			for (String dateString : columnElements) {
				Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
				dates.add(date);
			}
			// Check for descending order, handling duplicates
			boolean datesInDescendingOrder = true;
			for (int i = 1; i < dates.size(); i++) {
				if (dates.get(i).after(dates.get(i - 1))) {
					datesInDescendingOrder = false;
					break; //Dates not in descending order
				} else if (!dates.get(i).before(dates.get(i - 1))) {
					// Duplicate dates, continue checking
				}
			}
			if (datesInDescendingOrder) {
				KeywordUtil.logInfo("Dates are in descending order");
			} else {
				KeywordUtil.markFailed("Dates are not in descending order");
			}
			return columnElements;
		} catch (NoSuchElementException e) {
			// Ignore "Element not found" exceptions if column name is present but cell is missing in a row
		} catch (Exception e) {
			KeywordUtil.logInfo("Error fetching column elements: " + e.getMessage());
			throw e;
		}
	}
}