// BloodBankApp.java
import java.util.Scanner;

public class BloodBankApp {
    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Welcome to the Blood Bank Management System ===");
            System.out.println("1. Donor/Recipient");
            System.out.println("2. Staff Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            String input = sc.nextLine().trim();
            if (input.isEmpty()) continue;
            int choice = Integer.parseInt(input);

            switch (choice) {
                case 1:
                    System.out.print("Enter your name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter your blood type (e.g., A, B, AB, O): ");
                    String bloodType = sc.nextLine();
                    System.out.println("Select role:");
                    System.out.println("1. Donor");
                    System.out.println("2. Recipient");
                    System.out.print("Enter your choice: ");
                    int role = Integer.parseInt(sc.nextLine());
                    
                    switch (role) {
                        case 1:
                            Donor donor = new Donor(name, bloodType);
                            donor.showMenu(db);
                            break;
                        case 2:
                            Recipient recipient = new Recipient(name, bloodType);
                            recipient.showMenu(db);
                            break;
                        default:
                            System.out.println("Invalid role selection.");
                            break;
                    }   break;
                case 2:
                    System.out.print("Enter Staff ID: ");
                    String staffID = sc.nextLine();
                    System.out.print("Enter Password: ");
                    String password = sc.nextLine();
                    Staff staff = new Staff(staffID, password);
                    if (staff.login(db)) {
                        System.out.println("Staff login successful.");
                        staff.showMenu(db);
                    } else {
                        System.out.println("Invalid staff credentials.");
                    }   break;
                case 3:
                    System.out.println("Exiting the system. Goodbye!");
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}