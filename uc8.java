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

// Booking History (List preserves order)
class BookingHistory {
    private List<Reservation> history = new ArrayList<>();

    public void addReservation(Reservation r) {
        history.add(r);
    }

    public List<Reservation> getAllReservations() {
        return history;
    }
}

// Reporting Service
class BookingReportService {

    public void printAllBookings(List<Reservation> reservations) {
        System.out.println("Booking History:\n");
        for (Reservation r : reservations) {
            System.out.println("Reservation ID: " + r.getReservationId());
            System.out.println("Guest: " + r.getGuestName());
            System.out.println("Room Type: " + r.getRoomType());
            System.out.println();
        }
    }

    public void printSummary(List<Reservation> reservations) {
        Map<String, Integer> summary = new HashMap<>();

        for (Reservation r : reservations) {
            summary.put(r.getRoomType(),
                summary.getOrDefault(r.getRoomType(), 0) + 1);
        }

        System.out.println("Booking Summary:\n");
        for (String type : summary.keySet()) {
            System.out.println(type + " Rooms Booked: " + summary.get(type));
        }
    }
}

// Main
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();

        // Simulate confirmed bookings
        history.addReservation(new Reservation("R101", "Abhi", "Single"));
        history.addReservation(new Reservation("R102", "Subha", "Double"));
        history.addReservation(new Reservation("R103", "Kiran", "Single"));
        history.addReservation(new Reservation("R104", "Divya", "Suite"));

        BookingReportService reportService = new BookingReportService();

        reportService.printAllBookings(history.getAllReservations());
        reportService.printSummary(history.getAllReservations());
    }
}
