import java.util.*;

// Reservation
class Reservation {
    private String reservationId;
    private String roomType;

    public Reservation(String reservationId, String roomType) {
        this.reservationId = reservationId;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Inventory
class RoomInventory {
    private Map<String, Integer> availability = new HashMap<>();

    public RoomInventory() {
        availability.put("Single", 1);
        availability.put("Double", 1);
        availability.put("Suite", 1);
    }

    public void decrement(String type) {
        availability.put(type, availability.get(type) - 1);
    }

    public void increment(String type) {
        availability.put(type, availability.get(type) + 1);
    }

    public int getAvailable(String type) {
        return availability.getOrDefault(type, 0);
    }
}

// Booking Service
class BookingService {

    private RoomInventory inventory;
    private Map<String, String> confirmedBookings = new HashMap<>(); // reservationId -> roomType
    private Stack<String> rollbackStack = new Stack<>();

    private int counter = 1;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public String book(String roomType) {
        if (inventory.getAvailable(roomType) <= 0) {
            System.out.println("Booking failed: No rooms available for " + roomType);
            return null;
        }

        String reservationId = "R" + counter++;
        confirmedBookings.put(reservationId, roomType);

        inventory.decrement(roomType);

        System.out.println("Booking confirmed: " + reservationId + " (" + roomType + ")");
        return reservationId;
    }

    public void cancel(String reservationId) {

        if (!confirmedBookings.containsKey(reservationId)) {
            System.out.println("Cancellation failed: Invalid reservation ID " + reservationId);
            return;
        }

        String roomType = confirmedBookings.get(reservationId);

        // Push to rollback stack
        rollbackStack.push(reservationId);

        // Restore inventory
        inventory.increment(roomType);

        // Remove booking
        confirmedBookings.remove(reservationId);

        System.out.println("Cancellation successful: " + reservationId + " (" + roomType + ")");
    }
}

// Main
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService(inventory);

        String r1 = service.book("Single");
        String r2 = service.book("Double");

        service.cancel(r1);

        // Invalid cancellation
        service.cancel("R999");
    }
}
