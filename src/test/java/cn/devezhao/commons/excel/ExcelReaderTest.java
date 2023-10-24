package cn.devezhao.commons.excel;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * 
 * @author zhaofang123@gmail.com
 * @since 06/02/2017
 */
public class ExcelReaderTest {

	@Test
	public void testXLSX() throws Exception {
		URL fileUrl = ExcelReaderTest.class.getClassLoader().getResource("test.xlsx");
		
		ExcelReader excelReader = ExcelReaderFactory.create(new File(fileUrl.toURI()));
		String[] sNames = excelReader.getSheetNames();
		System.out.println("SHEET 列表 : " + StringUtils.join(sNames, " | "));

		int sheetIndex = 0;
		excelReader.sheetAt(sheetIndex);
		System.out.println("SHEET-" + sheetIndex + " 行总数 : " + excelReader.getRowCount());
		System.out.println();

		while (excelReader.hasNext()) {
			Cell[] row = excelReader.next();
			if (row == null) {
				break;
			}
			System.out.println("DATE : " + row[3].asDate());
			System.out.println("DATETIME : " + row[4].asDate());
			System.out.println("#" + row[0].getRowNo() + " >> " + StringUtils.join(row, " | "));
		}
	}

	@Test
	public void testIRow() {
		ExcelReader excelReader = ExcelReaderFactory.create(
				new File("D:\\GitHub\\for-production\\rebuild-market\\202211东航\\01出口\\出口台账_汪超_新模板-1024.xlsx"));

		while (excelReader.hasNext()) {
			IRow row = excelReader.nextRow();
			if (row == null) break;

			System.out.println(row.getCell("N"));
			System.out.println(row.getCell("R"));
		}
	}

	@Test
	public void  test() {
		System.out.println(new IRow(new Cell[] { Cell.NULL, Cell.NULL }, 0).getCell(0));
		System.out.println(new IRow(new Cell[] { Cell.NULL, Cell.NULL }, 0).getCell(1));
		System.out.println(new IRow(new Cell[] { Cell.NULL, Cell.NULL }, 0).getCell(2));
		System.out.println(new IRow(new Cell[] { Cell.NULL, Cell.NULL }, 0).getCell(21));
	}
}
