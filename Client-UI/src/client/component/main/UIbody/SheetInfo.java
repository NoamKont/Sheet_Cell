package client.component.main.UIbody;

import body.permission.PermissionInfo;
import dto.SheetDTO;

import java.util.List;
import java.util.Objects;

public class SheetInfo {
    private String sheetOwner;
    private String sheetName;
    private String sheetSize;
    private String permission;
    private int rows;
    private int columns;
    private List<PermissionInfo> permissionInfo;


    public SheetInfo(String sheetOwner, String sheetName, int rows, int columns, String permission, List<PermissionInfo> permissionInfo) {
        this.sheetOwner = sheetOwner;
        this.sheetName = sheetName;
        this.rows = rows;
        this.columns = columns;
        this.permission = permission;
        this.sheetSize = getSheetSize();
        this.permissionInfo = permissionInfo;
    }
    public SheetInfo(SheetDTO sheetDTO){
        this.sheetOwner = sheetDTO.getOwner();
        this.sheetName = sheetDTO.getSheetName();
        this.rows = sheetDTO.getRowCount();
        this.columns = sheetDTO.getColumnCount();
        //this.permission = sheetDTO.getPermission();
        this.sheetSize = getSheetSize();
    }
    public SheetInfo(SheetDTO sheetDTO, List<PermissionInfo> permissionInfo, String username){
        this.sheetOwner = sheetDTO.getOwner();
        this.sheetName = sheetDTO.getSheetName();
        this.rows = sheetDTO.getRowCount();
        this.columns = sheetDTO.getColumnCount();
        this.sheetSize = getSheetSize();
        this.permissionInfo = permissionInfo;
        for(PermissionInfo permissionInfo1 : permissionInfo){
            if(permissionInfo1.getUsername().equals(username)){
                this.permission = permissionInfo1.getPermissionType();
            }
        }
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
    public List<PermissionInfo> getPermissionInfo() {
        return permissionInfo;
    }

    public void setPermissionInfo(List<PermissionInfo> permissionInfo) {
        this.permissionInfo = permissionInfo;
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
