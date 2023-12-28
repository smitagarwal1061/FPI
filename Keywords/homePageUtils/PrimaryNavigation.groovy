package homePageUtils

import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI



public class PrimaryNavigation {
	/**
	 * Select option from primary navigation menu
	 * @param navigationPathInDropdown - path to navigate in primary navigation menu
	 *
	 */
	@Keyword(keywordObject = "HomePage")
	public static void clickOptionInPrimaryNavigationMenu(String navigationPathInDropdown) {
		try {
			//Split the string from ->
			String[] arrOfString= navigationPathInDropdown.split("->");

			//for loop based on substrings created from parameter
			for(int i=0;i<arrOfString.length;i++) {
				WebUI.findWebElement(findTestObject('Object Repository/PageHomePage/primaryMenuNavigation', ['menuOption' :arrOfString[i]])).click();
			}
		}catch (Exception e) {
			WebUI.comment("Exception :: " + e.getMessage())
			KeywordUtil.markFailedAndStop("Exception while clicking on option in Primary Navigation Menu");
		}
	}
}
