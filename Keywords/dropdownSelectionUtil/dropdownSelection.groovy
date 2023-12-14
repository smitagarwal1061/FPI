package dropdownSelectionUtil

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

public class dropdownSelection {
	/**
	 * Select option from dropdown
	 *
	 * @param parentDropdownOption  object for parent dropdown
	 * @param  optionToSelectText  object for option to be selected inside dropdown
	 */
	@Keyword
	public static void clickOnDropdownOption(String parentDropdownOption, String optionToSelectText) {
		try {
			//To click on parent dropdown option
			WebUI.findWebElement(findTestObject('Object Repository/PageDropdownSelection/parentDropdown', ['parentOptionToSelect' :parentDropdownOption ])).click();
			KeywordUtil.logInfo("Clicked on parent dropdown option")
			//To click on option inside dropdown
			WebUI.findWebElement(findTestObject('Object Repository/PageDropdownSelection/childOptionsInDropdown', ['optionToSelect' :optionToSelectText ])).click();
			KeywordUtil.logInfo("Clicked on child option in dropdown")
		}catch (Exception e) {
			//e.printStackTrace();
			WebUI.comment("Caught an Exception :: " + e.getMessage())
			KeywordUtil.markFailedAndStop("Exception while clicking on dropdown options");
		}
	}
}
