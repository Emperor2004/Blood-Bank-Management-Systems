// DatabaseManager.java
import java.sql.*;

public class DatabaseManager {
    private Connection conn;
    public static final String[] VALID_BLOOD_TYPES = {"A", "B", "AB", "O"};

    public DatabaseManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/bloodbank",
                "root",
                "ved@2775"
            );
            System.out.println("Connected to the database successfully.");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private boolean isValidBloodType(String bloodType) {
        for (String type : VALID_BLOOD_TYPES) {
            if (type.equalsIgnoreCase(bloodType)) return true;
        }
        return false;
    }

    public int getBloodQuantity(int bankID, String bloodType) throws InvalidBloodTypeException {
        if (!isValidBloodType(bloodType)) {
            throw new InvalidBloodTypeException("Invalid blood type: " + bloodType);
        }
        if (conn == null) {
            throw new NullPointerException("Database connection is not established.");
        }
        int qty = 0;
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT quantity FROM BloodInventory WHERE bloodBankID = ? AND bloodType = ?")) {
            ps.setInt(1, bankID);
            ps.setString(2, bloodType.toUpperCase());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) qty = rs.getInt("quantity");
        } catch (SQLException e) {
            System.out.println("Error fetching inventory: " + e.getMessage());
        }
        return qty;
    }

    public boolean updateBloodQuantity(int bankID, String bloodType, int newQty)
            throws InvalidBloodTypeException, InvalidStaffOperationException {

        if (!isValidBloodType(bloodType)) {
            throw new InvalidBloodTypeException("Invalid blood type: " + bloodType);
        }
        if (newQty < 0) {
            throw new InvalidStaffOperationException("Inventory cannot be negative! Tried to set: " + newQty + " unit(s) for " + bloodType);
        }

        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE BloodInventory SET quantity = ? WHERE bloodBankID = ? AND bloodType = ?")) {
            ps.setInt(1, newQty);
            ps.setInt(2, bankID);
            ps.setString(3, bloodType.toUpperCase());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error updating inventory: " + e.getMessage());
            return false;
        }
    }

    public boolean validateStaff(String staffID, String password) {
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT 1 FROM Staff WHERE staffID = ? AND password = ?")) {
            ps.setString(1, staffID);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Staff validation error: " + e.getMessage());
            return false;
        }
    }
}