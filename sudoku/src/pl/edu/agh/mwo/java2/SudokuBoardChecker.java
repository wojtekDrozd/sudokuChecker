package pl.edu.agh.mwo.java2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class SudokuBoardChecker {

	Workbook workbook;

	public SudokuBoardChecker(Workbook workbook) {
		this.workbook = workbook;
	}

	/* checking for sudoku syntax
	this method checks if worksheets 
	contain only blank or proper numeric values*/
	public boolean verifyBoardStructure(int sheetIndex) {

		Sheet sheet = workbook.getSheetAt(sheetIndex);
		// iterating over sheet cells and returning false if bad value found
		for (int rowNum = 0; rowNum < 9; rowNum++) {
			Row row = sheet.getRow(rowNum);
			for (int cellNum = 0; cellNum < 9; cellNum++) {
				Cell cell = row.getCell(cellNum);
				CellType cellType = cell.getCellTypeEnum();
				switch (cellType) {
				case BLANK:
					break;
				case NUMERIC:
					double cValue = cell.getNumericCellValue();
					if (cValue < 1.0 || cValue > 9.0) {
						return false;
					}
					break;
				case BOOLEAN:
					return false;
				case STRING:
					return false;
				case FORMULA:
					return false;
				case ERROR:
					return false;
				default:
					break;
				}
			}
		}
		return true;
	}

	/* checking sudoku semantics
	this method checks if sudoku boards within sheets are correct
	rows, columns and 3x3 squares are checked for duplicate values*/
	public boolean verifyBoardSemantics(int sheetIndex) {

		// this prevents from checking semantics in syntacticly incorrect boards
		if (!verifyBoardStructure(sheetIndex)) {
			return false;
		}

		/* HashMap will store key value pairs for sudoku board cells
		keys are cell's indexes and values are cell values*/
		HashMap<Integer, Double> m = new HashMap<>();

		// Iterating over sheet and populating HashMap
		Sheet sheet = workbook.getSheetAt(sheetIndex);
		for (int rowNum = 0; rowNum < 9; rowNum++) {
			Row row = sheet.getRow(rowNum);
			for (int cellNum = 0; cellNum < 9; cellNum++) {
				Cell cell = row.getCell(cellNum);
				CellType cellType = cell.getCellTypeEnum();
				if (cellType.equals(CellType.NUMERIC)) {
					double value = cell.getNumericCellValue();
					m.put((rowNum * 10 + cellNum), value);
				} else if (cellType.equals(CellType.BLANK)) {
					m.put((rowNum * 10 + cellNum), 0.0);
				}
			}
		}

		/* performing check for rows
		iterating over rows and checking duplicate values*/
		for (int j = 0; j < 9; j++) {
			ArrayList<Double> rowList = new ArrayList<>();
			Set<Double> rowSet = new HashSet<>();
			for (int i = 10 * j; i < 9 + 10 * j; i++) {
				Double x = m.get(i);
				if (x != 0.0) {
					rowList.add(x);
					rowSet.add(x);
					if (rowList.size() != rowSet.size()) {
						return false;
					}
				}
			}
			rowList.clear();
			rowSet.clear();
		}
		/* performing check for columns
		iterating over columns and checking for duplicate values*/
		for (int j = 0; j < 9; j++) {
			ArrayList<Double> colList = new ArrayList<>();
			Set<Double> colSet = new HashSet<>();
			for (int i = j; i < 90; i = i + 10) {
				Double x = m.get(i);
				if (x != 0.0) {
					colList.add(x);
					colSet.add(x);
					if (colList.size() != colSet.size()) {
						return false;
					}
				}

			}
			colList.clear();
			colSet.clear();
		}
		/*performing check for 3x3 squares
		building HashMap with indexes from HashMap with data to enable
		iteration*/
		HashMap<Integer, List<Integer>> m2 = new HashMap<>();
		m2.put(0, Arrays.asList(new Integer[] { 0, 1, 2, 10, 11, 12, 20, 21, 22 }));
		m2.put(1, Arrays.asList(new Integer[] { 3, 4, 5, 13, 14, 15, 23, 24, 25 }));
		m2.put(2, Arrays.asList(new Integer[] { 6, 7, 8, 16, 17, 18, 26, 27, 28 }));
		m2.put(3, Arrays.asList(new Integer[] { 30, 31, 32, 40, 41, 42, 50, 51, 52 }));
		m2.put(4, Arrays.asList(new Integer[] { 33, 34, 35, 43, 44, 45, 53, 54, 55 }));
		m2.put(5, Arrays.asList(new Integer[] { 36, 37, 38, 46, 47, 48, 56, 57, 58 }));
		m2.put(6, Arrays.asList(new Integer[] { 60, 61, 62, 70, 71, 72, 80, 81, 82 }));
		m2.put(7, Arrays.asList(new Integer[] { 63, 64, 65, 73, 74, 75, 83, 84, 85 }));
		m2.put(8, Arrays.asList(new Integer[] { 66, 67, 68, 76, 77, 78, 86, 87, 88 }));

		// iterating through 3x3 squares and checking for duplicate values
		for (int j = 0; j < 9; j++) {
			ArrayList<Double> sqList = new ArrayList<>();
			Set<Double> sqSet = new HashSet<>();
			for (int i = 0; i < 9; i++) {
				List<Integer> indexes = m2.get(j);
				int z = indexes.get(i);
				Double x = m.get(z);
				if (x != 0.0) {
					sqList.add(x);
					sqSet.add(x);
					if (sqList.size() != sqSet.size()) {
						return false;
					}
				}
			}
			sqList.clear();
			sqSet.clear();
		}
		// returning true if all checks are passed
		return true;
	}
}
