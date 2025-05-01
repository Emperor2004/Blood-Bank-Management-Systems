// Staff.java
import java.util.Scanner;

public class Staff {
    private String staffID;
    private String password;

    public Staff(String staffID, String password) {
        this.staffID = staffID;
        this.password = password;
    }

    public boolean login(DatabaseManager db) {
        return db.validateStaff(staffID, password);
    }

    public void showMenu(DatabaseManager db) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Staff Menu ---");
            System.out.println("1. Check Blood Inventory");
            System.out.println("2. Update Blood Inventory");
            System.out.println("3. Logout");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            if (choice == 1) {
                System.out.print("Enter blood type: ");
                String bloodType = sc.next();
                int quantity = db.getBloodQuantity(1, bloodType);
                System.out.println("Current inventory for " +
                    bloodType.toUpperCase() + " at bank 1: " +
                    quantity + " unit(s).");

            } else if (choice == 2) {
                System.out.print("Enter blood type: ");
                String bloodType = sc.next();
                System.out.print("Enter new quantity: ");
                int quantity = sc.nextInt();
                boolean updated = db.updateBloodQuantity(1, bloodType, quantity);
                if (updated) {
                    System.out.println("Inventory updated successfully for " +
                        bloodType.toUpperCase() + " at bank 1.");
                } else {
                    System.out.println("Failed to update inventory.");
                }

            } else if (choice == 3) {
                System.out.println("Logging out from staff account...");
                break;
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
        // note: not closing sc here
    }
}