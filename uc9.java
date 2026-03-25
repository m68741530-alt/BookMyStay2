import java.util.*;

// Custom Exception
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

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

// Inventory
class RoomInventory {
    private Map<String, Integer> availability = new HashMap<>();

    public RoomInventory() {
        availability.put("Single", 2);
        availability.put("Double", 1);
        availability.put("Suite", 0);
    }

    public int getAvailable(String type) {
        return availability.getOrDefault(type, -1);
    }

    public void decrement(String type) {
        availability.put(type, availability.get(type) - 1);
    }

    public boolean isValidRoomType(String type) {
        return availability.containsKey(type);
    }
}

// Validator
class BookingValidator {

    public static void validate(Reservation r, RoomInventory inventory)
            throws InvalidBookingException {

        if (r.getGuestName() == null || r.getGuestName().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        if (!inventory.isValidRoomType(r.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + r.getRoomType());
        }

        if (inventory.getAvailable(r.getRoomType()) <= 0) {
            throw new InvalidBookingException(
                    "No rooms available for type: " + r.getRoomType());
        }
    }
}

// Booking Service
class BookingService {
    private RoomInventory inventory;

    public BookingService(RoomInventory inventory) {
        this.inventory = inventory;
    }

    public void book(Reservation r) {
        try {
            BookingValidator.validate(r, inventory);

            inventory.decrement(r.getRoomType());

            System.out.println("Booking successful for " + r.getGuestName()
                    + " (" + r.getRoomType() + ")");

        } catch (InvalidBookingException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }
    }
}

// Main
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingService service = new BookingService(inventory);

        // Valid booking
        service.book(new Reservation("Abhi", "Single"));

        // Invalid room type
        service.book(new Reservation("Subha", "Deluxe"));

        // No availability
        service.book(new Reservation("Kiran", "Suite"));

        // Empty guest name
        service.book(new Reservation("", "Double"));
    }
}
