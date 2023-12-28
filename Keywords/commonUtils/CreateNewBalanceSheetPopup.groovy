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

import internal.GlobalVariable

public class CreateNewBalanceSheetPopup {
	/**
	 * Verify default date
	 * @param balanceSheetDateObject - object referenced from object repository
	 * @param expectedDate - "PREV_MONTH_LASTDAY"/any expected date in "MM/dd/yyyy"
	 */
	@Keyword(keywordObject = "HomePage")
	public static void verifyDisplayedDate(TestObject balanceSheetDateObject, String expectedDate) {
		try {
			DateTimeFormatter pattern = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			String actualDate=WebUI.findWebElement(balanceSheetDateObject).getAttribute("value");
			if(expectedDate.equals("PREV_MONTH_LASTDAY")) {
				expectedDate=(LocalDate.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth())).format(pattern);
				}
				//To compare actual and expected date
				WebUI.verifyMatch(actualDate, expectedDate, false, FailureHandling.STOP_ON_FAILURE)
		}catch(Exception e) {
			WebUI.comment("Exception :: " + e.getMessage())
			KeywordUtil.markFailedAndStop("Exception while checking date displayed");
		}
	}
}
