package fileDownloadUtils

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

import internal.GlobalVariable

public class fileDownload {
	
	/**
	 * Check if file is downloaded.
	 *
	 * @param hyperlinkObject String type object for link
	 * @param  downloadPath String type object for download path
	 */
	@Keyword
	public static void fileExist(String hyperlinkObject,String downloadPath) {
		try {

			//Code to get the expected file name from URL
			String urlAttrValue=WebUI.getAttribute(findTestObject(hyperlinkObject), "href");
			URL url = new URL(urlAttrValue);
			String expectedDownloadedFileName = FilenameUtils.getName(url.getPath());
			KeywordUtil.logInfo("Name of file downloaded :: "+expectedDownloadedFileName);

			//Code to check if file exists at download location
			String filePath=downloadPath+"/"+expectedDownloadedFileName;
			File file=new File(filePath);

			if( file.exists())
			{
				KeywordUtil.markPassed("File exists at location ::  "+downloadPath);

			}
			else {
				KeywordUtil.markFailed("File doesn't exist.");
			}

		} catch (Exception e) {
			e.printStackTrace();
			KeywordUtil.markFailedAndStop("Exception while finding a file");
		}



	}

	/**
	 * Move the file to other folder
	 *
	 * @param hyperlinkObject String type object for link
	 * @param  sourceFolder String type object for source folder path
	 */
	@Keyword
	public static void moveFileToProjectDirFolder(String hyperlinkObject,String sourceFolder) {

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
			File file=new File(destinationFolder+"/"+expectedDownloadedFileName);

			//Code to verify if file exists on other location after moving
			if( file.exists())
			{
				KeywordUtil.markPassed("File moved succesfully");

			}
			else {
				KeywordUtil.markFailed("File move failed");
			}

		}catch (Exception e) {
			e.printStackTrace();
			KeywordUtil.markFailedAndStop("Exception while moving a file");
		}
	}
}
