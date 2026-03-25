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

// Queue (FIFO)
class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();

    public void addRequest(Reservation r) {
        queue.offer(r);
    }

    public boolean hasPendingRequests() {
        return !queue.isEmpty();
    }

    public Reservation getNextRequest() {
        return queue.poll();
    }
}

// Inventory Service
class RoomInventory {
    private Map<String, Integer> availability = new HashMap<>();

    public RoomInventory() {
        availability.put("Single", 2);
        availability.put("Double", 2);
        availability.put("Suite", 1);
    }

    public int getAvailable(String type) {
        return availability.getOrDefault(type, 0);
    }

    public void decrement(String type) {
        availability.put(type, getAvailable(type) - 1);
    }
}

// Booking Service
class BookingService {

    private RoomInventory inventory;

    // Track allocated room IDs
    private Set<String> allocatedRoomIds = new HashSet<>();

    // Map room type -> assigned IDs
    private Map<String, Set<String>> roomAllocations = new HashMap<>();

    private int idCounter = 1;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void process(Reservation r) {

        String type = r.getRoomType();

        if (inventory.getAvailable(type) <= 0) {
            System.out.println("No rooms available for " + type + " for Guest: " + r.getGuestName());
            return;
        }

        // Generate unique room ID
        String roomId;
        do {
            roomId = type.substring(0, 1).toUpperCase() + idCounter++;
        } while (allocatedRoomIds.contains(roomId));

        // Store ID
        allocatedRoomIds.add(roomId);

        roomAllocations.putIfAbsent(type, new HashSet<>());
        roomAllocations.get(type).add(roomId);

        // Update inventory
        inventory.decrement(type);

        // Confirm booking
        System.out.println("Booking Confirmed → Guest: " + r.getGuestName()
                + ", Room Type: " + type
                + ", Room ID: " + roomId);
    }
}

// Main
public class UseCase6RoomAllocationService {

    public static void main(String[] args) {

        System.out.println("Room Allocation Service\n");

        BookingRequestQueue queue = new BookingRequestQueue();
        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService(inventory);

        queue.addRequest(new Reservation("Abhi", "Single"));
        queue.addRequest(new Reservation("Subha", "Double"));
        queue.addRequest(new Reservation("Ram", "Single"));
        queue.addRequest(new Reservation("Divya", "Suite"));
        queue.addRequest(new Reservation("Kiran", "Suite")); // should fail

        while (queue.hasPendingRequests()) {
            service.process(queue.getNextRequest());
        }
    }
}
