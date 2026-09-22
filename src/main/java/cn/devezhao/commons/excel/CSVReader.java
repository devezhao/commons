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
				boolean firstLine = true;
				while ((l = bufferedReader.readLine()) != null) {
					if (firstLine) {
						l = stripBom(l);
						firstLine = false;
					}
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
	public boolean hasNext() {
		getRowCount();
		return getRowIndex() < rowCount;
	}
	
	@Override
	public Cell[] next() {
		if (numberLines == null) getRowCount();  // init
		if (rowIndex >= numberLines.size()) return null;  // last
	
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
		List<String> fields = new ArrayList<>();
		List<Boolean> quoted = new ArrayList<>();
		parseCsvFields(line, fields, quoted);
	
		while (!fields.isEmpty() && !quoted.get(quoted.size() - 1)
				&& fields.get(fields.size() - 1).isEmpty()) {
			fields.remove(fields.size() - 1);
			quoted.remove(quoted.size() - 1);
		}
	
		Cell[] row = new Cell[fields.size()];
		for (int i = 0; i < fields.size(); i++) {
			String v = fields.get(i);
			if (StringUtils.isEmpty(v)) {
				row[i] = Cell.valueOf(rowNo, i);
			} else {
				row[i] = Cell.valueOf(v, rowNo, i);
			}
		}
		return row;
	}
	
	/**
	 * 按标准 CSV 语法拆分字段：支持双引号包裹（内含逗号）与双引号转义（"" 转义为一个双引号）
	 *
	 * @param line
	 * @param fields 拆分后的字段值
	 * @param quoted 各字段是否为引号包裹
	 */
	private static void parseCsvFields(String line, List<String> fields, List<Boolean> quoted) {
		StringBuilder field = new StringBuilder();
		boolean inQuotes = false;
		boolean isQuoted = false;
	
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);
			if (inQuotes) {
				if (c == '"') {
					if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
						field.append('"');
						i++;
					} else {
						inQuotes = false;
					}
				} else {
					field.append(c);
				}
			} else if (c == '"') {
				inQuotes = true;
				isQuoted = true;
			} else if (c == ',') {
				fields.add(field.toString());
				quoted.add(isQuoted);
				field.setLength(0);
				isQuoted = false;
			} else {
				field.append(c);
			}
		}
		fields.add(field.toString());
		quoted.add(isQuoted);
	}
	
	/**
	 * 去除行首的 UTF-8 BOM 字符（\uFEFF）
	 *
	 * @param line
	 * @return
	 */
	private static String stripBom(String line) {
		if (line != null && !line.isEmpty() && line.charAt(0) == '\uFEFF') {
			return line.substring(1);
		}
		return line;
	}
	
	@Override
	public void close() {
		ExcelReaderFactory.close(bufferedReader);
		this.bufferedReader = null;
	}
}
