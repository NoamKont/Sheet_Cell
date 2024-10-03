package body.Sheets;

import body.permission.PermissionInfo;
import dto.SheetDTO;

import java.util.List;
import java.util.Objects;

import static body.permission.PermissionInfo.Permissions.*;

public class SheetInfo {
    private String sheetOwner;
    private String sheetName;
    private String sheetSize;
    private PermissionInfo.Permissions userPermission;
    private PermissionInfo.Status userStatus;
    private int rows;
    private int columns;
    private List<PermissionInfo> AllUsersPermissionInfo;


    public SheetInfo(String sheetOwner, String sheetName, int rows, int columns, PermissionInfo.Permissions permission, List<PermissionInfo> AllUsersPermissionInfo) {
        this.sheetOwner = sheetOwner;
        this.sheetName = sheetName;
        this.rows = rows;
        this.columns = columns;
        this.userPermission = permission;
        this.sheetSize = getSheetSize();
        this.AllUsersPermissionInfo = AllUsersPermissionInfo;
    }
    public SheetInfo(SheetDTO sheetDTO){
        this.sheetOwner = sheetDTO.getOwner();
        this.sheetName = sheetDTO.getSheetName();
        this.rows = sheetDTO.getRowCount();
        this.columns = sheetDTO.getColumnCount();
        //this.permission = sheetDTO.getPermission();
        this.sheetSize = getSheetSize();
    }
    public SheetInfo(SheetDTO sheetDTO, List<PermissionInfo> AllUsersPermissionInfo, String username){
        this.sheetOwner = sheetDTO.getOwner();
        this.sheetName = sheetDTO.getSheetName();
        this.rows = sheetDTO.getRowCount();
        this.columns = sheetDTO.getColumnCount();
        this.sheetSize = getSheetSize();
        this.AllUsersPermissionInfo = AllUsersPermissionInfo;
        setUserPermission(AllUsersPermissionInfo, username);
    }

    private void setUserPermission(List<PermissionInfo> AllUsersPermissionInfo, String username) {
        for(PermissionInfo permissionInfo : AllUsersPermissionInfo){
            if(permissionInfo.getUsername().equals(username)){
                this.userPermission = permissionInfo.getPermissionType();
                this.userStatus = permissionInfo.getPermissionStatus();
            }
        }
        if(this.userPermission == null){
            this.userPermission = NO_PERMISSION;
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
    public String getUserPermission() {
        return userPermission.toString();
    }
    public String getUserStatus() {
        return userStatus.toString();
    }
    public String getSheetSize() {
        return rows + "x" + columns;
    }

    public List<PermissionInfo> getAllUsersPermissionInfo() {
        return AllUsersPermissionInfo;
    }

    public void setAllUsersPermissionInfo(List<PermissionInfo> allUsersPermissionInfo) {
        this.AllUsersPermissionInfo = allUsersPermissionInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SheetInfo that = (SheetInfo) o;
        return Objects.equals(sheetName, that.sheetName) &&
                Objects.equals(sheetOwner, that.sheetOwner)&&
                Objects.equals(sheetSize, that.sheetSize)&&
                Objects.equals(userPermission, that.userPermission)&&
                Objects.equals(AllUsersPermissionInfo, that.AllUsersPermissionInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sheetName, sheetOwner);
    }
}
