import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;

// Class representing the GatorLibrary
public class gatorLibrary {

    // Constants representing different operations
    public static final String INSERT_BOOK = "InsertBook";
    public static final String PRINT_BOOK = "PrintBook";
    public static final String PRINT_BOOKS = "PrintBooks";
    public static final String BORROW_BOOK = "BorrowBook";
    public static final String RETURN_BOOK = "ReturnBook";
    public static final String DELETE_BOOK = "DeleteBook";
    public static final String FIND_CLOSEST_BOOK = "FindClosestBook";

    // Service instance for GatorLibrary
    public static GatorLibraryService gatorLibraryService;

    // Method to process the InsertBook operation
    public static void processInsertBook(String arguments) {
        // Splitting and trimming input arguments
        String[] values = arguments.trim().split(",");
        for (int i = 0; i < values.length; i++) {
            values[i] = values[i].trim();
        }
        // Parsing values
        int id = Integer.parseInt(values[0]);
        String bookName = values[1];
        String authorName = values[2];
        boolean isAvailable = values[3].equals("Yes");
        // Calling the corresponding service method
        gatorLibraryService.insertBook(id, bookName, authorName, isAvailable);
    }

    // Method to process the PrintBook operation
    public static void processPrintBook(String arguments) {
        // Parsing book ID
        int bookID = Integer.parseInt(arguments.trim());
        // Calling the corresponding service method
        gatorLibraryService.printBook(bookID);
    }

    // Method to process the PrintBooks operation
    public static void processPrintBooks(String arguments) {
        // Parsing range values
        int[] range = Arrays.stream(arguments.trim().split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();
        // Calling the corresponding service method
        gatorLibraryService.printBooks(range[0], range[1]);
    }

    // Method to process the BorrowBook operation
    public static void processBorrowBook(String arguments) {
        // Parsing values
        int[] values = Arrays.stream(arguments.trim().split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();
        // Calling the corresponding service method
        gatorLibraryService.borrowBook(values[0], values[1], values[2]);
    }

    // Method to process the ReturnBook operation
    public static void processReturn(String arguments) {
        // Parsing values
        int[] values = Arrays.stream(arguments.trim().split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();
        // Calling the corresponding service method
        gatorLibraryService.returnBook(values[0], values[1]);
    }

    // Method to process the DeleteBook operation
    public static void processDelete(String arguments) {
        // Parsing value
        int value = Integer.parseInt(arguments.trim());
        // Calling the corresponding service method
        gatorLibraryService.deleteBook(value);
    }

    // Method to process the ColorFlip operation
    public static void processColorFlip() {
        // Calling the corresponding service method
        gatorLibraryService.printColorFlips();
    }

    // Method to process the FindClosestBook operation
    public static void processFindClosest(String arguments) {
        // Parsing value
        int value = Integer.parseInt(arguments.trim());
        // Calling the corresponding service method
        gatorLibraryService.findClosest(value);
    }

    // Method to redirect to the method for the corresponding operation
    public static void processCommand(String operation, String arguments) {

        switch (operation) {
            case INSERT_BOOK:
                processInsertBook(arguments);
                break;
            case PRINT_BOOK:
                processPrintBook(arguments);
                break;
            case PRINT_BOOKS:
                processPrintBooks(arguments);
                break;
            case BORROW_BOOK:
                processBorrowBook(arguments);
                break;
            case RETURN_BOOK:
                processReturn(arguments);
                break;
            case DELETE_BOOK:
                processDelete(arguments);
                break;
            case FIND_CLOSEST_BOOK:
                processFindClosest(arguments);
                break;
        }
    }

    // Main method
    public static void main(String[] args) {

        // Reading the file name from command line arguments
        String fileName = args[0];
        try {
            // Creating file reader and buffer reader
            File inputFile = new File(fileName);
            FileReader fileReader = new FileReader(inputFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Reading the first line
            String inputLine = bufferedReader.readLine();

            // Creating a GatorLibraryService instance
            gatorLibraryService = new GatorLibraryService(fileName.replace(".txt", ""));

            // Reading and processing commands line by line
            while (inputLine != null) {
                // Splitting the command into operation and arguments
                String[] command = inputLine.substring(0, inputLine.length() - 1).replace("\"", "").split("\\(");
                // Checking for Quit command
                if (command[0].equals("Quit")) {
                    gatorLibraryService.quit();
                    break;
                }
                // Checking for ColorFlipCount command
                else if (command[0].equals("ColorFlipCount")) {
                    processColorFlip();
                } else {
                    // Processing the command
                    processCommand(command[0], command[1]);
                }
                // Reading the next line
                inputLine = bufferedReader.readLine();
            }

            // Flushing all the written output to a file
            gatorLibraryService.flushOutput();

        } catch (Exception e) {
            // Handling exceptions
            System.out.println("Exception occurred: " + e);
        }
    }
}
