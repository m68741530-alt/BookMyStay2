import java.util.*;

// Reservation
class Reservation {
    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Thread-safe Queue
class BookingQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public synchronized void add(Reservation r) {
        queue.offer(r);
    }

    public synchronized Reservation get() {
        return queue.poll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}

// Inventory (Shared Resource)
class RoomInventory {
    private Map<String, Integer> availability = new HashMap<>();

    public RoomInventory() {
        availability.put("Single", 2);
        availability.put("Double", 1);
        availability.put("Suite", 1);
    }

    public synchronized boolean bookRoom(String type) {
        int available = availability.getOrDefault(type, 0);

        if (available <= 0) {
            return false;
        }

        availability.put(type, available - 1);
        return true;
    }
}

// Booking Processor (Thread)
class BookingProcessor extends Thread {

    private BookingQueue queue;
    private RoomInventory inventory;

    public BookingProcessor(BookingQueue queue, RoomInventory inventory) {
        this.queue = queue;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {

            Reservation r;

            synchronized (queue) {
                if (queue.isEmpty()) break;
                r = queue.get();
            }

            if (r != null) {
                boolean success = inventory.bookRoom(r.getRoomType());

                if (success) {
                    System.out.println(Thread.currentThread().getName()
                            + " booked " + r.getRoomType()
                            + " for " + r.getGuestName());
                } else {
                    System.out.println(Thread.currentThread().getName()
                            + " failed booking for " + r.getGuestName()
                            + " (" + r.getRoomType() + ")");
                }
            }
        }
    }
}

// Main
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        BookingQueue queue = new BookingQueue();
        RoomInventory inventory = new RoomInventory();

        // Simulate multiple requests
        queue.add(new Reservation("Abhi", "Single"));
        queue.add(new Reservation("Subha", "Single"));
        queue.add(new Reservation("Kiran", "Single"));
        queue.add(new Reservation("Divya", "Double"));
        queue.add(new Reservation("Ram", "Suite"));

        // Multiple threads (guests)
        BookingProcessor t1 = new BookingProcessor(queue, inventory);
        BookingProcessor t2 = new BookingProcessor(queue, inventory);

        t1.setName("Thread-1");
        t2.setName("Thread-2");

        t1.start();
        t2.start();
    }
}
