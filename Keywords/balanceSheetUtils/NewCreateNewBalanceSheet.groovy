package balanceSheetUtils

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI


import java.text.SimpleDateFormat

public class NewCreateNewBalanceSheet {
	/**
	 * Verify default date
	 * @param balanceSheetDateObject - object referenced from object repository
	 * @param expectedDate - "PREV_MONTH_LASTDAY"/any expected date in "MM/dd/yyyy"
	 */
	@Keyword(keywordObject = "HomePage")
	public static void verifyDefaultDate(TestObject balanceSheetDateObject, String expectedDate) {
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
	
	/**
	 * Get current timestamp
	 */
	@Keyword
	public static String getCurrentTimestamp() {
		String timestamp;
		try {
			timestamp=new SimpleDateFormat("MMddyyyy_HHmmss").format(new Date());
		}catch(Exception e) {
			WebUI.comment("Exception :: " + e.getMessage())
			KeywordUtil.markFailedAndStop("Exception while getting current timestamp");
		}
		return timestamp;
	}
}
