package commonUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.google.common.collect.MapDifference
import com.google.common.collect.Maps
import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.util.KeywordUtil
import java.util.Map.Entry
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI


public class WebTableUtils {

	/**
	 * Compare Web Table data
	 * @param primaryKeyName - WebTable primary key
	 * @param webTable1 - WebElement for WebTable 1
	 * @param webTable2 - WebElement for WebTable 1
	 */
	@Keyword(keywordObject = "WebTable")

	public static void compareWebTableElements(String primaryKeyName, TestObject webTableName1, TestObject webTableName2) {
		try {
			WebElement webTable1 = WebUI.findWebElement(webTableName1, 0)
			WebElement webTable2 = WebUI.findWebElement(webTableName2, 0)
			//Get WebTable records as Map
			Map<String, Map<String, String>> actMap=getWebTableAsMap(webTable1, primaryKeyName);
			Map<String, Map<String, String>> expMap=getWebTableAsMap(webTable2, primaryKeyName);
			if (actMap.size() != expMap.size()) {
				KeywordUtil.markFailed("Record count MISMATCH :: " + "Actual record count :: " + actMap.size()
						+ " Expected record count :: " + expMap.size())
			} else if (actMap.size() == 0) {
				KeywordUtil.markFailed("Map empty :: " + "Actual record count :: " + actMap.size()
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
			e.printStackTrace();
			KeywordUtil.markFailed("Exception in compareWebTableElements")
		}
	}

	/**
	 * Compare Actual and Expected WebTable Map records
	 * @param PrimaryKey of WebTable
	 * @param Actual WebTable Map object
	 * @param Expected WebTable Map object
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
						KeywordUtil.markPassed("All values matched for " + primaryKeyName + " :: " + actRecordKey)
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			KeywordUtil.markFailed("Exception in compareActExpMap, unable to compare WebTable Map records")
		}
	}

	/**
	 * Get the Expected WebTable Map records missing from Actual WebTable Map
	 * @param primaryKeyName - Primary Key of WebTable
	 * @param actMap - Actual WebTable Map object
	 * @param expMap - Expected WebTable Map object
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
	 * @param Key Name of WebTable record
	 * @param Key Value of WebTable record
	 * @param Recordentries differing Map object
	 * @param Recordentries differing Right WebTable Map object
	 * @param Recordentries differing of Left WebTable Map object
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
			e.printStackTrace();
			KeywordUtil.markFailed("Exception in reportMapDifference, unable to find differences in WebTables")
		}
	}

	/**
	 * Get the WebTable as Map
	 * @param table - WebElement for WebTable
	 * @param keyName - PrimaryKey for WebTable
	 */
	public static Map<String, Map<String, String>> getWebTableAsMap(WebElement table, String keyName) {
		Map<String, Map<String, String>> webTableRecords = new LinkedHashMap();
		int recordCounter = 0;
		try {
			//Get the WebTable into a List of String
			List<String> webTableData = readWebTableData(table);
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
	 * @param tableElement - WebElement for WebTable
	 */
	public static List<String> readWebTableData(WebElement tableElement) {
		List<String> tableData = new ArrayList<>();
		try {
			String rowData = null;
			tableData.add(rowData);
			List<WebElement> headers = tableElement.findElements(By.xpath("tr/th"));
			int count = 0;
			for (WebElement header : headers) {
				// Extract data from each header and add it to the list
				if (count == 0) {
					rowData = header.getText();
				} else {
					rowData = rowData + "," + header.getText();
				}
				count++;
				tableData.add(rowData);
			}
			// Get all rows
			List<WebElement> rows = tableElement.findElements(By.tagName("tr")).drop(1);
			for (WebElement row : rows) {
				// Get all cells in the current row
				List<WebElement> cells = row.findElements(By.tagName("td"));
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
			e.printStackTrace();
			KeywordUtil.markFailed("Exception in readWebTableData, unable to read data from WebTable")
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
			WebElement webTable = WebUI.findWebElement(webTableName, 0)
			Map<String, Map<String, String>> webTableMap = getWebTableAsMap(webTable, primaryKey);

			for (Entry<String, Map<String, String>> entry : webTableMap.entrySet()) {
				if (entry.getKey().equals(rowName)) {
					cellValue = entry.getValue().get(columnName);
					break;
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
