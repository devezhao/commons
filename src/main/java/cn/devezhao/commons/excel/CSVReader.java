package cn.devezhao.commons.excel;

import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV 读取 .csv
 * 
 * @author zhaofang123@gmail.com
 * @since 06/05/2017
 */
public class CSVReader extends ExcelReader  {

	private BufferedReader bufferedReader;
	
	private List<String> numberLines;
	
	/**
	 * @param csv
	 */
	public CSVReader(File csv) {
		this(csv, "utf-8");
	}
	
	/**
	 * @param csv
	 * @param charsetName
	 */
	public CSVReader(File csv, String charsetName) {
		try {
			InputStreamReader isr = new InputStreamReader(Files.newInputStream(csv.toPath()), charsetName);
			this.bufferedReader = new BufferedReader(isr);
		} catch (IOException e) {
			close();
			throw new ExcelReaderException(e);
		}
		this.rowCount = -1;
	}
	
	@Override
	public String[] getSheetNames() {
		return new String[] { "0" };
	}
	
	@Override
	public void sheetAt(int index) {
		// Unsupportted
	}
	
	@Override
	public int getRowCount() {
		if (numberLines == null) {
			numberLines = new ArrayList<>();
			try {
				String l;
				while ((l = bufferedReader.readLine()) != null) {
					numberLines.add(l);
				}
			} catch (IOException e) {
				throw new ExcelReaderException(e);
			} finally {
				close();
			}
			rowCount = numberLines.size();
		}
		return rowCount;
	}
	
	@Override
	public Cell[] next() {
		if (numberLines == null) getRowCount();  // init
		if (rowIndex == numberLines.size()) return null;  // last

		String l = numberLines.get(rowIndex++);
		if (l == null) {
			return null;
		}
		return parseLine(l, rowIndex - 1);
	}

	/**
	 * @param line
	 * @return
	 */
	private Cell[] parseLine(String line, int rowNo) {
		String[] lines = line.split(",");
		Cell[] row = new Cell[lines.length];
		for (int i = 0; i < lines.length; i++) {
			if (StringUtils.isEmpty(lines[i])) {
				row[i] = Cell.valueOf(rowNo, i);
			} else {
				row[i] = Cell.valueOf(lines[i], rowNo, i);
			}
		}
		return row;
	}
	
	@Override
	public void close() {
		ExcelReaderFactory.close(bufferedReader);
		this.bufferedReader = null;
	}
}
