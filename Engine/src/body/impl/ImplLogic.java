package body.impl;

import body.Cell;
import body.Coordinate;
import body.Logic;
import body.Sheet;
import body.permission.PermissionManager;
import dto.SheetDTO;
import dto.impl.CellDTO;
import dto.impl.ImplSheetDTO;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

//TODO important!!
//import jaxb.generated.STLCell;
//import jaxb.generated.STLSheet;

import jaxb.generatedEx2.STLCell;
import jaxb.generatedEx2.STLRange;
import jaxb.generatedEx2.STLSheet;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import body.Coordinate;


public class ImplLogic implements Logic,Serializable  {
    private String owner;
    private List<Sheet> mainSheet = new ArrayList<>();
    private final PermissionManager permissionManager = new PermissionManager();


    public ImplLogic() { }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String getOwner() {
        return owner;
    }
    public CellDTO getCell(String cellID) {
        Cell temp = mainSheet.get(mainSheet.size() - 1).getCell(cellID);
        return new CellDTO(temp);
    }

    @Override
    public int getRowsNumber() {
        return mainSheet.get(mainSheet.size() - 1).getRowCount();
    }

    @Override
    public int getColumnsNumber() {
        return mainSheet.get(mainSheet.size() - 1).getColumnCount();
    }


    public void updateCell(String cellId, String value){
        Sheet oldVersion = null;
        Sheet currentVersion = mainSheet.get(mainSheet.size() - 1);

        try {
            // Step 1: Serialize the object to a byte array
            ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
            ObjectOutputStream outStream = new ObjectOutputStream(byteOutStream);
            outStream.writeObject(currentVersion);
            outStream.flush();

            // Step 2: Deserialize the byte array into a new object
            ByteArrayInputStream byteInStream = new ByteArrayInputStream(byteOutStream.toByteArray());
            ObjectInputStream inStream = new ObjectInputStream(byteInStream);

            oldVersion = (Sheet) inStream.readObject();

            mainSheet.add(mainSheet.indexOf(currentVersion), oldVersion);
            currentVersion.setUpdateCellCount(0);
            currentVersion.updateCell(cellId, value);


        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void creatNewSheet(String path)throws JAXBException, FileNotFoundException {
        if(!checkPostFix(path)){
            throw new FileNotFoundException("Please enter a '.xml' file only.");
        }
        InputStream inputStream = new FileInputStream(new File(path));
        STLSheet res = creatGeneratedObject(inputStream);

        Sheet newSheet = STLSheet2Sheet(res);
        newSheet.setOwner(owner);
        mainSheet.clear();
        mainSheet.add(newSheet);
    }

//    private Sheet STLSheet2Sheet(STLSheet stlSheet) {
//        String name = stlSheet.getName();
//        int thickness = stlSheet.getSTLLayout().getSTLSize().getRowsHeightUnits();
//        int width = stlSheet.getSTLLayout().getSTLSize().getColumnWidthUnits();
//        int row = stlSheet.getSTLLayout().getRows();
//        int col = stlSheet.getSTLLayout().getColumns();
//        Sheet res = new ImplSheet(name,thickness,width,row,col);
//        List<STLCell> listofSTLCells = stlSheet.getSTLCells().getSTLCell();
//        for (STLCell stlCell : listofSTLCells) {
//            res.setVersion(0);
//            String cellId = stlCell.getColumn() + String.valueOf(stlCell.getRow());
//            Coordinate coordinate = new CoordinateImpl(cellId);
//            res.updateCellDitels(cellId,stlCell.getSTLOriginalValue());
//        }
//        res.updateCellEffectiveValue("A3");
//
//        return res;
//    }
//
//    private STLSheet creatGeneratedObject(InputStream path)throws JAXBException {
//        JAXBContext jc = JAXBContext.newInstance("jaxb.generated");
//        Unmarshaller u = jc.createUnmarshaller();
//        return (STLSheet) u.unmarshal(path);
//    }

    private Sheet STLSheet2Sheet(STLSheet stlSheet) {
        String name = stlSheet.getName();
        int thickness = stlSheet.getSTLLayout().getSTLSize().getRowsHeightUnits();
        int width = stlSheet.getSTLLayout().getSTLSize().getColumnWidthUnits();
        int row = stlSheet.getSTLLayout().getRows();
        int col = stlSheet.getSTLLayout().getColumns();
        Sheet res = new ImplSheet(name,thickness,width,row,col);
        List<STLRange> ranges = stlSheet.getSTLRanges().getSTLRange();

        List<STLCell> listofSTLCells = stlSheet.getSTLCells().getSTLCell();
        String cellId = null;

        for (STLRange range : ranges) {
            String rangeName = range.getName();
            String topLeft = range.getSTLBoundaries().getFrom();
            String bottomRight = range.getSTLBoundaries().getTo();
            try{
                res.addRange(rangeName, topLeft, bottomRight);
            }catch (Exception e){
                String errorMessage = "Can't Upload new Sheet!, " + e.getMessage();
                throw new IllegalArgumentException(errorMessage);
            }
        }

        for (STLCell stlCell : listofSTLCells) {
            res.setVersion(0);
            cellId = stlCell.getColumn() + String.valueOf(stlCell.getRow());
            try{
                res.updateCellDitels(cellId,stlCell.getSTLOriginalValue());
                res.updateListsOfDependencies(new CoordinateImpl(cellId));
            }catch (Exception e){
                String errorMessage = "Can't Upload new Sheet!, " + e.getMessage() + " in cell ID: " + cellId;
                throw new IllegalArgumentException(errorMessage);
            }
        }
        res.updateCellEffectiveValue(cellId);
        return res;
    }

    private STLSheet creatGeneratedObject(InputStream path)throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance("jaxb.generatedEx2");
        Unmarshaller u = jc.createUnmarshaller();
        return (STLSheet) u.unmarshal(path);
    }

    @Override
    public SheetDTO getSheet() {
        try {
            return new ImplSheetDTO(mainSheet.get(mainSheet.size() - 1));
        }catch (Exception e){
            return null;
        }
    }

    public List<Integer> getNumberOfUpdatePerVersion(){
        List<Integer> res = new ArrayList<Integer>();
        for (Sheet sheet : mainSheet) {
            res.add(sheet.getCountUpdateCell());
        }
        return res;
    }

    @Override
    public SheetDTO getSheetbyVersion(int version) {
        return new ImplSheetDTO(mainSheet.get(version - 1));
    }

    private boolean checkPostFix(String fullPath) {
        return fullPath.endsWith(".xml");
    }

    @Override
    public String saveToFile(String name)throws IOException{
        List<Sheet> currentVersion = this.mainSheet;
        // Step 1: Serialize the object to a file
        File file = new File(name);
        FileOutputStream fileOutStream = new FileOutputStream(file);
        ObjectOutputStream outStream = new ObjectOutputStream(fileOutStream);
        outStream.writeObject(currentVersion);
        outStream.flush();
        outStream.close();
        fileOutStream.close();
        return file.getAbsolutePath();
    }

    public List<Sheet> getMainSheet() {
        return mainSheet;
    }

    @Override
    public boolean isCellExist(String cellID) {
        return mainSheet.get(mainSheet.size() - 1).isCellExist(cellID);
    }

    @Override
    public SheetDTO sortSheet(String topLeft, String bottomRight, String... columns) {
        return new ImplSheetDTO(mainSheet.get(mainSheet.size() - 1).sortSheet(topLeft, bottomRight, columns));
    }

    @Override
    public SheetDTO filterSheet(String topLeft, String bottomRight, List<List<String>> value, List<String> columns) {
        return new ImplSheetDTO(mainSheet.get(mainSheet.size() - 1).filterSheet(topLeft, bottomRight,value, columns));
    }

    @Override
    public SheetDTO dynamicAnalysis(String cellId, String value) {
        Sheet analysVersion = null;
        Sheet currentVersion = mainSheet.get(mainSheet.size() - 1);

        try {
            // Step 1: Serialize the object to a byte array
            ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
            ObjectOutputStream outStream = new ObjectOutputStream(byteOutStream);
            outStream.writeObject(currentVersion);
            outStream.flush();

            // Step 2: Deserialize the byte array into a new object
            ByteArrayInputStream byteInStream = new ByteArrayInputStream(byteOutStream.toByteArray());
            ObjectInputStream inStream = new ObjectInputStream(byteInStream);

            analysVersion = (Sheet) inStream.readObject();

            analysVersion.updateCell(cellId, value);
            return new ImplSheetDTO(analysVersion);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Can't update cell " + cellId + " with value " + value);
        }

    }

    @Override
    public Set<Coordinate> addRangeToSheet(String rangeName, String topLeft, String bottomRight) {
        return mainSheet.get(mainSheet.size() - 1).addRange(rangeName, topLeft, bottomRight);
    }

    @Override
    public void loadFromFile(String path) throws IOException,  ClassNotFoundException{

        // Step 2: Deserialize the object from the file
        FileInputStream fileInStream = new FileInputStream(path);
        ObjectInputStream inStream = new ObjectInputStream(fileInStream);
        mainSheet = (List<Sheet>) inStream.readObject();
        inStream.close();
        fileInStream.close();
    }

    @Override
    public void deleteRange(String rangeName) {
        mainSheet.get(mainSheet.size() - 1).deleteRange(rangeName);
    }


}

