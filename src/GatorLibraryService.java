import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GatorLibraryService {

    RedBlackTree redBlackTree;

    BufferedWriter bufferedWriter;

    /* Initialize the RedBlack tree object and create a file to write the output */
    GatorLibraryService(String fileName) throws IOException {
        this.redBlackTree = new RedBlackTree();
        File file = new File(fileName+"_output_file.txt");
        FileWriter fileWriter = new FileWriter(file,false);
        bufferedWriter = new BufferedWriter(fileWriter);
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
    /* Get the node from tree and print its data */
    public void printBook(int id) {
        Node node = redBlackTree.searchNode(id);
        if(node!=null) {
            writeToFile(node.data.toString()+"\n");
        }
        else {
         writeToFile("Book "+id +" not found in the Library"+"\n");
        }
    }


    public void printBooks(int from, int to) {
        List<Node> books = redBlackTree.printBooks(from,to);
        for(Node node: books){
            writeToFile(node.data.toString()+"\n");
        }
    }
//    public void printAllBooks(){
//        writeToFile("================================== ALL BOOKs ++++======");
//        for(int i=0;i<1000;i++)
//        {
//        Node node = redBlackTree.searchNode(i);
//        if(node!=null) {
//            writeToFile(node.toString()+"\n");
//            }
//        }
//        writeToFile("================================== END ++++======");
//    }

    /* search the node if it is available check if it is already borrowed
    *   If it is already borrowed add the patron to reservation heap
    *   else assign to the book to the patron and set availability as false.
    * */
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

    /* Search the node if it is present
    *       check if it has reservations
    *          if yes then assign the book to the next patron from the heap
    *          else set borrowed to 0 and availability as true
    *
    *  */
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

    /* If the book is present delete the book */
    public void deleteBook(int id) {
        Node node = redBlackTree.searchNode(id);
        if(node!=null) {
            Book book = node.data;
            redBlackTree.deleteNode(id);
            if(!book.hasReservations()){
                writeToFile("Book "+book.id+" is no longer available"+"\n");
            }else{
                String patrons = book.getReservationHeap().getAsString();
                writeToFile("Book "+book.id+" is no longer available. Reservations made by Patrons "+patrons.substring(1,patrons.length()-1) +" have been cancelled!"+"\n");
            }
        }
    }

    /* Print Color Flips */
    public void printColorFlips() {
        writeToFile("Color Flip Count: "+redBlackTree.noOfColorFlips + "\n");
    }

    /* Write the output to a file */
    public void flushOutput() throws IOException{
        bufferedWriter.flush();
    }
    public void quit() throws IOException {
        writeToFile("Program Terminated!!");
    }

    /* Search for the book with the given id
    *       If present print
    *       else
    *           find ceil and floor of the given id
    *           if the difference between ids of ceil and floor to the given key is same print both books
    *           if ceil is closer, then print ceil
    *           if floor is closer, then print floor
    *
    *
    *  */
    public void findClosest(int id) {
        Node node = redBlackTree.searchNode(id);
        if(node!=null) {
            writeToFile(node.data.toString()+"\n");
        }else {
            Node[] ceilAndFloor = redBlackTree.findCeilAndFloor(id);
            int diff1 = Math.abs(ceilAndFloor[0].data.getId() - id);
            int diff2 = Math.abs(ceilAndFloor[1].data.getId() - id);
            if(diff1==diff2) {
                writeToFile(ceilAndFloor[0].data.toString()+"\n");
                writeToFile(ceilAndFloor[1].data.toString()+"\n");
            }else if(diff1 < diff2){
                writeToFile(ceilAndFloor[0].data.toString()+"\n");
            }else{
                writeToFile(ceilAndFloor[1].data.toString()+"\n");
            }

        }

    }
}
