package homePageUtils

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import org.junit.Before
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
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

import groovy.inspect.swingui.BytecodeCollector

import com.kms.katalon.core.util.KeywordUtil

import org.openqa.selenium.By
import org.openqa.selenium.WebElement

import internal.GlobalVariable

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
