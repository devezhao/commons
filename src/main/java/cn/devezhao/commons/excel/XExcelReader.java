package cn.devezhao.commons.excel;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

/**
 * XExcel 读取 .xlsx
 * @author zhaofang123@gmail.com
 * @since 06/02/2017
 */
public class XExcelReader extends ExcelReader {
	
	private OPCPackage pkg;
	private XSSFReader xssfReader;
	private ReadOnlySharedStringsTable sharedStringsTable;
	
	private int sheetIndex = 0;
	
	private XMLStreamReader cellReader;
	
	/**
	 * @param excel
	 */
	public XExcelReader(File excel) {
		super();
		try {
			pkg = OPCPackage.open(excel, PackageAccess.READ);
			xssfReader = new XSSFReader(pkg);
			sharedStringsTable = new ReadOnlySharedStringsTable(pkg);
		} catch (Exception e) {
			close();
			throw new ExcelReaderException(e);
		}
	}
	
	@Override
	public String[] getSheetNames() {
		List<String> names = new ArrayList<String>();
		XSSFReader.SheetIterator iter;
		try {
			iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
			while(iter.hasNext()) {
				InputStream is = iter.next();
			    names.add(iter.getSheetName());
			    ExcelReaderFactory.close(is);
			}
		} catch (Exception e) {
			throw new ExcelReaderException(e);
		}
		return names.toArray(new String[names.size()]);
	}
	
	@Override
	public void sheetAt(int index) {
		if (cellReader != null) {
			ExcelReaderFactory.close(cellReader);
			cellReader = null;
		}
		
		try {
			XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
			int currentIndex = 0;
			while(iter.hasNext()) {
				InputStream is = iter.next();
				if (currentIndex++ == index) {
					cellReader = XMLInputFactory.newInstance().createXMLStreamReader(is);
					while (cellReader.hasNext()) {
			            cellReader.next();
			            if (cellReader.isStartElement()) {
			                if (cellReader.getLocalName().equals("sheetData")) {  // Locate firse cell 
			                	break;
			                }
			            }
			        }
					break;
				} else {
					ExcelReaderFactory.close(is);
				}
			}
		} catch (Exception e) {
			throw new ExcelReaderException(e);
		}
		
		if (cellReader == null) {
			throw new ExcelReaderException("无效 SHEET 位置: " + index);
		}
		sheetIndex = index;
	}
	
	@Override
	public int getRowCount() {
		sheetAt(sheetIndex);
		int rowCount = 0;
		try {
			while (cellReader.hasNext()) {
				cellReader.next();
				if (cellReader.isStartElement()) {
					if (cellReader.getLocalName().equals("row")) {
						rowCount++;
					}
				}
			}
		} catch (XMLStreamException e) {
			throw new ExcelReaderException(e);
		}
		
		sheetAt(sheetIndex);
		return rowCount;
	}
	
	@Override
	public boolean hasNext() {
		if (cellReader == null) {
			sheetAt(sheetIndex);
		}
		
		try {
			return cellReader.hasNext();
		} catch (XMLStreamException e) {
			return false;
		}
	}
	
	@Override
	public Cell[] next() {
		if (cellReader == null) {
			sheetAt(sheetIndex);
		}
		
		try {
			do {
				cellReader.next();
				if (cellReader.isStartElement()) {
					if (cellReader.getLocalName().equals("row")) {
						return readRow();
					}
				}
			} while (cellReader.hasNext());
		} catch (XMLStreamException e) {
			throw new ExcelReaderException(e);
		}
		return null;
	}
	
	/**
	 * 读行
	 * 
	 * @return
	 * @throws XMLStreamException 
	 */
	private Cell[] readRow() throws XMLStreamException {
		List<Cell> cellList = new ArrayList<Cell>();
        while (cellReader.hasNext()) {
            cellReader.next();
            if (cellReader.isStartElement()) {
                if (cellReader.getLocalName().equals("c")) {
                    CellReference cellReference = new CellReference(cellReader.getAttributeValue(null, "r"));
                    while (cellList.size() < cellReference.getCol()) {
                        cellList.add(Cell.NULL);
                    }
                    
                    String cellValue = readCell(cellReader.getAttributeValue(null, "t"));
                    cellList.add(trimToStringCell(cellValue));
                }
            } else if (cellReader.isEndElement()
                    && cellReader.getLocalName().equals("row")) {
                break;
            }
        }
        return cellList.toArray(new Cell[cellList.size()]);
	}
	
	/**
	 * 读单元格
	 * 
	 * @param cellType
	 * @return
	 * @throws XMLStreamException 
	 * @throws NumberFormatException 
	 */
	private String readCell(String cellType) throws NumberFormatException, XMLStreamException {
		while (cellReader.hasNext()) {
			cellReader.next();
			if (cellReader.isStartElement()) {
				if (cellReader.getLocalName().equals("v")) {
					if (cellType != null && cellType.equals("s")) {
						int idx = Integer.parseInt(cellReader.getElementText());
						return new XSSFRichTextString(sharedStringsTable.getEntryAt(idx)).toString();
					} else {
						return cellReader.getElementText();
					}
				}
			} else if (cellReader.isEndElement() && cellReader.getLocalName().equals("c")) {
				break;
			}
		}
		return "";
	}
	
	@Override
	public void close() {
		super.close();
		ExcelReaderFactory.close(cellReader);
		ExcelReaderFactory.close(pkg);
	}
}
