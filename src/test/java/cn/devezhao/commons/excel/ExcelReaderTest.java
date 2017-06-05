package cn.devezhao.commons.excel;

import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

/**
 * 
 * @author zhaofang123@gmail.com
 * @since 06/02/2017
 */
public class ExcelReaderTest {

	@Test
	public void testXLSX() {
		String excel = "d:/acc.xlsx";

		ExcelReader excelReader = ExcelReaderFactory.create(new File(excel));
		String[] sNames = excelReader.getSheetNames();
		System.out.println("SHEET 列表 : " + StringUtils.join(sNames, " | "));

		int sheetIndex = 2;
		excelReader.sheetAt(sheetIndex);
		System.out.println("SHEET-" + sheetIndex + " 行总数 : " + excelReader.getRowCount());
		System.out.println();

		int index = 1;
		while (excelReader.hasNext()) {
			Cell[] row = excelReader.next();
			if (row == null) {
				break;
			}
			System.out.println("#" + index++ + " >> " + StringUtils.join(row, " | "));
		}
	}
}
