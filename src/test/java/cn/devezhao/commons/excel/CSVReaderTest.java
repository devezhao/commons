package cn.devezhao.commons.excel;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class CSVReaderTest {

	@Test
	public void read() throws Exception {
		URL fileUrl = CSVReaderTest.class.getClassLoader().getResource("test.csv");

		CSVReader reader = new CSVReader(new File(fileUrl.toURI()));
		Assert.assertTrue(reader.hasNext());

		Cell[] row0 = reader.next();
		Assert.assertEquals(5, row0.length);
		Assert.assertEquals("入职名称", row0[0].asString());
		Assert.assertEquals("创建人", row0[1].asString());
		Assert.assertEquals("状态", row0[4].asString());

		Cell[] row1 = reader.next();
		Assert.assertEquals(3, row1.length);
		Assert.assertEquals("李四", row1[0].asString());
		Assert.assertEquals("超级管理员", row1[1].asString());
		Assert.assertEquals("2022/1/11 14:12", row1[2].asString());
		Assert.assertEquals(1, row1[0].getRowNo());

		Cell[] row2 = reader.next();
		Assert.assertEquals(5, row2.length);
		Assert.assertEquals("911-017e47e4d99a002e", row2[0].asString());
		Assert.assertEquals("离职", row2[4].asString());

		Cell[] row3 = reader.next();
		Assert.assertEquals(3, row3.length);
		Assert.assertEquals("123", row3[0].asString());

		Assert.assertFalse(reader.hasNext());
		Assert.assertNull(reader.next());
		Assert.assertEquals(4, reader.getRowCount());
	}

	@Test
	public void readQuotedFields() throws Exception {
		File csv = File.createTempFile("commons-csv-test", ".csv");
		try {
			Files.write(csv.toPath(),
					"a,\"b,c\",\"\",\"say \"\"hi\"\"\"".getBytes(StandardCharsets.UTF_8));
			CSVReader reader = new CSVReader(csv);
			Cell[] row = reader.next();
			Assert.assertEquals(4, row.length);
			Assert.assertEquals("a", row[0].asString());
			Assert.assertEquals("b,c", row[1].asString());
			Assert.assertNull(row[2].asString());
			Assert.assertEquals("say \"hi\"", row[3].asString());
		} finally {
			csv.delete();
		}
	}
}
