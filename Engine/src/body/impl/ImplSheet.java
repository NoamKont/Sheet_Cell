package body.impl;

import body.Cell;
import body.Coordinate;
import body.Sheet;
import expression.Range.api.Range;
import expression.Range.impl.RangeImpl;
import expression.api.Expression;
import expression.impl.*;
import expression.impl.Number;
import expression.impl.boolFunction.*;
import expression.impl.numeric.*;
import expression.impl.numeric.Range.Average;
import expression.impl.numeric.Range.Sum;
import expression.impl.string.Concat;
import expression.impl.string.Sub;
import expression.impl.system.REF;

import java.io.*;
import java.util.*;

public class ImplSheet implements Sheet, Serializable  {



    private int sheetVersion = 1;
    final private String sheetName;
    final private int thickness;
    final private int width;
    final private int row;
    final private int col;
    private Map<Coordinate, Cell> activeCells = new HashMap<>();
    private Graph graph;
    private int countUpdateCell = 0;
    private Map<String, Range> rangeMap = new HashMap<>();


    public ImplSheet(String sheetName, int thickness, int width, int row, int col) {
        if (row > 50 || col > 20 || row < 1 || col < 1) {
            throw new IllegalArgumentException("ERROR! Can't load the file. The file has a row or column that is out of range.");
        }
        this.sheetName = sheetName;
        this.thickness = thickness;
        this.width = width;
        this.row = row;
        this.col = col;
        this.graph = new Graph();
    }

    @Override
    public String getSheetName() {
        return sheetName;
    }

    @Override
    public Cell getCell(String cellID) {
        Coordinate coordinate = new CoordinateImpl(cellID);
        checkValidBounds(coordinate);
        if(!activeCells.containsKey(coordinate)){
            Cell emptyCell = new ImplCell(cellID);
            activeCells.putIfAbsent(coordinate, emptyCell);
            return emptyCell;
        }
        else {
            updateListsOfDependencies(coordinate);
            return activeCells.get(coordinate);
        }
    }

    @Override
    public Cell getCell(Coordinate coordinate) {
        if(!activeCells.containsKey(coordinate)){
           return null;
        }
        return activeCells.get(coordinate);
    }

    @Override
    public int getRowCount() {
        return row;
    }

    @Override
    public int getColumnCount() {
        return col;
    }

