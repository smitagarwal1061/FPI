package homePageUtils

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

import internal.GlobalVariable

public class PrimaryNavigation {
	/**
	 * Select option from dropdown
	 * @param navigationPathInDropdown - for navigation through dropdown
	 *
	 */
	@Keyword(keywordObject = "HomePage")
	public static void clickOptionInPrimaryNavigationMenu(String navigationPathInDropdown) {
		try {
			//Split the string from ->
			String[] arrOfString= navigationPathInDropdown.split("->");

			//for loop based on substrings created from parameter
			for(int i=0;i<arrOfString.length;i++) {
				WebUI.findWebElement(findTestObject('Object Repository/PageHomePage/primaryMenuNavigation', ['menuOption' :arrOfString[i] ])).click();
			}
		}catch (Exception e) {
			WebUI.comment("Exception :: " + e.getMessage())
			KeywordUtil.markFailedAndStop("Exception while clicking on option in Primary Navigation Menu");
		}
	}
}
