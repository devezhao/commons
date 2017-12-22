package cn.devezhao.commons.excel;

import java.io.File;
import java.io.IOException;
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
 * 
 * @author zhaofang123@gmail.com
 * @since 06/02/2017
 */
public class XExcelReader extends ExcelReader {
	
	private OPCPackage pkg;
	private XSSFReader xssfReader;
	private ReadOnlySharedStringsTable sharedStringsTable;
	
	private XMLStreamReader xmlReader;
	
	private int sheetIndex = 0;
	
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
			throw new ExcelReaderException(e);
		}
		sheetAt(0);
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
		if (xmlReader != null) {
			ExcelReaderFactory.close(xmlReader);
			xmlReader = null;
		}
		
		int currentIndex = 0;
		try {
			XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
			while(iter.hasNext()) {
				InputStream is = iter.next();
				if (currentIndex++ == index) {
					xmlReader = XMLInputFactory.newInstance().createXMLStreamReader(is);
					while (xmlReader.hasNext()) {
			            xmlReader.next();
			            if (xmlReader.isStartElement()) {
			                if (xmlReader.getLocalName().equals("sheetData")) {
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
		
		if (xmlReader == null) {
			throw new ExcelReaderException("无效 SHEET 位置: " + index);
		}
		sheetIndex = index;
	}
	
	@Override
	public int getRowCount() {
		sheetAt(sheetIndex);
		int rowCount = 0;
		try {
			while (xmlReader.hasNext()) {
				xmlReader.next();
				if (xmlReader.isStartElement()) {
					if (xmlReader.getLocalName().equals("row")) {
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
		try {
			return xmlReader.hasNext();
		} catch (XMLStreamException e) {
			return false;
		}
	}
	
	@Override
	public Cell[] next() {
		try {
			do {
				xmlReader.next();
				if (xmlReader.isStartElement()) {
					if (xmlReader.getLocalName().equals("row")) {
						return readRow();
					}
				}
			} while (xmlReader.hasNext());
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
		List<Cell> rowValues = new ArrayList<Cell>();
        while (xmlReader.hasNext()) {
            xmlReader.next();
            if (xmlReader.isStartElement()) {
                if (xmlReader.getLocalName().equals("c")) {
                    CellReference cellReference = new CellReference(xmlReader.getAttributeValue(null, "r"));
                    while (rowValues.size() < cellReference.getCol()) {
                        rowValues.add(Cell.NULL);
                    }
                    String cellType = xmlReader.getAttributeValue(null, "t");
                    String cellValue = readCell(cellType);
                    // cellValue 有大量空格???
                    rowValues.add(new Cell(trimToEmpty(cellValue)));
                }
            } else if (xmlReader.isEndElement()
                    && xmlReader.getLocalName().equals("row")) {
                break;
            }
        }
        return rowValues.toArray(new Cell[rowValues.size()]);
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
		while (xmlReader.hasNext()) {
			xmlReader.next();
			if (xmlReader.isStartElement()) {
				if (xmlReader.getLocalName().equals("v")) {
					if (cellType != null && cellType.equals("s")) {
						int idx = Integer.parseInt(xmlReader.getElementText());
						return new XSSFRichTextString(sharedStringsTable.getEntryAt(idx)).toString();
					} else {
						return xmlReader.getElementText();
					}
				}
			} else if (xmlReader.isEndElement() && xmlReader.getLocalName().equals("c")) {
				break;
			}
		}
		return "";
	}
	
	@Override
	public void close() throws IOException {
		super.close();
		if (xmlReader != null) {
			ExcelReaderFactory.close(xmlReader);
		}
		pkg.close();
	}
}
