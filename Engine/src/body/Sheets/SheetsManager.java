package body.Sheets;

import body.Logic;
import body.Sheet;
import body.impl.ImplLogic;
import dto.SheetDTO;

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
    }

    public synchronized void addSheet(String path) {
        Logic nsheet = new ImplLogic();
        try{
            nsheet.creatNewSheet(path);
            sheetsSet.add(nsheet);
        }catch (Exception e){
            System.out.println("Error creating new sheet");
            e.printStackTrace();
        }
    }

    public synchronized void removeSheet(String sheetname) {
        sheetsSet.stream().findFirst().ifPresent(sheet -> {
            if (sheet.getSheet().getSheetName().equals(sheetname)) {
                sheetsSet.remove(sheet);
            }
        });
    }

    public synchronized Set<Logic> getSheets() {
        return Collections.unmodifiableSet(sheetsSet);
    }

    public boolean isSheetExists(String sheetName) {
        return sheetsSet.stream().filter(sheet -> sheet.getSheet().getSheetName().equals(sheetName)).count() > 0;

    }
}
