package cn.devezhao.commons.excel;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.net.URL;

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
		Assert.assertTrue(sNames.length > 0);
		System.out.println("SHEET 列表 : " + StringUtils.join(sNames, " | "));

		int sheetIndex = 0;
		excelReader.sheetAt(sheetIndex);
		Assert.assertTrue(excelReader.getRowCount() > 0);
		System.out.println("SHEET-" + sheetIndex + " 行总数 : " + excelReader.getRowCount());
		System.out.println();

		int rows = 0;
		while (excelReader.hasNext()) {
			Cell[] row = excelReader.next();
			if (row == null) {
				break;
			}
			rows++;
			if (row.length > 4) {
				System.out.println("DATE : " + row[3].asDate());
				System.out.println("DATETIME : " + row[4].asDate());
			}
			if (row.length > 0) {
				System.out.println("#" + row[0].getRowNo() + " >> " + StringUtils.join(row, " | "));
			}
		}
		Assert.assertTrue(rows > 0);
	}

	@Ignore("依赖本地指定路径的 Excel 文件（作者工作机路径），不可移植")
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
	public void test() {
		IRow row = new IRow(new Cell[] { Cell.NULL, Cell.NULL }, 0);
		Assert.assertSame(Cell.NULL, row.getCell(0));
		Assert.assertSame(Cell.NULL, row.getCell(1));
		Assert.assertNull(row.getCell(2));
		Assert.assertNull(row.getCell(21));
		Assert.assertSame(Cell.NULL, row.getCell("A"));
		Assert.assertNull(row.getCell("C"));
	}
}
