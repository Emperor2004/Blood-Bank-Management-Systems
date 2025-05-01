// Recipient.java
import java.util.Scanner;

public class Recipient extends User {

    public Recipient(String name, String bloodType) {
        super(name, bloodType);
    }

    public void requestBlood(DatabaseManager db, int quantity) {
        boolean available = db.processBloodRequest(name, bloodType, quantity);
        if (available) {
            System.out.println("Your request for " + quantity +
                               " unit(s) of " + bloodType + " blood has been processed.");
        }
    }

    @Override
    public void showMenu(DatabaseManager db) {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n--- Recipient Menu ---");
        System.out.println("1. Request Blood");
        System.out.print("Enter your choice: ");

        int choice = sc.nextInt();
        if (choice == 1) {
            System.out.print("Enter quantity to request: ");
            int quantity = sc.nextInt();
            requestBlood(db, quantity);
        } else {
            System.out.println("Invalid choice.");
        }
        // note: not closing sc here
    }
}