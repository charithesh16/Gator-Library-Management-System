public class Reservation {
    int patronID;
    int priority;
    long timeOfReservation;

    public Reservation(int patronID, int priority, long timeOfReservation) {
        this.patronID = patronID;
        this.priority = priority;
        this.timeOfReservation = timeOfReservation;
    }

    public int getPatronID() {
        return patronID;
    }

    public void setPatronID(int patronID) {
        this.patronID = patronID;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public long getTimeOfReservation() {
        return timeOfReservation;
    }

    public void setTimeOfReservation(long timeOfReservation) {
        this.timeOfReservation = timeOfReservation;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "patronID=" + patronID +
                ", priority=" + priority +
                ", timeOfReservation=" + timeOfReservation +
                '}';
    }
}
