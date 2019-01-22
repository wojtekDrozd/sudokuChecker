package pl.edu.agh.mwo.java2;

import java.io.File;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class App {

	public static void main(String[] args) {
		boolean resultSyntax;
		boolean resultSemantics;
		try {
			Workbook wb = WorkbookFactory.create(new File("sudoku.xlsx"));

			SudokuBoardChecker sudoku = new SudokuBoardChecker(wb);
			
			// iterating over sheets in workbook
			for (int i = 0; i < 7; i++) {
				
				// checking sudoku board syntax and printing result
				resultSyntax = sudoku.verifyBoardStructure(i);
				if (resultSyntax) {
					System.out.println("Number " + (i + 1) + " is syntactically correct");
				} else {
					System.out.println("Number " + (i + 1) + " is syntactically incorrect");
				}
				
				// checking sudoku board semantics and printing result
				resultSemantics = sudoku.verifyBoardSemantics(i);
				if (resultSemantics) {
					System.out.println("Number " + (i + 1) + " is semantically correct");
				} else {
					System.out.println("Number " + (i + 1) + " is semantically incorrect");
				}
				System.out.println();
			}

		} catch (EncryptedDocumentException | InvalidFormatException | IOException e) {
			e.printStackTrace();
		}
	}

}
