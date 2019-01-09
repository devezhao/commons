package cn.devezhao.commons.excel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * CSV 读取 .csv
 * 
 * @author zhaofang123@gmail.com
 * @since 06/05/2017
 */
public class CSVReader extends ExcelReader  {

	private BufferedReader bufferedReader;
	
	private List<String> numberLines = new ArrayList<>();
	
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
			InputStreamReader isr = new InputStreamReader(new FileInputStream(csv), charsetName);
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
		if (rowCount == -1) {
			try {
				String l = null;
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
		String l = numberLines.get(rowIndex++);
		if (l == null) {
			return null;
		}
		return parseLine(l);
	}
	
	/**
	 * @param line
	 * @return
	 */
	private Cell[] parseLine(String line) {
		String line_s[] = line.split(",");
		Cell[] row = new Cell[line_s.length];
		for (int i = 0; i < line_s.length; i++) {
			if (StringUtils.isEmpty(line_s[i])) {
				row[i] = Cell.NULL;
			} else {
				row[i] = new Cell(line_s[i]);
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
