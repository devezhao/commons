package cn.devezhao.commons.excel;

import javax.xml.stream.XMLStreamReader;
import java.io.Closeable;
import java.io.File;

/**
 * 
 * @author zhaofang123@gmail.com
 * @since 06/02/2017
 */
public class ExcelReaderFactory {

	/**
	 * @param excel
	 * @return
	 * @throws ExcelReaderException
	 */
	public static ExcelReader create(File excel) throws ExcelReaderException {
		return create(excel, "utf-8");
	}
	
	/**
	 * @param excel
	 * @param encoding
	 * @return
	 * @throws ExcelReaderException
	 */
	public static ExcelReader create(File excel, String encoding) throws ExcelReaderException {
		if (excel != null && excel.getName().endsWith(".xlsx")) {
			return new XExcelReader(excel);
		} else if (excel != null && excel.getName().endsWith(".xls")) {
			return new ExcelReader(excel);
		} else if (excel != null && excel.getName().endsWith(".csv")) {
			return new CSVReader(excel, encoding);
		} else {
			throw new ExcelReaderException("无效 Excel/CSV 文件: " + excel);
		}
	}
	
	/**
	 * @param closeable
	 */
	public static void close(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (Exception ignore) { }
	}
	
	/**
	 * @param reader
	 */
	protected static void close(XMLStreamReader reader) {
		try {
			if (reader != null) {
				reader.close();
			}
		} catch (Exception ignore) { }
	}
}
