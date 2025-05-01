import java.util.Scanner;

public class Recipient {
    private String name;
    private String bloodType;

    public Recipient(String name, String bloodType) {
        this.name = name;
        this.bloodType = bloodType;
    }

    public void requestBlood(DatabaseManager db, Scanner sc) {
        try {
            System.out.print("Enter quantity to request: ");
            int quantity = sc.nextInt();
            sc.nextLine(); // Clear buffer

            int availableQty = db.getBloodQuantity(1, bloodType);
            if (availableQty < quantity) {
                System.out.println("Insufficient stock. Only " + availableQty + " unit(s) available for " + bloodType + ".");
                return;
            }

            boolean updated = db.updateBloodQuantity(1, bloodType, availableQty - quantity);
            if (updated) {
                System.out.println("Request successful. Remaining quantity for " + bloodType + " at bank 1: " + (availableQty - quantity));
                System.out.println("Dear " + name + ", your " + quantity + " unit(s) of " + bloodType + " blood is ready.");
            } else {
                System.out.println("Error processing request. Please try again later.");
            }
        } catch (InvalidBloodTypeException | InvalidStaffOperationException e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    public void showMenu(DatabaseManager db, Scanner sc) {
        OUTER:
        while (true) {
            System.out.println("\n--- Recipient Menu ---");
            System.out.println("1. Request Blood");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();
            
            switch (choice) {
                case 1:
                    requestBlood(db, sc);
                    break;
                case 2:
                    System.out.println("Thank you " + name + ", stay healthy!");
                    break OUTER;
                default:
                    System.out.println("Invalid choice. Please select again.");
                    break;
            }
        }
    }
}
