package body.Sheets;

import body.Logic;
import body.Sheet;
import body.impl.ImplLogic;
import dto.SheetDTO;
import jakarta.xml.bind.JAXBException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class SheetsManager {

    private final Set<Logic> sheetsSet;

    public SheetsManager() {
        sheetsSet = new HashSet<>();

        Logic newSheet = new ImplLogic();
        newSheet.setOwner("Noam");
        Logic newSheet1 = new ImplLogic();
        newSheet1.setOwner("Moshe");
        try{
            newSheet.creatNewSheet("C:\\Users\\Noam\\Downloads\\Ex2 example\\grades.xml");
            newSheet1.creatNewSheet("C:\\Users\\Noam\\Downloads\\Ex2 example\\insurance.xml");
            sheetsSet.add(newSheet);
            sheetsSet.add(newSheet1);
        }catch (JAXBException | IOException e){
            e.printStackTrace();
        }
    }

    public synchronized void addSheet(String path,String owner) throws JAXBException, IOException {
        Logic newSheet = new ImplLogic();
        newSheet.setOwner(owner);
        newSheet.creatNewSheet(path);
        if(isSheetExists(newSheet.getSheet().getSheetName())){
            throw new IllegalArgumentException("Sheet with this name is already exists! Please choose another name");
        }
        sheetsSet.add(newSheet);
    }

    public synchronized void removeSheet(String sheetName) {
        sheetsSet.stream().findFirst().ifPresent(sheet -> {
            if (sheet.getSheet().getSheetName().equals(sheetName)) {
                sheetsSet.remove(sheet);
            }
        });
    }

    public synchronized Set<Logic> getSheets() {
        return sheetsSet;
    }

    public synchronized Logic getSheet(String sheetName) {
        return sheetsSet.stream().filter(sheet -> sheet.getSheet().getSheetName().equals(sheetName)).findFirst().orElse(null);
    }

    public boolean isSheetExists(String sheetName) {
        return sheetsSet.stream().filter(sheet -> sheet.getSheet().getSheetName().equals(sheetName)).count() > 0;

    }
}
