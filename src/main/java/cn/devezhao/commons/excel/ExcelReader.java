package cn.devezhao.commons.excel;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Excel 读取 .xls
 * 
 * @author zhaofang123@gmail.com
 * @since 06/02/2017
 */
public class ExcelReader implements Iterator<Cell[]>, Closeable {
	
	private Workbook workbook;
	private Sheet sheet;
	
	protected int rowCount = 0;
	protected int rowIndex = 0;
	
	protected ExcelReader() {
		// For subclass ...
	}

	/**
	 * @param excel
	 */
	public ExcelReader(File excel) {
		NPOIFSFileSystem fs = null;
		try {
			fs = new NPOIFSFileSystem(excel);
			workbook = new HSSFWorkbook(fs);
		} catch (IOException e) {
			close();
			throw new ExcelReaderException(e);
		}
		sheetAt(0);
	}
	
	/**
	 * 获取所有 SHEET 名称
	 * 
	 * @return
	 */
	public String[] getSheetNames() {
		List<String> names = new ArrayList<String>();
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			names.add(workbook.getSheetName(i));
		}
		return names.toArray(new String[names.size()]);
	}
	
	/**
	 * 定位 SHEET
	 * 
	 * @param index
	 */
	public void sheetAt(int index) {
		if (index > workbook.getNumberOfSheets()) {
			throw new ExcelReaderException("无效 SHEET 位置: " + index);
		}
		
		sheet = workbook.getSheetAt(index);
		rowCount = sheet.getPhysicalNumberOfRows();
		rowIndex = 0;
	}
	
	/**
	 * 总计行数
	 * 
	 * @return
	 */
	public int getRowCount() {
		return rowCount;
	}
	
	/**
	 * 当前行号
	 * 
	 * @return
	 */
	public int getRowIndex() {
		return rowIndex;
	}

	@Override
	public boolean hasNext() {
		return getRowIndex() < getRowCount();
	}

	@Override
	public Cell[] next() {
		List<Cell> rowValues = new ArrayList<Cell>();
		Row row = sheet.getRow(rowIndex++);
		for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
			org.apache.poi.ss.usermodel.Cell cell = row.getCell(i);
			rowValues.add(readCell(cell));
		}
		return rowValues.toArray(new Cell[rowValues.size()]);
	}
	
	/**
	 * 读单元格
	 * 
	 * @param cell
	 * @return
	 */
	private Cell readCell(org.apache.poi.ss.usermodel.Cell cell) {
		CellType type = cell.getCellTypeEnum();
		if (type == CellType.BOOLEAN) {
			return new Cell(cell.getBooleanCellValue());
		} else if (type == CellType.NUMERIC || type == CellType.FORMULA) {
			if (DateUtil.isCellDateFormatted(cell)) {
				return new Cell(cell.getDateCellValue());
			} else {
				return new Cell(cell.getNumericCellValue());
			}
		} else if (type == CellType.STRING) {
			// TODO 是否需要将空格或换行替换?
			return new Cell(trimToEmpty(cell.getStringCellValue()));
		} else {
			return Cell.NULL;
		}
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * @param str
	 * @return
	 */
	protected String trimToEmpty(String str) {
		if (str == null) {
			return "";
		} else {
			return str.replaceAll("\n\t", "").replaceAll("\n", "").replaceAll("\t", "").replaceAll(" ", "").replaceAll(" ", "").trim();
		}
	}
	
	@Override
	public void close() {
		ExcelReaderFactory.close(workbook);
	}
}
