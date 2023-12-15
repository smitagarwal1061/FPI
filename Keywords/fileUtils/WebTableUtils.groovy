package fileUtils

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import org.openqa.selenium.WebElement

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import org.openqa.selenium.By;

import internal.GlobalVariable

public class WebTableUtils {

	/**
	 * Get the Cell Value of the WebTable
	 * @param WebElement of webTable
	 * @param RowName of WebTable
	 * @param ColumnName of WebTable
	 */
	@Keyword
	public static String getCellValueWebTable(WebElement webTable,String rowName,String columnName) {
		try {
			//Get the rows of the WebTable in a list
			List<WebElement> rows = webTable.findElements(By.xpath("tr/th[@scope='row']"));
			//Get the columns of the WebTable in a list
			List<WebElement> columns = webTable.findElements(By.xpath("tr/th[@scope='col']"));

			boolean cellFound = false;
			String cellName = "";

			for (int i = 0; i < rows.size(); i++) {
				if (rows.get(i).getText().equalsIgnoreCase(rowName)) {
					for (int j = 0; j < columns.size(); j++) {
						if (columns.get(j).getText().equalsIgnoreCase(columnName)) {
							cellFound = true;
							cellName = webTable.findElement(By.xpath("tr[" + (i + 2) + "]/td[" + (j + 1) + "]")).getText();
							break; // Exit inner loop once cell found
						}
					}
				}
			}
			
			if (!cellFound) {
				KeywordUtil.markFailed("Cell with row name '" + rowName + "' and column name '" + columnName + "' not found in the table.")
			}
			return cellName;
		}
		catch (Exception e)
		{
			WebUI.comment("Caught an Exception :: " + e.getMessage())
		}
	}
}
