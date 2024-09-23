package client.component.main.UIbody;

public class SheetInfo {
    private String sheetOwner;
    private String sheetName;
    private String sheetSize;
    private String permission;
    private int rows;
    private int columns;


    public SheetInfo(String sheetOwner, String sheetName, int rows, int columns, String permission) {
        this.sheetOwner = sheetOwner;
        this.sheetName = sheetName;
        this.rows = rows;
        this.columns = columns;
        this.permission = permission;
        this.sheetSize = getSheetSize();
    }
    public String getSheetOwner() {
        return sheetOwner;
    }
    public String getSheetName() {
        return sheetName;
    }
    public int getRows() {
        return rows;
    }
    public int getColumns() {
        return columns;
    }
    public String getPermission() {
        return permission;
    }
    public String getSheetSize() {
        return rows + "x" + columns;
    }
}
