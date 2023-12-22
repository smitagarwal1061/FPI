package commonUtils

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
	 * @param objDateField- locator for date field
	 * @param expectedDate- date to enter in date field
	 */
	@Keyword
	public static void verifyDate(TestObject objDateField, String expectedDate) {
		try {
			DateTimeFormatter pattern = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			WebElement dateField = WebUI.findWebElement(objDateField);
			String dateFieldString = dateField.getAttribute("value");
			KeywordUtil.logInfo("The date in date field is :: "+dateFieldString)
			if(expectedDate.equals("PREV_MONTH_LASTDAY")) {

				LocalDate lastDayOfPreviousMonth= LocalDate.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
				String lastDayOfPreviousMonthString=lastDayOfPreviousMonth.format(pattern);
				KeywordUtil.logInfo("The last date of previous month is :: "+lastDayOfPreviousMonthString)
				if(lastDayOfPreviousMonthString.equals(dateFieldString)) {
					KeywordUtil.markPassed("The date in date field is the last day of previous month.");
				}
				else {
					KeywordUtil.markFailed("The date in date field is not the last day of previous month.");
				}
			}
			else {
				if(expectedDate.equals(dateFieldString)) {
					KeywordUtil.logInfo("The expected date is :: "+expectedDate)
					KeywordUtil.markPassed("The date in date field matches with expected date");
				}
				else {
					KeywordUtil.markPassed("The date in date field does not match with expected date");
				}
			}
		}catch(Exception e) {
			WebUI.comment("Exception :: " + e.getMessage())
			KeywordUtil.markFailedAndStop("Exception while comparing dates");
		}
	}
}
