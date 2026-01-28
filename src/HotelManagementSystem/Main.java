package HotelManagementSystem;
import java.util.Scanner;

/**
 * Presentation Layer (UI)
 * Login credentials removed for direct access to the dashboard.
 */
public class Main {
    @SuppressWarnings("resource")
	public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Service service = new Service();

        String cyan = Service.CYAN;
        String reset = Service.RESET;

        // Directly starting the system without login prompts
        System.out.println(cyan + "🏨 WELCOME TO THE SMART HOTEL SYSTEM 🏨" + reset);

        while (true) {
            System.out.println(cyan + "\n========================================");
            System.out.println("        HOTEL MANAGEMENT DASHBOARD");
            System.out.println("========================================" + reset);
            System.out.println("1. 🚀 Auto Check-In (Quick)");
            System.out.println("2. 📝 Manual Check-In");
            System.out.println("3. 📑 Guest Ledger");
            System.out.println("4. 🛎️ Check-Out");
            System.out.println("5. 🏢 Room Status & Capacity");
            System.out.println("6. 🔍 Find Guest (by Name)");
            System.out.println("7. 🔑 Find Guest (by Room No)");
            System.out.println("8. 📊 Analytics");
            System.out.println("0. 🚪 Exit System");
            System.out.print("\n➤ Selection: ");

            String choice = sc.nextLine();

            try {
                switch (choice) {
                    case "1" -> { 
                        System.out.print("Enter Guest Name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter Room Type (Standard/Deluxe/Suite): ");
                        String type = sc.nextLine();
                        System.out.print("Enter Number of Days: ");
                        int days = Integer.parseInt(sc.nextLine());
                        
                        System.out.println(service.processAutoCheckIn(name, type, days));
                    }

                    case "2" -> { 
                        System.out.print("Enter Guest Name: ");
                        String name = sc.nextLine();
                        System.out.print("Enter Room Number: ");
                        int roomNo = Integer.parseInt(sc.nextLine());
                        System.out.print("Enter Number of Days: ");
                        int days = Integer.parseInt(sc.nextLine());

                        System.out.println(service.processCheckIn(name, roomNo, days));
                    }

                    case "3" -> service.showLedger();

                    case "4" -> {
                        System.out.print("Enter Room Number for Check-Out: ");
                        int roomNo = Integer.parseInt(sc.nextLine());
                        System.out.println(service.processCheckOut(roomNo));
                    }

                    case "5" -> service.displayRoomStatus();

                    case "6" -> {
                        System.out.print("Enter Guest Name to Search: ");
                        String name = sc.nextLine();
                        service.findGuest(name);
                    }

                    case "7" -> { 
                        System.out.print("Enter Room Number to Search: ");
                        int roomNo = Integer.parseInt(sc.nextLine());
                        service.findByRoom(roomNo);
                    }

                    case "8" -> service.showStats();

                    case "0" -> {
                        System.out.print("Are you sure you want to exit? (y/n): ");
                        if(sc.nextLine().equalsIgnoreCase("y")) {
                            System.out.println("👋 Thank you for using the system. Goodbye!");
                            System.exit(0);
                        }
                    }

                    default -> System.out.println(Service.RED + "⚠️ Invalid selection. Please try again." + reset);
                }
            } catch (NumberFormatException e) {
                System.out.println(Service.RED + "⚠️ Input Error: Please enter numbers for IDs, Room Numbers, and Days." + reset);
            } catch (Exception e) {
                System.out.println(Service.RED + "⚠️ Error: " + e.getMessage() + reset);
            }
        }
    }
}