package com.suryoday.payment.payment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelToInsertQuery {
	public static void main(String[] args) {
		// Define the input Excel file path and database connection details
		String excelFilePath = "input.xlsx"; // Replace with the path to your Excel file
		String jdbcUrl = "jdbc:mysql://localhost:8081/your_database"; // Replace with your database URL
		String username = "your_username";
		String password = "your_password";

		Connection connection = null;

		try {
			FileInputStream excelFile = new FileInputStream(new File(excelFilePath));
			Workbook workbook = new XSSFWorkbook(excelFile);

			// Establish a database connection
			connection = DriverManager.getConnection(jdbcUrl, username, password);

			// Iterate through each sheet in the Excel workbook
			for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
				Sheet sheet = workbook.getSheetAt(sheetIndex);

				// Assuming the first row contains column names, so skip it
				for (Row row : sheet) {
					String tableName = "your_table_name"; // Replace with your table name
					StringBuilder queryBuilder = new StringBuilder("INSERT INTO ");
					queryBuilder.append(tableName).append(" (");

					int cellIndex = 0;
					for (Cell cell : row) {
						if (cellIndex > 0) {
							queryBuilder.append(", ");
						}
						queryBuilder.append(cell.toString());
						cellIndex++;
					}

					queryBuilder.append(") VALUES (");

					// Assuming the number of cells in a row matches the number of columns in the
					// table
					for (int i = 0; i < cellIndex; i++) {
						if (i > 0) {
							queryBuilder.append(", ?");
						} else {
							queryBuilder.append("?");
						}
					}

					queryBuilder.append(")");

					String insertQuery = queryBuilder.toString();
					PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

					// Bind values from Excel to the prepared statement
					int parameterIndex = 1;
					for (Cell cell : row) {
						preparedStatement.setString(parameterIndex, cell.toString());
						parameterIndex++;
					}

					// Execute the insert query
					preparedStatement.executeUpdate();

					// Close the prepared statement
					preparedStatement.close();
				}
			}

			// Close the Excel workbook
			workbook.close();

			System.out.println("Data from Excel has been successfully inserted into the database.");
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		} finally {
			// Close the database connection
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
