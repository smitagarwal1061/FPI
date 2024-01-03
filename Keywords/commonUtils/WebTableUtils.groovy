package commonUtils

import java.util.Map.Entry

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.google.common.collect.MapDifference
import com.google.common.collect.Maps
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

public class WebTableUtils {

	/**
	 * Compare Web Table data
	 * @param primaryKeyName - WebTable primary key
	 * @param primaryWebTableObjectOne 	- Primary WebTable object one referenced from OR
	 * @param primaryWebTableObjectTwo 	- Primary WebTable object two referenced from OR
	 * @param secondaryWebTableObjectOne - Secondary WebTable object one referenced from OR
	 * @param secondaryWebTableObjectTwo - Secondary WebTable object two referenced from OR
	 */
	@Keyword(keywordObject = "WebTable")
	public static void compareWebTable(String primaryKeyName, TestObject primaryWebTableObjectOne, TestObject primaryWebTableObjectTwo,TestObject secondaryWebTableObjectOne, TestObject secondaryWebTableObjectTwo) {
		try {
			//Get WebTable records as Map
			Map<String, Map<String, String>> actMap=getWebTableAsMap(primaryWebTableObjectOne,primaryWebTableObjectTwo, primaryKeyName);
			Map<String, Map<String, String>> expMap=getWebTableAsMap(secondaryWebTableObjectOne,secondaryWebTableObjectTwo, primaryKeyName);
			if (actMap.size() != expMap.size()) {
				KeywordUtil.markFailedAndStop("Record count MISMATCH :: " + "Actual record count :: " + actMap.size()
						+ " Expected record count :: " + expMap.size())
			} else if (actMap.size() == 0) {
				KeywordUtil.markFailedAndStop("Map empty :: " + "Actual record count :: " + actMap.size()
						+ "Expected record count :: " + expMap.size())
			} else {
				KeywordUtil.logInfo("Record count MATCHED :: " + "Actual record count :: " + actMap.size()
						+ "Expected record count :: " + expMap.size())
			}
			//Method to compare Actual and Expected Map records
			compareActExpMap(primaryKeyName, actMap, expMap);
			//Method to get Expected records missing from Actual Data
			getExpRecordMissingFrmActData(primaryKeyName, actMap, expMap);
		} catch (Exception e) {
			WebUI.comment("Exception :: " + e.getMessage())
			KeywordUtil.markFailedAndStop("Exception while comparing Web Table data");
		}
	}

	/**
	 * Compare Actual and Expected map
	 * @param primaryKeyName - primary key to be used for comparison
	 * @param actMap - actual map
	 * @param expMap - expected map
	 */
	public static void compareActExpMap(String primaryKeyName, Map<String, Map<String, String>> actMap,
			Map<String, Map<String, String>> expMap) {
		try {
			for (String actRecordKey : actMap.keySet()) {
				if (!expMap.containsKey(actRecordKey)) {
					KeywordUtil.markFailed("Searched actual record not found in expected data :: " + primaryKeyName + " :: "
							+ actRecordKey)
				} else {
					//Compare actual and expected map records
					MapDifference<String, String> diff = Maps
							.difference(actMap.get(actRecordKey), expMap.get(actRecordKey));
					if (!diff.areEqual()) {
						Map<String, MapDifference.ValueDifference<String>> entriesDiffering = diff.entriesDiffering();
						Map<String, String> entriesOnlyOnLeft = diff.entriesOnlyOnLeft();
						Map<String, String> entriesOnlyOnRight = diff.entriesOnlyOnRight();
						reportMapDifference(primaryKeyName, actRecordKey, entriesDiffering, entriesOnlyOnLeft,
								entriesOnlyOnRight);
					} else {
						KeywordUtil.logInfo("All values matched for " + primaryKeyName + " :: " + actRecordKey)
					}
				}
			}
		} catch (Exception e) {
			WebUI.comment("Exception :: " + e.getMessage());
			KeywordUtil.markFailedAndStop("Exception in compareActExpMap, unable to compare Table records");
		}
	}

	/**
	 * Get the Expected table map records missing from Actual table map
	 * @param primaryKeyName - Primary Key of WebTable
	 * @param actMap - actual map
	 * @param expMap - expected map
	 */
	public static void getExpRecordMissingFrmActData(String primaryKeyName, Map<String, Map<String, String>> actMap,
			Map<String, Map<String, String>> expMap) {
		// Report records present in expected data but missing in actual data
		for (String expRecordKey : expMap.keySet()) {
			if (!actMap.containsKey(expRecordKey)) {
				KeywordUtil.markFailed("Searched expected record not found in actual data :: " + primaryKeyName + " :: "
						+ expRecordKey);
			}
		}
	}

