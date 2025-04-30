// DatabaseManager.java
import java.sql.*;
import java.time.LocalDate;

public class DatabaseManager {
    private final Connection conn;
    private final int defaultBankID = 1;
    public static final String[] VALID_BLOOD_TYPES = {"A", "B", "AB", "O"};

    public DatabaseManager() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/bloodbank",
            "root",
            "2-Liccolony"
        );
        System.out.println("Connected to the database successfully.");
    }

    private boolean isValidBloodType(String bloodType) {
        for (String type : VALID_BLOOD_TYPES) {
            if (type.equalsIgnoreCase(bloodType)) return true;
        }
        return false;
    }

    public int getBloodQuantity(int bankID, String bloodType) throws Exception {
        if (!isValidBloodType(bloodType)) {
            System.out.println("Invalid blood type: " + bloodType);
            return 0;
        }
        int qty = 0;
        PreparedStatement ps = conn.prepareStatement(
            "SELECT quantity FROM BloodInventory WHERE bloodBankID = ? AND bloodType = ?");
        ps.setInt(1, bankID);
        ps.setString(2, bloodType.toUpperCase());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            qty = rs.getInt("quantity");
        }
        return qty;
    }

    public boolean updateBloodQuantity(int bankID, String bloodType, int newQty) throws Exception {
        if (!isValidBloodType(bloodType)) {
            System.out.println("Invalid blood type: " + bloodType);
            return false;
        }
        PreparedStatement ps = conn.prepareStatement(
            "UPDATE BloodInventory SET quantity = ? WHERE bloodBankID = ? AND bloodType = ?");
        ps.setInt(1, newQty);
        ps.setInt(2, bankID);
        ps.setString(3, bloodType.toUpperCase());
        return ps.executeUpdate() > 0;
    }

    private int getOrCreateDonorID(String name, String bloodType) throws Exception {
        PreparedStatement ps = conn.prepareStatement(
            "SELECT donorID FROM Donors WHERE name = ? AND bloodType = ?");
        ps.setString(1, name);
        ps.setString(2, bloodType.toUpperCase());
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("donorID");
        }
        PreparedStatement ps2 = conn.prepareStatement(
            "INSERT INTO Donors (name, bloodType, address, city, state, contact) VALUES (?, ?, NULL, NULL, NULL, NULL)",
            Statement.RETURN_GENERATED_KEYS);
        ps2.setString(1, name);
        ps2.setString(2, bloodType.toUpperCase());
        ps2.executeUpdate();
        ResultSet keys = ps2.getGeneratedKeys();
        if (keys.next()) {
            return keys.getInt(1);
        }
        System.out.println("Failed to retrieve new donorID");
        return 0;
    }

    public boolean insertDonation(String donorName, String bloodType, int quantityDonated) throws Exception {
        int currentQty = getBloodQuantity(defaultBankID, bloodType);
        int newQty = currentQty + quantityDonated;
        boolean inventoryOk = updateBloodQuantity(defaultBankID, bloodType, newQty);
        if (!inventoryOk) return false;

        int donorID = getOrCreateDonorID(donorName, bloodType);

        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO DonationEvents " +
            "(donorID, bloodBankID, bloodType, quantity, donationDate) " +
            "VALUES (?, ?, ?, ?, ?)");
        ps.setInt(1, donorID);
        ps.setInt(2, defaultBankID);
        ps.setString(3, bloodType.toUpperCase());
        ps.setInt(4, quantityDonated);
        ps.setDate(5, Date.valueOf(LocalDate.now()));
        ps.executeUpdate();

        System.out.println("Donation successful. New quantity for " +
            bloodType.toUpperCase() + " at bank " + defaultBankID + ": " + newQty);
        return true;
    }

    public boolean processBloodRequest(String recipientName, String bloodType, int qtyReq) throws Exception {
        int currentQty = getBloodQuantity(defaultBankID, bloodType);
        if (currentQty < qtyReq) {
            System.out.println("Insufficient stock for " + bloodType.toUpperCase() +
                ". Available: " + currentQty);
            return false;
        }
        int newQty = currentQty - qtyReq;
        boolean invOk = updateBloodQuantity(defaultBankID, bloodType, newQty);
        if (invOk) {
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO BloodRequests " +
                "(recipientID, recipientName, bloodType, requiredQuantity, " +
                " requestDate, address, city, state, contact, status, assignedBloodBankID) " +
                "VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, recipientName);
            ps.setString(2, bloodType.toUpperCase());
            ps.setInt(3, qtyReq);
            ps.setDate(4, Date.valueOf(LocalDate.now()));
            ps.setString(5, "Default Address, Pune");
            ps.setString(6, "Pune");
            ps.setString(7, "Maharashtra");
            ps.setString(8, "0000000000");
            ps.setString(9, "Approved");
            ps.setInt(10, defaultBankID);
            ps.executeUpdate();
            System.out.println("Request approved. New quantity for " +
                bloodType.toUpperCase() + ": " + newQty);
            return true;
        }
        return false;
    }

    public boolean validateStaff(String staffID, String password) throws Exception {
        PreparedStatement ps = conn.prepareStatement(
            "SELECT 1 FROM Staff WHERE staffID = ? AND password = ?");
        ps.setString(1, staffID);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }
}

