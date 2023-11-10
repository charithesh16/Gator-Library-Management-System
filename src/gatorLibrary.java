import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;

public class gatorLibrary {

    public static final String INSERT_BOOK = "InsertBook";
    public static final String PRINT_BOOK = "PrintBook";
    public static final String PRINT_BOOKS = "PrintBooks";
    public static final String BORROW_BOOK = "BorrowBook";
    public static final String RETURN_BOOK = "ReturnBook";
    public static final String DELETE_BOOK = "DeleteBook";
    public static final String FIND_CLOSEST_BOOK= "FindClosestBook";
    public static final String COLOR_FLIP_COUNT = "ColorFlipCount";
    public static final String QUIT = "Quit";

    public static GatorLibraryService gatorLibraryService;

    public static void processInsertBook(String arguments) {
        String[] values = arguments.trim().split(",");
        for(int i=0;i<values.length;i++) {
            values[i] = values[i].trim();
        }
        int id = Integer.parseInt(values[0]);
        String bookName = values[1];
        String authorName = values[2];
        boolean isAvailable = values[3].equals("Yes");
        gatorLibraryService.insertBook(id,bookName,authorName,isAvailable);
    }

    public static void processPrintBook(String arguments){
        int bookID = Integer.parseInt(arguments.trim());
        gatorLibraryService.printBook(bookID);
    }

    public static void processPrintBooks(String arguments) {
        int[] range = Arrays.stream(arguments.trim().split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();
        gatorLibraryService.printBooks(range[0],range[1]);
    }

    public static void processBorrowBook(String arguments) {
        int[] values = Arrays.stream(arguments.trim().split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();
        gatorLibraryService.borrowBook(values[0],values[1],values[2]);
    }
    public static void processReturn(String arguments) {
        int[] values = Arrays.stream(arguments.trim().split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();
        gatorLibraryService.returnBook(values[0],values[1]);
    }

    public static void processDelete(String arguments){
        int value = Integer.parseInt(arguments.trim());
        gatorLibraryService.deleteBook(value);
    }
    public static void processColorFlip(){
        gatorLibraryService.printColorFlips();
    }

    public static void processFindClosest(String arguments){
        int value = Integer.parseInt(arguments.trim());
        gatorLibraryService.findClosest(value);
    }



    public static void processCommand(String operation,String arguments) {
        switch(operation) {
            case INSERT_BOOK -> processInsertBook(arguments);
            case PRINT_BOOK -> processPrintBook(arguments);
            case PRINT_BOOKS -> processPrintBooks(arguments);
            case BORROW_BOOK -> processBorrowBook(arguments);
            case RETURN_BOOK -> processReturn(arguments);
            case DELETE_BOOK -> processDelete(arguments);
            case FIND_CLOSEST_BOOK -> processFindClosest(arguments);
        }
    }
    public static void main(String[] args) throws IOException {

        String fileName = args[0];
//        try {
            File inputFile = new File(fileName);
            FileReader fileReader = new FileReader(inputFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String inputLine = bufferedReader.readLine();
            gatorLibraryService = new GatorLibraryService(fileName.replace(".txt",""));
            while(inputLine !=null) {
                String[] command = inputLine.substring(0,inputLine.length()-1).replace("\"","").split("\\(");
                if(command[0].equals("Quit")){
                    gatorLibraryService.quit();
                    break;
                }else if(command[0].equals("ColorFlipCount")) {
                    processColorFlip();
                }else {
                    processCommand(command[0],command[1]);
                }
                inputLine = bufferedReader.readLine();
            }

//        }catch (Exception e) {
//            System.out.println("Exeption occured : "+e);
//        }
    }
}