	/**
	 * Report mismatched records in Map
	 * @param recordKeyName - Key Name of Table record
	 * @param recordKeyValue - Key Value of Table record
	 * @param entriesDiffering - Mismatched records in Map
	 * @param entriesOnlyOnLeft - Records present only in actual Map
	 * @param entriesOnlyOnRight - Records present only in expected Map
	 */
	public static void reportMapDifference(String recordKeyName, String recordKeyValue,
			Map<String, MapDifference.ValueDifference<String>> entriesDiffering, Map<String, String> entriesOnlyOnLeft,
			Map<String, String> entriesOnlyOnRight) {
		try {
			String failureMsg = "Value mismatch for field :: ";
			for (String keyName : entriesOnlyOnLeft.keySet()) {
				String actValue = entriesOnlyOnLeft.get(keyName);
				String expValue = " ** NOT FOUND ** ";
				KeywordUtil.markFailed("Failed " + recordKeyName + " :: " + recordKeyValue + " " + actValue + " " + expValue)
			}
			for (String keyName : entriesOnlyOnRight.keySet()) {
				String actValue = " ** NOT FOUND ** ";
				String expValue = entriesOnlyOnRight.get(keyName);
				KeywordUtil.markFailed("Failed " + recordKeyName + " :: " + recordKeyValue + " " + actValue + " " + expValue)
			}
			for (String keyName : entriesDiffering.keySet()) {
				String actValue = entriesDiffering.get(keyName).leftValue();
				String expValue = entriesDiffering.get(keyName).rightValue();
				KeywordUtil.markFailed("Failed " + recordKeyName + " :: " + recordKeyValue + " " + actValue + " " + expValue)
			}
		} catch (Exception e) {
			WebUI.comment("Exception :: " + e.getMessage());
			KeywordUtil.markFailedAndStop("Exception in reportMapDifference")
		}
	}

	/**
	 * Get the WebTable as Map
	 * @param webTableObjectOne - WebTable One object referenced from OR
	 * @param webTableObjectTwo - WebTable Two object referenced from OR
	 * @param keyName - PrimaryKey for WebTable
	 */
	public static Map<String, Map<String, String>> getWebTableAsMap(TestObject webTableObjectOne, TestObject webTableObjectTwo, String keyName) {
		Map<String, Map<String, String>> webTableRecords = new LinkedHashMap();
		int recordCounter = 0;
		try {
			//Get the WebTable into a List of String
			List<String> webTableData = readWebTableData(webTableObjectOne,webTableObjectTwo);
			String colNames = webTableData.get(0);
			String[] arrColName = colNames.split(",", -1);
			int columnCount = arrColName.length;
			for (String colValues : webTableData) {
				String[] arrColValue = colValues.split(",", -1);
				Map<String, String> fileRecord = new LinkedHashMap<>();
				String mapKey = "";
				for (int i = 0; i < columnCount; i++) {
					String columnName = arrColName[i];
					if (columnName.equalsIgnoreCase(keyName))
						mapKey = arrColValue[i];
					fileRecord.put(columnName, arrColValue[i]);
				}
				if (keyName.equalsIgnoreCase("DEFAULT") || mapKey.equalsIgnoreCase("")) {
					webTableRecords.put(String.valueOf(recordCounter), fileRecord);
					recordCounter++;
				} else
					webTableRecords.put(mapKey, fileRecord);
			}
		} catch (Exception e) {
			WebUI.comment("Exception :: " + e.getMessage());
			KeywordUtil.markFailedAndStop("Exception in getWebTableAsMap, unable to get Table as Map");
		}
		return webTableRecords;
	}

