package client.component.main.UIbody;

import dto.SheetDTO;

import java.util.Objects;

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
    public SheetInfo(SheetDTO sheetDTO) {
        this.sheetOwner = sheetDTO.getOwner();
        this.sheetName = sheetDTO.getSheetName();
        this.rows = sheetDTO.getRowCount();
        this.columns = sheetDTO.getColumnCount();
        //this.permission = sheetDTO.getPermission();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SheetInfo that = (SheetInfo) o;
        return Objects.equals(sheetName, that.sheetName) &&
                Objects.equals(sheetOwner, that.sheetOwner)&&
                Objects.equals(sheetSize, that.sheetSize)&&
                Objects.equals(permission, that.permission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sheetName, sheetOwner);
    }
}
