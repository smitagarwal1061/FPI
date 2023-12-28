package readOnlyUtils

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import org.openqa.selenium.By
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords;
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable

public class readOnly {
	/**
	 * to get the read only cells
	 * @param tableTitle - Title of the table to locate
	 * @param disabled   - The class that indicates a readOnly state
	 *
	 */
	@Keyword(keywordObject = 'readOnly')
	public static void verifyTableCellIsEnabled(String tableTitle, String disabled) {
		try {
			WebElement table = WebUiBuiltInKeywords.findWebElement(new TestObject().addProperty("title", ConditionType.EQUALS, tableTitle), 30);
			List<WebElement> allCells = new ArrayList<>();
			for (WebElement row : table.findElements(By.tagName("tr"))) {
				if (row.getAttribute("class").contains(disabled)) {
					allCells.addAll(row.findElements(By.tagName("td")));
				}
			}
			for (WebElement cell : allCells) {
				String rowClass = cell.findElement(By.xpath("parent::tr")).getAttribute("class");
				if (rowClass != null && rowClass.contains(disabled)) {
					KeywordUtil.logInfo("Cell is in readonly mode: " + cell.getText());
				}else {
					KeywordUtil.logInfo("Cell is enable: " + cell.getText());
				}
			}
		} catch (NoSuchElementException e) {
			WebUI.comment("Caught an Exception :: " + e.getMessage());
			KeywordUtil.markFailedAndStop("Exception while verifying the cells in the table");
		}
	}
}


