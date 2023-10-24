package cn.devezhao.commons.excel;

/**
 * 行
 *
 * @author zhaofang123@gmail.com
 * @since 06/02/2017
 */
public class IRow {

    final private Cell[] cells;
    final private int rowNo;

    public IRow(Cell[] cells, int rowNo) {
        this.cells = cells;
        this.rowNo = rowNo;
    }

    /**
     * @return
     */
    public int getRowNo() {
        return rowNo;
    }

    /**
     * @return
     */
    public Cell[] getCells() {
        return cells;
    }

    /**
     * @param columnNo 0,1,2...
     * @return
     */
    public Cell getCell(int columnNo) {
        return getCells()[columnNo];
    }

    /**
     * @param columnName A,B,C,AA,AZ...
     * @return
     */
    public Cell getCell(String columnName) {
        int no;
        int columnNo = -1;
        columnName = columnName.toUpperCase();
        int length = columnName.length();
        for(int i = 0; i < length; i++) {
            char ch = columnName.charAt(length - i - 1);
            no = ch - 'A' + 1;
            no *= (int) Math.pow(26, i);
            columnNo += no;
        }

        return getCell(columnNo);
    }
}
