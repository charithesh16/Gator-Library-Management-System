import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Book {
    int id;
    String name;
    String authorName;
    boolean isAvailable;
    int borrowedBy;
    ReservationHeap reservationHeap;

    Book(){}
    Book(int id, String name, String authorName, boolean isAvailable) {
        this.id = id;
        this.name = name;
        this.authorName = authorName;
        this.isAvailable = isAvailable;
        this.reservationHeap = new ReservationHeap();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public int getBorrowedBy() {
        return borrowedBy;
    }

    public void setBorrowedBy(int borrowedBy) {
        this.borrowedBy = borrowedBy;
    }

    public ReservationHeap getReservationHeap() {
        return reservationHeap;
    }

    public void setReservationHeap(ReservationHeap reservationHeap) {
        this.reservationHeap = reservationHeap;
    }

    @Override
    public String toString() {
        return "BookID = "+ id + "\n" +
                "Title = \"" + name +"\""+ "\n" +
                "Author = \"" + authorName +"\""+ "\n" +
                "Availability = \"" + (isAvailable() ? "Yes" : "No") +"\"" + "\n" +
                "BorrowedBy = " + ( borrowedBy==0 ? "None" : borrowedBy) + "\n" +
                "Reservations = " + getReservationHeap().getAsString();
    }

    public void addToWaitlist(int patronID, int patronPriority) {
        Reservation reservation = new Reservation(patronID,patronPriority,System.currentTimeMillis());
        getReservationHeap().insert(reservation);
    }

    public int getNextReservation() {
        return getReservationHeap().remove().getPatronID();
    }

    public boolean hasReservations(){
        return getReservationHeap().size >0;
    }
}
