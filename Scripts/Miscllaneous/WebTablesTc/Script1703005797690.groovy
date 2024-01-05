import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

WebUI.openBrowser('https://rahulshettyacademy.com/AutomationPractice/')

WebUI.maximizeWindow()

WebUI.executeJavaScript('window.scrollBy(0,750)', [])

String cellValue = CustomKeywords.'commonUtils.WebTableUtils.getWebTableCellValue'(findTestObject('Object Repository/WebTable/webTable2'), 
    findTestObject('Object Repository/WebTable/webTable2'), 'Name', 'Alex', 'Amount')

println(cellValue)

int total=CustomKeywords.'earningsUtils.Earning.calculateTotal'(findTestObject('Object Repository/WebTable/webTable2'), findTestObject('Object Repository/WebTable/webTable2'), 'Name', 'Amount')

println(total)

CustomKeywords.'commonUtils.WebTableUtils.compareWebTable'('Name', findTestObject('Object Repository/WebTable/webTable3'), 
    findTestObject('Object Repository/WebTable/webTable3'), findTestObject('WebTable/webTable2'), findTestObject('WebTable/webTable2'))



