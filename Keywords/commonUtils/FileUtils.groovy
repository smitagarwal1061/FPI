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
import org.apache.commons.io.FilenameUtils;
import java.text.SimpleDateFormat
import java.util.Date;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.FileSystems;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import com.kms.katalon.core.configuration.RunConfiguration;
import internal.GlobalVariable

public class FileUtils {

	/**
	 * Verify if file exists at download path 
	 *
	 * @param hyperlinkObject - link clicked to download file from web page
	 * @param downloadPath - location of downloaded file
	 */
	@Keyword(keywordObject = "File")
	public static void verifyFileExist(String hyperlinkObject,String downloadPath) {
		try {
			//Code to get the expected file name from URL
			String urlAttrValue=WebUI.getAttribute(findTestObject(hyperlinkObject), "href");
			URL url = new URL(urlAttrValue);
			String expectedDownloadedFileName = FilenameUtils.getName(url.getPath());

			//Code to check if file exists at download location
			String filePath=downloadPath+"/"+expectedDownloadedFileName;
			File file=new File(filePath);
			if(file.exists())
			{
				KeywordUtil.markPassed("File exists at location ::  "+filePath);
			}
			else {
				KeywordUtil.markFailed("File does not exist at location ::  "+filePath);
			}
		}catch (Exception e) {
			WebUI.comment("Exception :: " + e.getMessage())
			KeywordUtil.markFailedAndStop("Exception while finding a file");		
		}
	}

	/**
	 * Move the file to project reports directory
	 *
	 * @param hyperlinkObject - link clicked to download file from web page
	 * @param sourceFolder - source location of the file 
	 */
	@Keyword(keywordObject = "File")
	public static void moveFileToProjectReportsDirectory(String hyperlinkObject,String sourceFolder) {
		try {
			//Code to get the expected file name from URL
			String urlAttrValue=WebUI.getAttribute(findTestObject(hyperlinkObject), "href");
			URL url = new URL(urlAttrValue);
			String expectedDownloadedFileName = FilenameUtils.getName(url.getPath());

			//Code to create a folder based on current timestamp
			String timestamp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			String destinationFolder=System.getProperty("user.dir")+"/Reports/"+timestamp ;
			File folder=new File(destinationFolder);
			folder.mkdir();

			//Code to move downloaded file from source to destination
			Path source=Paths.get(sourceFolder+"/"+expectedDownloadedFileName);
			Path destination=Paths.get(destinationFolder+"/"+expectedDownloadedFileName);
			Files.move(source,destination);
			KeywordUtil.logInfo("File is moved succesfully to project Reports directory");
		}catch (Exception e) {
			WebUI.comment("Exception :: " + e.getMessage())
			KeywordUtil.markFailedAndStop("Exception while moving a file");
		}
	}
}
