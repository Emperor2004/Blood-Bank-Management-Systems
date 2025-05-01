import java.util.Scanner;

public class Donor {
    private String name;
    private String bloodType;

    public Donor(String name, String bloodType) {
        this.name = name;
        this.bloodType = bloodType;
    }

    public void donateBlood(DatabaseManager db, Scanner sc) {
        try {
            System.out.print("Enter quantity to donate: ");
            int quantity = sc.nextInt();
            sc.nextLine(); // Clear buffer


            int currentQty = db.getBloodQuantity(1, bloodType);
            boolean updated = db.updateBloodQuantity(1, bloodType, currentQty + quantity);

            if (updated) {
                System.out.println("Donation successful. New quantity for " + bloodType + " at bank 1: " + (currentQty + quantity));
                System.out.println("Thank you " + name + " for donating " + quantity + " unit(s) of " + bloodType + " blood.");
            } else {
                System.out.println("Error updating blood stock. Please try again later.");
            }

        } catch (InvalidBloodTypeException | InvalidStaffOperationException e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    public void showMenu(DatabaseManager db, Scanner sc) {
        while (true) {

            System.out.println("\n--- Donor Menu ---");
            System.out.println("1. Donate Blood");
            System.out.println("2. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine();
            switch (choice) {
                case 1:
                    donateBlood(db, sc);
                    break;
                case 2:
                    System.out.println("Thank you for your contribution, " + name + "!");
                default:
                    System.out.println("Invalid choice. Please select again.");
                    break;
            }
        }
    }
}
