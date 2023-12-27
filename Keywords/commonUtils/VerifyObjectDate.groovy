package commonUtils

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.keyword.builtin.VerifyEqualKeyword
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import com.kms.katalon.core.util.KeywordUtil
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords
import org.openqa.selenium.WebElement
import org.openqa.selenium.WebDriver
import org.openqa.selenium.By
import com.kms.katalon.core.webui.driver.DriverFactory
import internal.GlobalVariable

public class VerifyObjectDate {
	/**
	 * Verify date
	 * @param objDateField- object for date field
	 * @param expectedDate- PREV_MONTH_LASTDAY or date to enter in date field PREV_MONTH_LASTDAY
	 */
	@Keyword
	public static void verifyDisplayedDate(TestObject objDateField, String expectedDate) {
		try {
			DateTimeFormatter pattern = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			String dateField=WebUI.findWebElement(objDateField).getAttribute("value");
			KeywordUtil.logInfo("The date in date field is :: "+dateField)
			if(expectedDate.equals("PREV_MONTH_LASTDAY")) {
				String lastDayOfPreviousMonth=(LocalDate.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth())).format(pattern);
				KeywordUtil.logInfo("The last date of previous month is :: "+lastDayOfPreviousMonth)
				//To compare actual and expected date
				WebUI.verifyMatch(dateField, lastDayOfPreviousMonth, false)
			}
			else {
				KeywordUtil.logInfo("The expected date is :: "+expectedDate)
				WebUI.verifyMatch(dateField, expectedDate, false)
			}
		}catch(Exception e) {
			WebUI.comment("Exception :: " + e.getMessage())
			KeywordUtil.markFailedAndStop("Exception while comparing dates");
		}
	}
}