    @Override
    public int getThickness() {
        return thickness;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getVersion() {
        return sheetVersion;
    }

    @Override
    public void setVersion(int version) {
        this.sheetVersion = version;
    }

    @Override
    public void setUpdateCellCount(int countUpdateCell) {
        this.countUpdateCell = countUpdateCell;
    }

    @Override
    public void addRange(String rangeName, String topLeftCellId, String rightBottomCellId) {
        Range range = new RangeImpl(rangeName, topLeftCellId, rightBottomCellId, this);
        rangeMap.putIfAbsent(rangeName, range);
    }

    @Override
    public void deleteRange(String rangeName) {
        if(checkIfCanRemoveRange(rangeName)){
            rangeMap.remove(rangeName);
        }
    }

    private boolean checkIfCanRemoveRange(String rangeName) {
        String cells = "";
        for (Map.Entry<Coordinate, Cell> entry : activeCells.entrySet()) {
            Cell cell = entry.getValue();
            if(cell.getExpression() instanceof UnaryExpression){
                UnaryExpression unaryExpression = (UnaryExpression) cell.getExpression();
                if(unaryExpression.getExpression() instanceof RangeImpl) {
                    RangeImpl range = (RangeImpl) unaryExpression.getExpression();
                    if (range.getRangeName().equals(rangeName)) {
                        cells = cells + cell.getCoordinate().toString() + " ";
                    }
                }
            }
        }
        if (cells.isEmpty() || cells.isBlank()) {
            return true;
        }
        else{
            throw new IllegalArgumentException("Can't remove the range! cells : " + cells + "depends on it");
        }
    }


    @Override
    public void updateCellDitels(String cellId, String value){
        Coordinate currCoord = new CoordinateImpl(cellId);
        checkValidBounds(currCoord);
        activeCells.putIfAbsent(currCoord, new ImplCell(cellId));
        graph.addVertex(currCoord);
        sheetVersion = sheetVersion + 1;
        graph.removeEntryEdges(currCoord);
        Cell cell = activeCells.get(currCoord);

        //why we need it?
        //cell.setEffectiveValue(null);
        Expression currExpression = stringToExpression(value,currCoord);
        cell.setOriginalValue(currExpression.expressionTOtoString());
        cell.setExpression(currExpression);
        cell.setLastVersionUpdate(sheetVersion);
        countUpdateCell++;
    }

    @Override
    public void updateCellEffectiveValue(String cellId){
        Set<Coordinate> neighbors = graph.listOfAccessibleVertex(new CoordinateImpl(cellId));
        List<Coordinate> topologicalSorted = graph.topologicalSort();

        for(Coordinate coord : topologicalSorted){
            Cell currCell = activeCells.get(coord);
            if(neighbors.contains(coord) && !coord.equals(new CoordinateImpl(cellId))){
                currCell.setLastVersionUpdate(sheetVersion);
                countUpdateCell++;
            }
            String value = currCell.getOriginalValue();
            Expression currExpression = stringToExpression(value,coord);
            currCell.setExpression(currExpression);
            currCell.setEffectiveValue(currCell.getExpression().evaluate());
            updateListsOfDependencies(coord);
        }
    }

    @Override
    public boolean isCellExist(String cellID) {
        return activeCells.containsKey(new CoordinateImpl(cellID));
    }

    @Override
    public Sheet sortSheet(String topLeft, String bottomRight, String... columns) {
        Sheet sortedSheet = null;
        try {
            ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
            ObjectOutputStream outStream = new ObjectOutputStream(byteOutStream);
            outStream.writeObject(this);
            outStream.flush();

            // Step 2: Deserialize the byte array into a new object
            ByteArrayInputStream byteInStream = new ByteArrayInputStream(byteOutStream.toByteArray());
            ObjectInputStream inStream = new ObjectInputStream(byteInStream);

            sortedSheet = (Sheet) inStream.readObject();


            Coordinate topLeftCoord = new CoordinateImpl(topLeft);
            Coordinate bottomRightCoord = new CoordinateImpl(bottomRight);
            checkValidBounds(topLeftCoord);
            checkValidBounds(bottomRightCoord);
            Range range = new RangeImpl("range", topLeft, bottomRight, this);

            for (String column : columns) {
                List<Cell> cellsInColumn = new ArrayList<>();
                range.getCells().stream().filter(cell -> cell.getCoordinate().getColumn() == Integer.parseInt(column)).forEach(cellsInColumn::add);
                cellsInColumn.sort(Comparator.comparingDouble(cell -> (Double) cell.getEffectiveValue().getValue()));

            }

        }catch(IOException | ClassNotFoundException e){
                e.printStackTrace();
            }
        return sortedSheet;
    }
    @Override
    public void updateCell(String cellId, String value) {
        updateCellDitels(cellId, value);
        updateCellEffectiveValue(cellId);
    }

    @Override
    public void updateListsOfDependencies(Coordinate coord) {
            Cell cell = activeCells.get(coord);
            cell.setDependsOnHim(graph.getNeighbors(coord));
            cell.setDependsOnThem(graph.getSources(coord));
    }

    private Expression stringToExpression(String input,Coordinate coordinate) {
        Expression currExpression = helperStringToExpression(input,coordinate);
        if(currExpression instanceof Str ){
            return new Str(((Str) currExpression).getValue().toString().trim());
        }
        return currExpression;
    }

    private Expression helperStringToExpression(String input,Coordinate coordinate) {

        if(input.isEmpty()){
            return new Empty();
        }
        validInputBracket(input);
        if(!(input.trim().charAt(0) == '{' && input.trim().charAt(input.trim().length()-1) == '}')){
            try{
                Double.parseDouble(input);
                return (new Number(input));
            }catch (NumberFormatException error){

                if(input.toUpperCase().equals("TRUE") || input.toUpperCase().equals("FALSE")){
                    return (new Bool(input));
                }
                return (new Str(input));
            }
        }

        else {
            input = input.trim();
            input = input.substring(1, input.length() - 1);
            List<String> result = new ArrayList<>();
            List<Expression> e = new ArrayList<>();
            StringBuilder currentElement = new StringBuilder();
            boolean insideBraces = false;
            int openBracket = 0;
            for (char c : input.toCharArray()) {
                if (c == '{') {
                    insideBraces = true;
                    openBracket++;
                } else if (c == '}') {
                    openBracket--;
                    if(openBracket == 0){
                        insideBraces = false;
                    }
                }
                if (c == ',' && !insideBraces) {
                    if(!currentElement.toString().isEmpty()){
                        result.add(currentElement.toString());
                    }

                    currentElement.setLength(0); // Clear the current element
                } else {
                    currentElement.append(c);
                }
            }
            if(!currentElement.toString().isEmpty()){
                result.add(currentElement.toString()); // Add the last element
            }
            if(!isValidOperator(result.get(0).toUpperCase().trim())){
                throw new NumberFormatException("Invalid Operator" + System.lineSeparator());// + "The valid Operator are: PLUS, MINUS, TIMES, DIVIDE, MOD, POW, CONCAT, ABS, SUB, REF");
            }
            isValidNumOfArgs(result);
            for(int i = 1; i < result.size(); i++) {
                e.add(helperStringToExpression(result.get(i),coordinate));
            }

            return createExpression(result.get(0), e, coordinate);
        }
    }

    private Boolean isValidNumOfArgs(List<String> args){
        Boolean res = true;
        switch (args.get(0).toUpperCase().trim()) {
            case "PLUS":
            case "MINUS":
            case "TIMES":
            case "DIVIDE":
            case "MOD":
            case "POW":
            case "CONCAT":
            case "EQUAL":
            case "BIGGER":
            case "LESS":
            case "OR":
            case "AND":
            case "PERCENT":
                if (args.size() != 3){
                    res = false;
                    throw new NumberFormatException("Error: Incorrect number of arguments. Expected 2 arguments.");
                }
                break;
            case "REF":
                if (args.size() == 2){
                    args.set(0, "REF");
                    args.set(1, args.get(1).toUpperCase());
                }
            case "ABS":
            case "AVERAGE":
            case "NOT":
            case "SUM":
                if (args.size() != 2){
                    res = false;
                    throw new NumberFormatException("Error: Incorrect number of arguments. Expected 1 arguments.");
                }
                break;
            case "SUB":
                if (args.size() != 4){
                    res = false;
                    throw new NumberFormatException("Error: Incorrect number of arguments. Expected 3 arguments.");
                }
                break;
            default:
                res = true;
                break;
        }
        return res;
    }

    private Expression createExpression(String operator, List<Expression> args, Coordinate coordinate) {
        return switch (operator.trim().toUpperCase()) {
            case "PLUS" -> new Plus(args.get(0), args.get(1));
            case "MINUS" -> new Minus(args.get(0), args.get(1));
            case "TIMES" -> new Times(args.get(0), args.get(1));
            case "DIVIDE" -> new Divide(args.get(0), args.get(1));
            case "MOD" -> new Modulo(args.get(0), args.get(1));
            case "POW" -> new Pow(args.get(0), args.get(1));
            case "ABS" -> new AbsoluteValue(args.get(0));
            case "CONCAT" -> new Concat(args.get(0), args.get(1));
            case "SUB" -> new Sub(args.get(0), args.get(1),args.get(2));
            case "REF" -> new REF(args.get(0), activeCells.get(refHelper(args.get(0), coordinate)));
            case "EQUAL" -> new Equal(args.get(0), args.get(1));
            case "BIGGER" -> new Bigger(args.get(0), args.get(1));
            case "LESS" -> new Less(args.get(1), args.get(0));
            case "OR" -> new Or(args.get(0), args.get(1));
            case "AND" -> new And(args.get(0), args.get(1));
            case "NOT" -> new Not(args.get(0));
            case "PERCENT" -> new Percent(args.get(0), args.get(1));
            //case "SUM" -> new Sum(rangeMap.get(args.get(0).expressionTOtoString().trim()));
            case "SUM" -> new Sum(rangeHelper(args.get(0).expressionTOtoString(), coordinate));
            case "AVERAGE" -> new Average(rangeHelper(args.get(0).expressionTOtoString(), coordinate));
            //case "AVERAGE" -> new Average(rangeMap.get(args.get(0).expressionTOtoString().trim()));
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }

    private Expression rangeHelper(String rangeName, Coordinate coordinate){
        rangeName = rangeName.trim();
        if(!rangeMap.containsKey(rangeName)){
            return null;
        }
        else{
            rangeMap.get(rangeName).getCells().
                    forEach(cell -> {graph.addEdge(cell.getCoordinate(), coordinate);
            });
            return rangeMap.get(rangeName);
        }
    }

    private Coordinate refHelper(Expression input, Coordinate toCoordinate){
        String cellID = (String) input.evaluate().getValue();
        cellID = cellID.trim();
        if(validInputCell(cellID.toUpperCase(), toCoordinate)){
            Coordinate coordinate = new CoordinateImpl(cellID);
            return coordinate;
        }
        return null;
    }

    @Override
    public int getCountUpdateCell() {
        return countUpdateCell;
    }

    @Override
    public Map<Coordinate, Cell> getActiveCells() {
        return activeCells;
    }

    @Override
    public Map<String, Range> getAllRanges() {
        return rangeMap;
    }

    @Override
    public Set<Cell> getRangeCells(String rangeName) {
        return rangeMap.get(rangeName).getCells();
    }

    private void checkValidBounds(Coordinate coordinate) {
        if(coordinate.getRow() > row || coordinate.getColumn() > col){
            throw new IllegalArgumentException("Cell: " + coordinate.toString() + " is out of bounds");
        }
    }

    private void validInputBracket(String input){
        if(input.charAt(0) == '{') {
            if(!isValidBracket(input)){
                throw new NumberFormatException("Invalid expression Bracket");
            }
        }
    }

    private boolean isValidBracket(String s) {

        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            if (c == '{') {
                stack.push(c);
            } else if (c == '}') {
                if (stack.isEmpty()) {
                    return false;
                }
                stack.pop();
            }
        }
        return stack.isEmpty();
    }

    private boolean validInputCell(String input, Coordinate toCoordinate){
        if (input.length() >= 2 && input.charAt(0) >= 'A' && input.charAt(0) <= 'Z') {

            try {
                Coordinate coordinate = new CoordinateImpl(input.toUpperCase());

                //check if the cell is valid
                checkValidBounds(coordinate);

                //check if we want to point on not existing cell
                if(!activeCells.containsKey(coordinate)){

                    //creating empty cell and add to map
                    Cell newCell = new ImplCell(input);
                    newCell.setExpression(new Empty());
                    newCell.setEffectiveValue(new Empty());
                    activeCells.putIfAbsent(coordinate, newCell);
                }

                //check if adding the ref will create a circle
                graph.addEdge(coordinate, toCoordinate);
                if(graph.hasCycle()){
                    graph.removeEdge(coordinate, toCoordinate);
                    throw new IllegalArgumentException("Error! the cell: " + input + " create a circle");
                }


            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid input, please enter a valid cell identifier (e.g., A4):");
            }
        }
        else{
            throw new IllegalArgumentException("Invalid input, please enter a valid cell identifier (e.g., A4):");
        }
        return true;
    }

    private boolean isValidOperator(String operator){
        return switch (operator) {
            case "PLUS", "MINUS", "TIMES", "DIVIDE", "MOD", "POW", "CONCAT", "ABS", "SUB", "REF","EQUAL","BIGGER","NOT","OR","AND","LESS","IF","PERCENT","SUM","AVERAGE" -> true;
            default -> false;
        };
    }

}
