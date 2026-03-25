import java.util.*;

// Reservation
class Reservation {
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

// Add-On Service
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getCost() {
        return cost;
    }
}

// Add-On Service Manager
class AddOnServiceManager {

    // Map: Reservation ID -> List of Services
    private Map<String, List<AddOnService>> serviceMap = new HashMap<>();

    public void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);
    }

    public List<AddOnService> getServices(String reservationId) {
        return serviceMap.getOrDefault(reservationId, new ArrayList<>());
    }

    public double calculateTotalCost(String reservationId) {
        double total = 0;
        for (AddOnService s : getServices(reservationId)) {
            total += s.getCost();
        }
        return total;
    }
}

// Main
public class UseCase7AddOnServiceSelection {

    public static void main(String[] args) {

        System.out.println("Add-On Service Selection\n");

        // Create reservation
        Reservation r = new Reservation("R101", "Abhi", "Single");

        // Create services
        AddOnService breakfast = new AddOnService("Breakfast", 200);
        AddOnService wifi = new AddOnService("WiFi", 100);
        AddOnService pickup = new AddOnService("Airport Pickup", 500);

        // Service manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Add services to reservation
        manager.addService(r.getReservationId(), breakfast);
        manager.addService(r.getReservationId(), wifi);
        manager.addService(r.getReservationId(), pickup);

        // Display services
        System.out.println("Reservation ID: " + r.getReservationId());
        System.out.println("Guest: " + r.getGuestName());

        System.out.println("\nSelected Services:");
        for (AddOnService s : manager.getServices(r.getReservationId())) {
            System.out.println("- " + s.getServiceName() + " : " + s.getCost());
        }

        // Total cost
        System.out.println("\nTotal Add-On Cost: " + 
            manager.calculateTotalCost(r.getReservationId()));
    }
}
