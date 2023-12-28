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
	 * @param webTableObjectOne - WebTable1 object referenced from OR
	 * @param webTableObjectTwo - WebTable2 object referenced from OR
	 */
	@Keyword(keywordObject = "WebTable")
	public static void compareWebTable(String primaryKeyName, TestObject webTableObjectOne, TestObject webTableObjectTwo) {
		try {
			//Get WebTable records as Map
			Map<String, Map<String, String>> actMap=getWebTableAsMap(webTableObjectOne, primaryKeyName);
			Map<String, Map<String, String>> expMap=getWebTableAsMap(webTableObjectTwo, primaryKeyName);
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
	 * @param webTableObject - WebTable object referenced from OR
	 * @param keyName - PrimaryKey for WebTable
	 */
	public static Map<String, Map<String, String>> getWebTableAsMap(TestObject webTableObject, String keyName) {
		Map<String, Map<String, String>> webTableRecords = new LinkedHashMap();
		int recordCounter = 0;
		try {
			//Get the WebTable into a List of String
			List<String> webTableData = readWebTableData(webTableObject);
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
	 * @param webTableObject - TestObject for WebTable referred by OR
	 */
	public static List<String> readWebTableData(TestObject webTableObject) {
		WebElement tableElement = WebUI.findWebElement(webTableObject, 0)
		List<String> tableData = new ArrayList<>();
		try {
			String rowData = null;
			int count = 0

			// Get all rows
			List<WebElement> rows = tableElement.findElements(By.tagName("tr"));

			//Get all headers into list
			List<WebElement> headers = rows.get(0).findElements(By.tagName("th"));
			for (WebElement header : headers) {
				// Extract data from each header and add it to the list
				if (count == 0) {
					rowData = header.getText();
				} else {
					rowData = rowData + "," + header.getText();
				}
				count++;
			}
			tableData.add(rowData);

			//Get data of table rows
			for (int i = 1; i < rows.size(); i++) {
				// Get all cells in the current row
				count=0;
				List<WebElement> cells = rows.get(i).findElements(By.tagName("td"));
				for (WebElement cell : cells) {
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
	 * @param webTableObject - WebTable object referenced from OR
	 * @param primaryKey - primary key of WebTable
	 * @param primaryKeyValue - primary key value of row
	 * @param columnName  - column to fetch cell value
	 */
	public static String getWebTableCellValue(TestObject webTableObject,String primaryKey,String rowName,String columnName) {
		try {
			String cellValue;
			Map<String, Map<String, String>> webTableMap = getWebTableAsMap(webTableObject, primaryKey);
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
