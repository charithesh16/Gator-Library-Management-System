import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

public class ReservationHeap {
    Reservation[] heap;
    int size;
    private static final int MAX_SIZE=20;

    private static final int FRONT = 1;

    public ReservationHeap() {
        this.size = 0;
        heap = new Reservation[this.MAX_SIZE+1];
        heap[0] = new Reservation(-1,Integer.MIN_VALUE,0);
    }

    private int parent(int pos) {
        return pos/2;
    }

    private int leftChild(int pos) {
        return 2*pos;
    }

    private int rightChild(int pos) {
        return 2*pos + 1;
    }

    private boolean isLeaf(int pos) {
        if(pos > (size/2)) {
            return true;
        }
        return false;
    }

    private void swap(int fpos,int spos) {
        Reservation temp;
        temp = heap[fpos];
        heap[fpos] = heap[spos];
        heap[spos] = temp;
    }

    private boolean checkForLessThan(int i,int j) {
        return (heap[i].getPriority() < heap[j].getPriority()) ||
                ( (heap[i].getPriority() == heap[j].getPriority()) &&  heap[i].getTimeOfReservation() < heap[j].getTimeOfReservation());
    }

    private void minHeapify(int pos) {
        if(!isLeaf(pos)) {
            int swapPos = pos;
            if(rightChild(pos) <= size) {
                swapPos = checkForLessThan(leftChild(pos),rightChild(pos)) ? leftChild(pos) : rightChild(pos);
            }else {
                swapPos = leftChild(pos);
            }
            if(leftChild(pos) <= size && rightChild(pos) <=size && (checkForLessThan(leftChild(pos),pos) || checkForLessThan(rightChild(pos),pos))){
                swap(pos,swapPos);
                minHeapify(swapPos);
            }
//            if(heap[pos].getPriority() > heap[leftChild(pos)].getPriority() || heap[pos].getPriority() > heap[rightChild(pos)].getPriority()) {
//
//            }
        }
    }

    public void insert(Reservation reservation) {
        if(size >= MAX_SIZE) {
            return;
        }
        heap[++size] = reservation;
        int current = size;
        while(checkForLessThan(current,parent(current))) {
            swap(current,parent(current));
            current = parent(current);
        }
    }

    public Reservation remove(){
        Reservation popped = heap[FRONT];
        heap[FRONT] = heap[size--];
        heap[size+1] = null;
        minHeapify(FRONT);
        return popped;
    }

    public String getAsString()
    {
        Reservation[] dummy = Arrays.copyOf(heap,heap.length);
//        System.out.println(" Before sort");
//        for(Reservation reservation:dummy) {
//            if(reservation!=null)
//                System.out.println(reservation.toString());
//        }
        Arrays.stream(dummy).filter(Objects::nonNull).collect(Collectors.toList()).sort(Comparator.comparingInt(Reservation::getPriority));
//        System.out.println(" After sort");
        StringBuilder output = new StringBuilder("[");
        for(Reservation reservation:dummy) {
            if(reservation!=null && reservation.getPatronID()!=-1){
                if(output.toString().equals("[")) {
                    output.append(reservation.getPatronID());
                }else {
                    output.append(",").append(reservation.getPatronID());
                }
            }
        }
        output.append("]");
        return output.toString();
//        Arrays.sort(dummy,(a,b) -> {if(a!=null && b!=null)});
//        for (int i = 1; i <= size / 2; i++) {
//
//            // Printing the parent and both childrens
//            System.out.print(
//                    " PARENT : " + heap[i].toString()
//                            + " LEFT CHILD : " + heap[2 * i].toString()
//                            + " RIGHT CHILD :" + heap[2 * i + 1].toString());
//
//            // By here new line is required
//            System.out.println();
//        }
    }


}
