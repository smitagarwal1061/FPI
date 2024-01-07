package homePageUtils

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.driver.DriverFactory
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
			WebDriver driver= DriverFactory.getWebDriver()
			String[] arrOfString= navigationPathInDropdown.split("->");
			String xpath;
			WebElement parentMenuOption;
			WebElement childMenuOption;
			for(int i=0;i<arrOfString.length;i++) {
				xpath = "//a[(text()='"+arrOfString[i]+"')]" ;
				if(i==0) {
					parentMenuOption=driver.findElement(By.xpath(xpath))
					parentMenuOption.click();
				}
				else{
					childMenuOption=parentMenuOption.findElement(By.xpath(xpath))
					childMenuOption.click();
					parentMenuOption=childMenuOption
				}
			}
		}catch (Exception e) {
			WebUI.comment("Exception :: " + e.getMessage())
			KeywordUtil.markFailedAndStop("Exception while clicking on option in Primary Navigation Menu");
		}
	}
}
