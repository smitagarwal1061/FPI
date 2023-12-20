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
	 * @param webTableNameOne - Test Object for WebTable 1 referred by OR
	 * @param webTableNameTwo - Test Object for WebTable 2 referred by OR
	 */
	@Keyword(keywordObject = "WebTable")
	public static void compareWebTable(String primaryKeyName, TestObject webTableNameOne, TestObject webTableNameTwo) {
		try {
			/*WebElement webTableOne = WebUI.findWebElement(webTableNameOne, 0)
			 WebElement webTableTwo = WebUI.findWebElement(webTableNameTwo, 0)*/
			//Get WebTable records as Map
			Map<String, Map<String, String>> actMap=getWebTableAsMap(webTableNameOne, primaryKeyName);
			Map<String, Map<String, String>> expMap=getWebTableAsMap(webTableNameTwo, primaryKeyName);
			if (actMap.size() != expMap.size()) {
				KeywordUtil.markFailedAndStop("Record count MISMATCH :: " + "Actual record count :: " + actMap.size()
						+ " Expected record count :: " + expMap.size())
			} else if (actMap.size() == 0) {
				KeywordUtil.markFailedAndStop("Map empty :: " + "Actual record count :: " + actMap.size()
						+ "Expected record count :: " + expMap.size())
			} else {
				KeywordUtil.markPassed("Record count MATCHED :: " + "Actual record count :: " + actMap.size()
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
	 * Report the WebTable Map records differences
	 * @param recordKeyName - Key Name of Table record
	 * @param recordKeyValue - Key Value of Table record
	 * @param entriesDiffering - Entries differing Map object
	 * @param entriesOnlyOnLeft - Record entries differing Right Table Map object
	 * @param entriesOnlyOnRight - Record entries differing of Left Table Map object
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
	 * @param webTableName - TestObject for WebTable referred by OR
	 * @param keyName - PrimaryKey for WebTable
	 */
	public static Map<String, Map<String, String>> getWebTableAsMap(TestObject webTableName, String keyName) {
		//WebElement tableElement = WebUI.findWebElement(webTableLocator, 0)
		Map<String, Map<String, String>> webTableRecords = new LinkedHashMap();
		int recordCounter = 0;
		try {
			//Get the WebTable into a List of String
			List<String> webTableData = readWebTableData(webTableName);
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
	 * @param webTableName - TestObject for WebTable referred by OR
	 */
	public static List<String> readWebTableData(TestObject webTableName) {
		WebElement tableElement = WebUI.findWebElement(webTableName, 0)
		List<String> tableData = new ArrayList<>();
		try {
			// Get all rows
			List<WebElement> rows = tableElement.findElements(By.tagName("tr"));
			for (WebElement row : rows) {
				StringBuilder rowData = new StringBuilder();
				List<WebElement> cells = row.findElements(By.tagName("th"));
				// Get both headers and cells in one go
				cells.addAll(row.findElements(By.tagName("td")));
				// Concatenate cell data efficiently using StringBuilder
				for (int i = 0; i < cells.size(); i++) {
					rowData.append(cells.get(i).getText());
					if (i < cells.size() - 1) {
						rowData.append(",");
					}
				}
				tableData.add(rowData.toString());
			}
		}
		catch (Exception e) {
			WebUI.comment("Exception :: " + e.getMessage());
            KeywordUtil.markFailedAndStop("Exception in readWebTableData, unable to read table data");
		}
		return tableData;
	}

	@Keyword(keywordObject = "WebTable")
	/**
	 * Get Web table cell value
	 * @param webTableName - TestObject for Web Table
	 * @param primaryKey - Primary key for Web Table
	 * @param rowName - Row Name of WebTable
	 * @param columnName - ColumnName of WebTable
	 */
	public static String getWebTableCellValue(TestObject webTableName,String primaryKey,String rowName,String columnName) {
		try {
			String cellValue;
			Map<String, Map<String, String>> webTableMap = getWebTableAsMap(webTableName, primaryKey);
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
				KeywordUtil.markFailedAndStop("Cell with row name '" + rowName + "' and column name '" + columnName + "' not found in the table.")
			}
			return cellValue;
		}
		catch (Exception e) {
			WebUI.comment("Exception :: " + e.getMessage())
			KeywordUtil.markFailedAndStop("Exception while fetching cell value from WebTable");
		}
	}
}
