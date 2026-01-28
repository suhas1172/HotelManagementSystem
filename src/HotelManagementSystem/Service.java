package HotelManagementSystem;
import java.util.List;

/**
 * Service Class (Business Logic Layer)
 * Coordinates between the UI and Data layer, enforcing policies and formatting.
 */
public class Service {
    
    private DAO dao = new DAOIMPL();

    // ANSI Color Constants for Professional Console Output
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String CYAN = "\u001B[36m";

    /**
     * Helper Method: Formats names to Title Case (e.g., "suhas" -> "Suhas")
     */
    private String formatName(String name) {
        if (name == null || name.isEmpty()) return "Unknown";
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }

    /**
     * AUTO-ROOM CHECK-IN: Finds room automatically and validates input.
     */
    public String processAutoCheckIn(String name, String type, int days) {
        if (days <= 0) {
            return RED + "❌ Error: Stay duration must be at least 1 day." + RESET;
        }

        String formattedName = formatName(name);

        // 1. Find the first available room of that type
        int roomNo = dao.getFirstAvailableRoom(type);
        if (roomNo == -1) {
            return RED + "❌ Error: No " + type + " rooms are currently available." + RESET;
        }

        // 2. Enforce the 10-room limit per type
        int occupiedCount = dao.getOccupiedCountByType(type);
        if (occupiedCount >= 10) {
            return RED + "❌ Error: Capacity reached! All 10 " + type + " rooms are full." + RESET;
        }

        // 3. Determine price
        double price = 0;
        if (type.equalsIgnoreCase("Standard")) price = 500.0;
        else if (type.equalsIgnoreCase("Deluxe")) price = 1000.0;
        else if (type.equalsIgnoreCase("Suite")) price = 2000.0;

        double total = price * days;
        
        // 4. Update Database
        dao.addBooking(new DTO(0, formattedName, roomNo, days, total));
        dao.updateStatus(roomNo, "Occupied");
        
        return GREEN + "✨ Success! Guest " + formattedName + " assigned to Room " + roomNo + ". Bill: $" + total + RESET;
    }

    /**
     * SEARCH: Lookup active guest by Room Number.
     */
    public void findByRoom(int roomNo) {
        DTO guest = dao.getGuestByRoom(roomNo);
        if (guest == null) {
            System.out.println(CYAN + "🔍 Room " + roomNo + " is currently vacant." + RESET);
        } else {
            System.out.println(GREEN + "✅ Found: " + guest.getGuestName() + " is in Room " + roomNo + " | Bill: $" + guest.getBill() + RESET);
        }
    }

    /**
     * MANUAL CHECK-IN: Allows specific room selection.
     */
    public String processCheckIn(String name, int roomNo, int days) {
        if (days <= 0) return RED + "❌ Error: Invalid stay duration." + RESET;
        
        double price = dao.getRoomPrice(roomNo);
        if (price == -1) return RED + "❌ Room unavailable or invalid." + RESET;

        // Extract type from room data string
        String type = dao.getRoomAvailability().stream()
                        .filter(s -> s.contains("Room " + roomNo))
                        .map(s -> s.substring(s.indexOf("[") + 1, s.indexOf("]")).trim())
                        .findFirst().orElse("");

        int occupiedCount = dao.getOccupiedCountByType(type);
        if (occupiedCount >= 10) {
            return RED + "❌ Error: Capacity reached for " + type + "." + RESET;
        }

        String formattedName = formatName(name);
        double total = price * days;
        dao.addBooking(new DTO(0, formattedName, roomNo, days, total));
        dao.updateStatus(roomNo, "Occupied");
        
        return GREEN + "✨ Success! Bill: $" + total + RESET;
    }

    public String processCheckOut(int roomNo) {
        DTO guest = dao.getBookingByRoom(roomNo);
        if (guest == null) return RED + "⚠️ No guest found in Room " + roomNo + RESET;
        
        dao.deleteBooking(roomNo);
        dao.updateStatus(roomNo, "Available");
        return GREEN + "🛎️ Check-out complete for " + guest.getGuestName() + RESET;
    }

    public void showLedger() {
        List<DTO> all = dao.getAllBookings();
        if (all.isEmpty()) {
            System.out.println("📋 Ledger is empty.");
        } else {
            System.out.println(CYAN + "--- CURRENT GUEST LEDGER ---" + RESET);
            all.forEach(d -> 
                System.out.printf("ID:%-3d | Guest:%-12s | Room:%-4d | Bill:$%.2f\n", 
                d.getId(), d.getGuestName(), d.getRoomNo(), d.getBill()));
        }
    }

    public void showStats() {
        System.out.println(CYAN + "\n--- 📊 HOTEL ANALYTICS ---" + RESET);
        System.out.println("💰 Total Revenue: $" + dao.calculateTotalRevenue());
        long occupied = dao.getRoomAvailability().stream()
                           .filter(s -> s.contains("Occupied"))
                           .count();
        System.out.println("🏨 Total Occupied Rooms: " + occupied);
    }

    /**
     * STATUS: Displays grouped room ranges for cleaner view.
     */
    public void displayRoomStatus() {
        System.out.println(CYAN + "\n--- CURRENT ROOM STATUS (GROUPED) ---" + RESET);
        
        List<String> rawStatus = dao.getRoomAvailability();
        if (rawStatus.isEmpty()) {
            System.out.println("No rooms found.");
            return;
        }

        int startRoom = -1;
        int lastRoom = -1;
        String currentType = "";

        for (int i = 0; i <= rawStatus.size(); i++) {
            String line = (i < rawStatus.size()) ? rawStatus.get(i) : null;
            
            int roomNo = -1;
            String type = "";
            String status = "";
            
            if (line != null) {
                roomNo = Integer.parseInt(line.substring(5, line.indexOf("[")).trim());
                type = line.substring(line.indexOf("[") + 1, line.indexOf("]")).trim();
                status = line.substring(line.indexOf(":") + 1).trim();
            }

            // Logic to group consecutive Available rooms
            if (line != null && status.equals("Available") && (startRoom == -1 || (roomNo == lastRoom + 1 && type.equals(currentType)))) {
                if (startRoom == -1) {
                    startRoom = roomNo;
                    currentType = type;
                }
                lastRoom = roomNo;
            } else {
                if (startRoom != -1) {
                    String range = (startRoom == lastRoom) ? String.valueOf(startRoom) : startRoom + "-" + lastRoom;
                    System.out.println(GREEN + "Rooms " + range + " [" + currentType + "]: Available" + RESET);
                    startRoom = -1;
                }
                
                if (line != null && status.equals("Occupied")) {
                    System.out.println(RED + "Room " + roomNo + " [" + type + "]: Occupied" + RESET);
                }
                
                if (line != null && status.equals("Available")) {
                    startRoom = roomNo;
                    currentType = type;
                    lastRoom = roomNo;
                }
            }
        }
    }
    
    // Kept for consistency with the Main class
    public void findGuest(String name) {
        List<DTO> found = dao.searchByName(name);
        if (found.isEmpty()) {
            System.out.println("🔍 No records found for: " + name);
        } else {
            found.forEach(d -> System.out.println(GREEN + "✅ Found: " + d.getGuestName() + " in Room " + d.getRoomNo() + RESET));
        }
    }
}