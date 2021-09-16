package cn.devezhao.commons.excel;

import org.apache.commons.lang.StringUtils;

import java.io.*;
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
	
	private final List<String> numberLines = new ArrayList<>();
	
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
		String[] lines = line.split(",");
		Cell[] row = new Cell[lines.length];
		for (int i = 0; i < lines.length; i++) {
			if (StringUtils.isEmpty(lines[i])) {
				row[i] = Cell.NULL;
			} else {
				row[i] = new Cell(lines[i]);
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
