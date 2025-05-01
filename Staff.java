// Staff.java
import java.util.InputMismatchException;
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
            try {
                System.out.println("\n--- Staff Menu ---");
                System.out.println("1. Check Blood Inventory");
                System.out.println("2. Update Blood Inventory");
                System.out.println("3. Logout");
                System.out.print("Enter your choice: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        {
                            System.out.print("Enter blood type: ");
                            String bloodType = sc.nextLine();
                            try {
                                int quantity = db.getBloodQuantity(1, bloodType);
                                System.out.println("Current inventory for " + bloodType.toUpperCase() + " at bank 1: " + quantity + " unit(s).");
                            } catch (InvalidBloodTypeException e) {
                                System.out.println("Error: " + e.getMessage());
                            }       break;
                        }
                    case 2:
                        {
                            System.out.print("Enter blood type: ");
                            String bloodType = sc.nextLine();
                            System.out.print("Enter new quantity: ");
                            int quantity = sc.nextInt();
                            sc.nextLine(); // Clear buffer
                            try {
                                boolean updated = db.updateBloodQuantity(1, bloodType, quantity);
                                if (updated) {
                                    System.out.println("Inventory updated successfully for " + bloodType.toUpperCase() + " at bank 1.");
                                } else {
                                    System.out.println("Failed to update inventory.");
                                }
                            } catch (InvalidBloodTypeException | InvalidStaffOperationException e) {
                                System.out.println("Error: " + e.getMessage());
                            }       break;
                        }
                    case 3:
                        System.out.println("Logging out from staff account...");
                        break;
                    default:
                        System.out.println("Invalid choice. Try again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid number.");
                sc.nextLine();
            }
        }
    }

    public String getStaffID() {
        return staffID;
    }

    public void setStaffID(String staffID) {
        this.staffID = staffID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