	/**
	 * Read the WebTable Data records
	 * @param webTableObjectOne - TestObject for WebTable One referred by OR
	 * @param webTableObjectTwo - TestObject for WebTable Two referred by OR
	 */
	public static List<String> readWebTableData(TestObject webTableObjectOne, TestObject webTableObjectTwo) {
		WebElement tableElementOne = WebUI.findWebElement(webTableObjectOne, 0)
		WebElement tableElementTwo = WebUI.findWebElement(webTableObjectTwo, 0)
		List<String> tableData = new ArrayList<>();
		try {
			String rowData = null;
			int count = 0

			// Get all rows from WebTableOne
			List<WebElement> rowsWebTableOne = tableElementOne.findElements(By.tagName("tr"));
			// Get all rows from WebTableTwo
			List<WebElement> rowsWebTableTwo = tableElementTwo.findElements(By.tagName("tr"));

			//Compare WebTableOne and WebTableTwo Row records
			if (rowsWebTableOne.size() != rowsWebTableTwo.size()) {
				KeywordUtil.markFailedAndStop("Record count MISMATCH :: " + "WebTable One record count :: " + rowsWebTableOne.size()
						+ ", WebTable Two record count :: " + rowsWebTableOne.size())}

			//Get all headers into list for WebTableOne and WebTableTwo
			List<WebElement> headersWebTableOne = rowsWebTableOne.get(0).findElements(By.tagName("th"));
			List<WebElement> headersWebTableTwo = rowsWebTableTwo.get(0).findElements(By.tagName("th"));

			for (WebElement header : headersWebTableOne) {
				// Extract data from each header and add it to the list
				if (count == 0) {
					rowData = header.getText();
				} else {
					rowData = rowData + "," + header.getText();
				}
				count++;
			}
			for (WebElement header : headersWebTableTwo) {
				// Extract data from each header and add it to the list
				if (count == 0) {
					rowData = header.getText();
				} else {
					rowData = rowData + "," + header.getText();
				}
				count++;
			}
			tableData.add(rowData);

			//Get all the data of rows from WebTableOne and WebTableTwo
			for (int i = 1; i < rowsWebTableOne.size(); i++) {
				// Get all cells in the current row for WebTableOne
				count=0;
				List<WebElement> cellsWebTableOne = rowsWebTableOne.get(i).findElements(By.tagName("td"));
				for (WebElement cell : cellsWebTableOne) {
					// Extract data from each cell and add it to the list
					if (count == 0) {
						rowData = cell.getText();
					} else {
						rowData = rowData + "," + cell.getText();
					}
					count++;
				}
				// Get all cells in the current row for WebTableTwo
				List<WebElement> cellsWebTableTwo = rowsWebTableTwo.get(i).findElements(By.tagName("td"));
				for (WebElement cell : cellsWebTableTwo) {
					// Extract data from each cell and add it to the list
					if (count == 0) {
						rowData = cell.getText();
					} else {
						rowData = rowData + "," + cell.getText();
					}
					count++;
				}
				tableData.add(rowData);
			}
		}
		catch (Exception e) {
			WebUI.comment("Exception :: " + e.getMessage())
			KeywordUtil.markFailedAndStop("Exception while reading data from WebTable");
		}
		return tableData;
	}

	@Keyword(keywordObject = "WebTable")
	/**
	 * Get Web table cell value
	 * @param webTableObjectOne - WebTable object One referenced from OR
	 * @param webTableObjectTwo - WebTable object Two referenced from OR
	 * @param primaryKey - primary key of WebTable
	 * @param primaryKeyValue - primary key value of row
	 * @param columnName  - column to fetch cell value
	 */
	public static String getWebTableCellValue(TestObject webTableObjectOne,TestObject webTableObjectTwo,String primaryKey,String rowName,String columnName) {
		try {
			String cellValue;
			Map<String, Map<String, String>> webTableMap = getWebTableAsMap(webTableObjectOne,webTableObjectTwo, primaryKey);
			//Iterating through Webtable maps
			for (Entry<String, Map<String, String>> entry : webTableMap.entrySet()) {
				if (entry.getKey().equals(rowName)) {
					cellValue = entry.getValue().get(columnName);
					break; //Exit inner loop once found
				}
			}
			if (cellValue != null) {
				KeywordUtil.markPassed("Cell Value for row name: '" + rowName + "' and column name: '" + columnName+"' is '"+cellValue+"'")
			} else {
				KeywordUtil.markFailedAndStop("Cell with row name '" + rowName + "' and column name '" + columnName + "' not found in the table with primarykey: '"+primaryKey+"'")
			}
			return cellValue;
		}
		catch (Exception e) {
			WebUI.comment("Exception :: " + e.getMessage())
			KeywordUtil.markFailedAndStop("Exception while fetching cell value from WebTable");
		}
	}
}
