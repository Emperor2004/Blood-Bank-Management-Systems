// Donor.java
import java.util.Scanner;

public class Donor extends User {

    public Donor(String name, String bloodType) {
        super(name, bloodType);
    }

    public void donateBlood(DatabaseManager db, int quantity) {
        boolean success = db.insertDonation(name, bloodType, quantity);
        if (success) {
            System.out.println("Thank you " + name + " for donating " + quantity +
                               " unit(s) of " + bloodType + " blood.");
        } else {
            System.out.println("Donation failed. Please try again.");
        }
    }

    @Override
    public void showMenu(DatabaseManager db) {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- Donor Menu ---");
        System.out.println("1. Donate Blood");
        System.out.println("2. Request Blood");
        System.out.print("Enter your choice: ");

        int choice = sc.nextInt();
        if (choice == 1) {
            System.out.print("Enter quantity to donate: ");
            int quantity = sc.nextInt();
            donateBlood(db, quantity);

        } else if (choice == 2) {
            System.out.print("Enter quantity to request: ");
            int quantity = sc.nextInt();
            boolean available = db.processBloodRequest(name, bloodType, quantity);
            if (available) {
                System.out.println("Your request for " + quantity +
                                   " unit(s) of " + bloodType + " blood has been processed.");
            }

        } else {
            System.out.println("Invalid choice.");
        }
        // note: not closing sc here so System.in remains open for main
    }
}

