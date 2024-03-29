package cn.devezhao.commons.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.NPOIFSFileSystem;
import org.apache.poi.ss.usermodel.*;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
		NPOIFSFileSystem fs;
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
		List<String> names = new ArrayList<>();
		for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
			names.add(workbook.getSheetName(i));
		}
		return names.toArray(new String[0]);
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
		List<Cell> rowValues = new ArrayList<>();
		Row row = sheet.getRow(rowIndex++);
		for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
			org.apache.poi.ss.usermodel.Cell cell = row.getCell(i);
			rowValues.add(readCell(cell));
		}
		return rowValues.toArray(new Cell[0]);
	}

	/**
	 * @return
	 */
	public IRow nextRow() {
		Cell[] cells = next();
		if (cells == null) return null;
		return new IRow(cells, rowIndex);
	}
	
	/**
	 * 读单元格
	 * 
	 * @param cell
	 * @return
	 */
	private Cell readCell(org.apache.poi.ss.usermodel.Cell cell) {
		if (cell == null) return Cell.NULL;

		int rowNo = cell.getAddress().getRow();
		int columnNo = cell.getAddress().getColumn();

		CellType type = cell.getCellTypeEnum();
		if (type == CellType.BOOLEAN) {
			return Cell.valueOf(cell.getBooleanCellValue(), rowNo, columnNo);
		} else if (type == CellType.NUMERIC || type == CellType.FORMULA) {
			if (DateUtil.isCellDateFormatted(cell)) {
				return Cell.valueOf(cell.getDateCellValue(), rowNo, columnNo);
			} else {
				return Cell.valueOf(cell.getNumericCellValue(), rowNo, columnNo);
			}
		} else if (type == CellType.STRING) {
			return trimToStringCell(cell.getStringCellValue(), rowNo, columnNo);
		} else {
			return Cell.valueOf(rowNo, columnNo);
		}
	}

	/**
	 * @param cellText
	 * @param rowNo
	 * @param columnNo
	 * @return
	 */
	protected Cell trimToStringCell(String cellText, int rowNo, int columnNo) {
		if (cellText == null) {
			return Cell.valueOf(rowNo, columnNo);
		} else {
			return Cell.valueOf(cellText.trim(), rowNo, columnNo);
		}
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void close() {
		ExcelReaderFactory.close(workbook);
	}
}
