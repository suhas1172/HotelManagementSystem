package HotelManagementSystem;
import java.util.List;

/**
 * DAO Interface (Data Access Object)
 * Defines the contract for database operations.
 */
public interface DAO {
    // REMOVED: boolean validateUser(String user, String pass); - Login no longer required.

    // --- CREATE ---
    /** Adds a new guest record to the reservations table. */
    void addBooking(DTO dto);                       

    // --- READ ---
    /** Retrieves all active guest records. */
    List<DTO> getAllBookings();                    

    /** Searches for guests by name (partial matches supported). */
    List<DTO> searchByName(String name);           

    /** Fetches the price of a specific room. */
    double getRoomPrice(int roomNo);               

    /** Retrieves guest details currently assigned to a specific room. */
    DTO getBookingByRoom(int roomNo);              

    /** Fetches room details including room number, type, and status. */
    List<String> getRoomAvailability();            

    /** Retrieves guest information by room number for the search feature. */
    DTO getGuestByRoom(int roomNo);               

    // --- UPDATE ---
    /** Changes the status of a room (e.g., 'Available' to 'Occupied'). */
    void updateStatus(int roomNo, String status);  

    // --- DELETE ---
    /** Removes a reservation record when a guest checks out. */
    void deleteBooking(int roomNo);                

    // --- LOGIC & ANALYTICS ---
    /** Returns the count of occupied rooms for a specific category (Standard, Deluxe, Suite). */
    int getOccupiedCountByType(String type);      

    /** Finds the lowest available room number for a specific category. */
    int getFirstAvailableRoom(String type);       
    
    /** Sums all bills in the reservations table for total income reports. */
    double calculateTotalRevenue();                
}