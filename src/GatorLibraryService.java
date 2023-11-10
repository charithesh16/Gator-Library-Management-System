import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GatorLibraryService {

    RedBlackTree redBlackTree;

    BufferedWriter bufferedWriter;

    GatorLibraryService(String fileName) throws IOException {
        this.redBlackTree = new RedBlackTree();
        File file = new File(fileName+"_output_file.txt");
        if(file.createNewFile()){
            FileWriter fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);
        }

    }

    public void insertBook(int id,String name,String authorName,boolean isAvailable) {
        Book book = new Book(id,name,authorName,isAvailable);
        redBlackTree.insertNode(book);
    }

    private void writeToFile(String s) {
        try{
            bufferedWriter.write(s);
            bufferedWriter.newLine();
        }catch (Exception e){
            System.out.println("Exception occured while write to the file "+e);
        }

    }

    public void printBook(int id) {
        Node node = redBlackTree.searchNode(id);
        if(node!=null) {
            writeToFile(node.data.toString()+"\n");
        }
//        else {
//         writeToFile("BookID not found in the Library"+"\n");
//        }
    }


    public void printBooks(int from, int to) {
        for(int id = from;id<=to;id++) {
            printBook(id);
        }
    }

    public void borrowBook(int patronID, int bookID, int patronPriority) {
        Node node = redBlackTree.searchNode(bookID);
        if(node!=null) {
            Book book = node.data;
            if(book.isAvailable()) {
                book.setBorrowedBy(patronID);
                book.setAvailable(false);
                writeToFile("Book "+bookID +" Borrowed by Patron "+patronID+"\n");
            }else{
                book.addToWaitlist(patronID,patronPriority);
                writeToFile("Book "+ bookID + " Reserved by Patron "+patronID+"\n");
            }
        }
    }

    public void returnBook(int patronID, int bookID) {
        Node node = redBlackTree.searchNode(bookID);
        if(node!=null) {
            Book book = node.data;
            writeToFile("Book "+ bookID +" Returned by Patron "+patronID+"\n");
            if(book.hasReservations()) {
                int nextPatron = book.getNextReservation();
                book.setBorrowedBy(nextPatron);
                writeToFile("Book "+ bookID +" Allotted to Patron "+ nextPatron +"\n");
            }else {
                book.setBorrowedBy(0);
                book.setAvailable(true);
            }
        }
    }

    public void deleteBook(int id) {
        Node node = redBlackTree.searchNode(id);
        if(node!=null) {
            Book book = node.data;
            if(!book.hasReservations()){
                writeToFile("Book "+book.id+" is no longer available"+"\n");
            }else{
                String patrons = book.getReservationHeap().getAsString();
                writeToFile("Book "+book.id+" is no longer available. Reservations made by Patrons "+patrons.substring(1,patrons.length()-1) +" have been cancelled!"+"\n");
            }
            redBlackTree.deleteNode(id);
        }
    }

    public void printColorFlips() {
        writeToFile("Colour Flip Count: "+redBlackTree.noOfColorFlips + "\n");
    }

    public void quit() throws IOException {
        writeToFile("Program Terminated!!");
        bufferedWriter.flush();
    }

    public void findClosest(int id) {
        Node node = redBlackTree.searchNode(id);
        if(node!=null) {
            writeToFile(node.data.toString()+"\n");
        }else {
            Node[] ceilAndFloor = redBlackTree.findCeilAndFloor(id);
            writeToFile(ceilAndFloor[0].data.toString()+"\n");
            writeToFile(ceilAndFloor[1].data.toString()+"\n");
        }

    }
}