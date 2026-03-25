import java.io.*;
import java.util.*;

// Reservation (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

// Inventory (Serializable)
class RoomInventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> availability = new HashMap<>();

    public RoomInventory() {
        availability.put("Single", 2);
        availability.put("Double", 1);
        availability.put("Suite", 1);
    }

    public Map<String, Integer> getAvailability() {
        return availability;
    }

    public void setAvailability(Map<String, Integer> availability) {
        this.availability = availability;
    }
}

// System State Wrapper
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Reservation> reservations;
    private RoomInventory inventory;

    public SystemState(List<Reservation> reservations, RoomInventory inventory) {
        this.reservations = reservations;
        this.inventory = inventory;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public RoomInventory getInventory() {
        return inventory;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    public static void save(SystemState state) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("System state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    public static SystemState load() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(FILE_NAME))) {

            System.out.println("System state loaded successfully.");
            return (SystemState) ois.readObject();

        } catch (Exception e) {
            System.out.println("No previous state found. Starting fresh.");
            return null;
        }
    }
}

// Main
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        // Try loading previous state
        SystemState state = PersistenceService.load();

        List<Reservation> reservations;
        RoomInventory inventory;

        if (state == null) {
            reservations = new ArrayList<>();
            inventory = new RoomInventory();

            // Simulate new bookings
            reservations.add(new Reservation("R101", "Abhi", "Single"));
            reservations.add(new Reservation("R102", "Subha", "Double"));

            System.out.println("New system state created.");
        } else {
            reservations = state.getReservations();
            inventory = state.getInventory();

            System.out.println("Recovered Reservations:");
            for (Reservation r : reservations) {
                System.out.println(r.getReservationId() + " - " 
                        + r.getGuestName() + " (" + r.getRoomType() + ")");
            }
        }

        // Save current state before shutdown
        PersistenceService.save(new SystemState(reservations, inventory));
    }
}
