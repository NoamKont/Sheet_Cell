package menu.impl;

import body.Coordinate;
import body.Logic;
import body.impl.CoordinateImpl;
import body.impl.ImplLogic;
import dto.SheetDTO;
import dto.impl.CellDTO;
import expression.api.EffectiveValue;
import expression.impl.Str;
import jaxb.generated.STLSheet;
import menu.Menu;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

public enum MainMenu implements Menu {
    READFILE{
        @Override
        public void invoke(Logic logic) {
            boolean success = false;
            while (!success) {
                display();
                Scanner scanner = new Scanner(System.in);
                String enterdPath = scanner.nextLine();
                if(enterdPath.equals("0")) {
                    break;
                }
                try {
                    logic.creatNewSheet(enterdPath);
                    System.out.println();
                    System.out.println("=====File loaded successfully=====");
                    success = true;
                }catch (FileNotFoundException e) {
                    System.out.println("ERROR file not found! Please enter a valid path to the file you want to load:");
                }catch (ClassCastException e) {
                    System.out.println("ERROR! in the file, one of the cells has a value that does not match the function");
                }catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        void display(){
            System.out.println("To exit to main menu please enter '0' ");
            System.out.println("Please enter the full path to the XML file you want to load: ");
            System.out.println("(Example for Windows: C:\\path\\to\\your\\file.xml)");
        }
    },
    DISPLAYSPREADSHEET{
        @Override
        public void invoke(Logic logic) {
            printSheet(logic.getSheet());
        }
    },
    DISPLAYCELL{
        @Override
        public void invoke(Logic logic) {
            display();
            while (true) {
                Scanner scanner = new Scanner(System.in);
                String enterdCell = scanner.nextLine();
                if (enterdCell.equals("0")) {
                    break;
                }
                try{
                    if(validInputCell(enterdCell.toUpperCase())){
                        printCell(logic.getCell(enterdCell.toUpperCase()), false);
                        break;
                    }
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
                System.out.println("ERROR! Cell ID not as the format(e.g. A4), Please enter a valid cell ID:");
            }
        }

        void display(){
            System.out.println("To exit to main menu please enter '0' ");
            System.out.println("Please enter the cell identifier (e.g., A4):");
        }
    },
    UPDATECELL{
        @Override
        public void invoke(Logic logic) {
            display();
            Scanner scanner = new Scanner(System.in);
            String enterdCell = null;
            boolean success = false;
            while(true) {
                enterdCell = scanner.nextLine();
                enterdCell = enterdCell.toUpperCase();
                if (enterdCell.equals("0")) {
                    break;
                }

                if(!validInputCell(enterdCell)){
                    System.out.println("ERROR! Cell ID not as the format(e.g. A4), Please enter a valid cell ID: ");
                    continue;
                }
                try{
                    printCell(logic.getCell(enterdCell),true);
                    success = true;
                    break;
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("Please enter the cell identifier (e.g., A4):");
            }

            while(success){
                System.out.println("Please enter the new Value:");
                String enterdValue = scanner.nextLine();
                try {
                    logic.updateCell(enterdCell, enterdValue);
                    printCell(logic.getCell(enterdCell),true);
                    break;
                } catch (ClassCastException  e) {
                    System.out.println("ERROR! Please enter Values that match to the function:");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            if(success){
                DISPLAYSPREADSHEET.invoke(logic);
            }
        }

        void display(){
            System.out.println("To exit to main menu please enter '0' ");
            System.out.println("Please enter the cell identifier (e.g. A4):");
        }

    },
    DISPLAYVERSION{
        @Override
        public void invoke(Logic logic) {

            List<Integer> CellsPerVersion = logic.getNumberOfUpdatePerVersion();
            System.out.println("version |  Number of cells updated");
            for (int i = 0; i < CellsPerVersion.size(); i++) {
                String prefix = (i+1) + "        ";
                System.out.println(prefix.substring(0,8) + "|  " + CellsPerVersion.get(i));
            }

            int option = 0;
            while(true){
                try {
                    Scanner scanner = new Scanner(System.in);
                    display();
                    String input = scanner.nextLine();
                    option = Integer.parseInt(input.trim());
                    if (option == 0) {
                        break;
                    }
                    if(option > 0 && option <= CellsPerVersion.size()){
                        SheetDTO sheet = logic.getSheetbyVersion(option - 1);
                        printSheet(sheet);
                        break;

                    }
                    System.out.println("Invalid input, please enter a valid version number:");

                } catch (NumberFormatException e) {
                    System.out.println("ERROR! Please enter a number only");
                }
            }

        }
        private void display(){
            System.out.println();
            System.out.println("To exit to main menu please enter '0' ");
            System.out.println("Please enter the version number to preview:");
        }
    },
    SAVECURRENTFILE{
        @Override
        public void invoke(Logic logic) {
            try {
                System.out.println();
                System.out.println("Please enter name for your file: ");
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                String path = logic.saveToFile(input);
                System.out.println(System.lineSeparator() + "===== File saved successfully =====");
                System.out.println("file named : '" + input + "' created successfully in " + path);
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    },
    LOADFROMSAVEDFILE{
        @Override
        public void invoke(Logic logic) {
            Logic res = null;
            while(true) {
                System.out.println();
                System.out.println("Please enter the full path to the file you want to load: ");
                System.out.println("To exit to main menu please enter '0' ");
                Scanner scanner = new Scanner(System.in);
                String enterdPath = scanner.nextLine();
                if (enterdPath.equals("0")) {
                    break;
                }
                try {
                    logic.loadFromFile(enterdPath);
                    System.out.println();
                    System.out.println("=====File loaded successfully=====");
                    break;
                }catch (FileNotFoundException e) {
                    System.out.println();
                    System.out.println("ERROR file not found! Please enter a valid path to the file you want to load:");
                } catch (Exception e) {
                    System.out.println();
                    System.out.println(e.getMessage());
                }
            }
        }
    };

    public static void printMenu(){
        System.out.println();
        System.out.println("1) Read File from .XML");
        System.out.println("2) Display Spreadsheet");
        System.out.println("3) Display Single Cell");
        System.out.println("4) Update Single Cell");
        System.out.println("5) Display Versions");
        System.out.println("6) Save your Sheet to file");
        System.out.println("7) Upload an existing Sheet");
        System.out.println("8) Exit");
    }

    public static MainMenu parser (int option){
        switch(option){
            case 1:
                return READFILE;
            case 2:
                return DISPLAYSPREADSHEET;
            case 3:
                return DISPLAYCELL;
            case 4:
                return UPDATECELL;
            case 5:
                return DISPLAYVERSION;
            case 6:
                return SAVECURRENTFILE;
            case 7:
                return LOADFROMSAVEDFILE;
            case 8:
                exit(0);

            default:
                throw new IllegalArgumentException("Invalid option was pressed, only 1-8 numbers. Please try again.");
        }
    }

    public void printSheet(SheetDTO currSheet) {
        String whiteSpace = makeWidth(currSheet.getWidth());
        System.out.println();
        System.out.println("Sheet version: " + currSheet.getVersion() + System.lineSeparator() + "Sheet name: " + currSheet.getSheetName());
        System.out.print(makeWidth(howManyDigits(currSheet.getRowCount())) + "  "); // Leading space for row numbers
        for (int i = 0; i < currSheet.getColumnCount(); i++) {
            System.out.print((char) ('A' + i) + whiteSpace);
        }
        System.out.println();

        // Print the rows with numbers and placeholders
        for (int i = 1; i <= currSheet.getRowCount(); i++) {
            String whiteSpaceBeforeRow = "";
            if(i<10){
                System.out.print("0" + i+ whiteSpaceBeforeRow + "|");
            }
            else{
                System.out.print(i+ whiteSpaceBeforeRow + "|"); // Print the row number
            }
            for (int j = 1; j <= currSheet.getColumnCount(); j++) {
                Coordinate currCoord = new CoordinateImpl(i,j);
                EffectiveValue currCell = currSheet.getEfectivevalueCell(currCoord);
                if(currCell != null){
                    int cellWidth = currCell.toString().length();
                    String tempWhiteSpace = makeWidth(currSheet.getWidth() - cellWidth);
                    System.out.print(currCell.toString().substring(0, Math.min(currSheet.getWidth(), currCell.toString().length())) + tempWhiteSpace + "|");
                }
                else {
                    System.out.print(whiteSpace + "|"); // Placeholder for cell content
                }
            }
            System.out.println();
            for(int j = 0; j < currSheet.getThickness() - 1; j++){
                System.out.print("  |");
                for (int K = 0; K < currSheet.getColumnCount(); K++) {
                    System.out.print(whiteSpace + "|");
                }
                System.out.println();
            }

        }
    }

    private int howManyDigits(int number){
        if (number == 0) {
            return 1;
        }
        return (int) Math.log10(Math.abs(number)) + 1;
    }

    private String makeWidth(int width){
        String res = "";
        for (int i = 0; i < width; i++) {
            res += " ";
        }
        return res;
    }

    public static void printCell (CellDTO cell,boolean inUpdate){
        System.out.println();
        System.out.println("Name: " + cell.getId());
        System.out.println("Original value: " + cell.getOriginalValue());
        try{
            System.out.println("Effective value: " + cell.getOriginalEffectiveValue().toString());
        }catch(NullPointerException e){
            System.out.println("Effective value: ");
        }
        if(!inUpdate){
            System.out.println("Last changed version: " + cell.getLastVersionUpdate());
            System.out.println("List of cells that Depend on " + cell.getId() + ": " + cell.getCellsDependsOnHim() );
            System.out.println("List of cells that " + cell.getId() + " depend on: " + cell.getCellsDependsOnThem() );
        }
    }
    public boolean validInputCell(String input){
        if (input.length() >= 2 && input.charAt(0) >= 'A' && input.charAt(0) <= 'Z') {
            String temp = input.substring(1);
            try {
                if(Integer.parseInt(temp) > 0) {
                    return true;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }


}
