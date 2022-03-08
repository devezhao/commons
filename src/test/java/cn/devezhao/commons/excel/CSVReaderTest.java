package cn.devezhao.commons.excel;

import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.Arrays;

public class CSVReaderTest {

    @Test
    public void read() throws Exception {
        URL fileUrl = CSVReaderTest.class.getClassLoader().getResource("test.csv");

        CSVReader reader = new CSVReader(new File(fileUrl.toURI()));
        Cell[] row;
        while ((row = reader.next()) != null) {
            System.out.println(Arrays.toString(row));
            for (Cell c : row) {
                System.out.println(c.getRowNo() + "," + c.getColumnNo() + "=" + c);
            }
        }
    }
}