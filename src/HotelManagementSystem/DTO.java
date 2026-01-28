package HotelManagementSystem;
/**
 * DTO (Data Transfer Object)
 * Represents a single guest reservation record.
 */
public class DTO {
    private int id;
    private String guestName;
    private int roomNo;
    private int days;
    private double bill;

    // Default Constructor
    public DTO() {}

    // Parameterized Constructor
    public DTO(int id, String guestName, int roomNo, int days, double bill) {
        this.id = id; 
        this.guestName = guestName; 
        this.roomNo = roomNo; 
        this.days = days; 
        this.bill = bill;
    }

    // --- Getters ---
    public int getId() { return id; }
    public String getGuestName() { return guestName; }
    public int getRoomNo() { return roomNo; }
    public int getDays() { return days; }
    public double getBill() { return bill; }

    // --- Setters (Essential for business logic updates) ---
    public void setId(int id) { this.id = id; }
    public void setGuestName(String guestName) { this.guestName = guestName; }
    public void setRoomNo(int roomNo) { this.roomNo = roomNo; }
    public void setDays(int days) { this.days = days; }
    public void setBill(double bill) { this.bill = bill; }

    /**
     * Optional: toString override for quick debugging
     */
    @Override
    public String toString() {
        return "Guest: " + guestName + " | Room: " + roomNo + " | Bill: $" + bill;
    }
}