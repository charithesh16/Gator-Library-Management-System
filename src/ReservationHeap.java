import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

// Class for implementing a min-heap data structure for reservations
public class ReservationHeap {

    // Array to represent the heap
    Reservation[] heap;

    // Current number of elements in the heap
    int size;

    // Maximum size of the heap
    private static final int MAX_SIZE = 20;

    // Index of the root element in the array
    private static final int FRONT = 1;

    // Constructor for ReservationHeap
    public ReservationHeap() {
        this.size = 0;
        // Initialize the heap array with a sentinel node at index 0
        heap = new Reservation[this.MAX_SIZE + 1];
        heap[0] = new Reservation(-1, Integer.MIN_VALUE, 0);
    }

    // Method to calculate the index of the parent of a given position
    private int parent(int pos) {
        return pos / 2;
    }

    // Method to calculate the index of the left child of a given position
    private int leftChild(int pos) {
        return 2 * pos;
    }

    // Method to calculate the index of the right child of a given position
    private int rightChild(int pos) {
        return 2 * pos + 1;
    }

    // Check if a position is a leaf node
    private boolean isLeaf(int pos) {
        if (pos > (size / 2)) {
            return true;
        }
        return false;
    }

    // Swap two elements in the heap array
    private void swap(int fpos, int spos) {
        Reservation temp;
        temp = heap[fpos];
        heap[fpos] = heap[spos];
        heap[spos] = temp;
    }

    // Check if the element at position i has a priority less than the element at position j
    private boolean checkForLessThan(int i, int j) {
        return (heap[i].getPriority() < heap[j].getPriority()) ||
                ((heap[i].getPriority() == heap[j].getPriority()) && heap[i].getTimeOfReservation() < heap[j].getTimeOfReservation());
    }

    // Maintain the min-heap property starting from a given position
    private void minHeapify(int pos) {
        if (!isLeaf(pos)) {
            int swapPos = pos;
            if (rightChild(pos) <= size) {
                // Determine the child with the smaller priority
                swapPos = checkForLessThan(leftChild(pos), rightChild(pos)) ? leftChild(pos) : rightChild(pos);
            } else {
                swapPos = leftChild(pos);
            }
            // Swap with the smaller child and continue heapifying
            if (leftChild(pos) <= size && rightChild(pos) <= size && (checkForLessThan(leftChild(pos), pos) || checkForLessThan(rightChild(pos), pos))) {
                swap(pos, swapPos);
                minHeapify(swapPos);
            }
        }
    }

    // Insert a new reservation into the heap
    public void insert(Reservation reservation) {
        if (size >= MAX_SIZE) {
            return; // Heap is full
        }
        heap[++size] = reservation; // Insert at the end
        int current = size;
        // Move the new element up the heap to maintain the min-heap property
        while (checkForLessThan(current, parent(current))) {
            swap(current, parent(current));
            current = parent(current);
        }
    }

    // Remove and return the reservation with the highest priority from the heap
    public Reservation remove() {
        Reservation popped = heap[FRONT];
        heap[FRONT] = heap[size--];
        heap[size + 1] = null;
        minHeapify(FRONT);
        return popped;
    }

    // Get a string representation of the heap
    public String getAsString() {
        // Create a copy of the heap array, filter out null values, and sort by priority
        Reservation[] dummy = Arrays.copyOf(heap, heap.length);
        Arrays.stream(dummy).filter(Objects::nonNull).collect(Collectors.toList()).sort(Comparator.comparingInt(Reservation::getPriority));
        // Build a string representation of the reservations
        StringBuilder output = new StringBuilder("[");
        for (Reservation reservation : dummy) {
            if (reservation != null && reservation.getPatronID() != -1) {
                if (output.toString().equals("[")) {
                    output.append(reservation.getPatronID());
                } else {
                    output.append(",").append(reservation.getPatronID());
                }
            }
        }
        output.append("]");
        return output.toString();
    }
}
