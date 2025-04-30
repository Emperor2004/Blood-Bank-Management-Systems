# Blood-Bank-Management-Systems

## 🗃️File Descriptions

### [bloodbank.sql](bloodbank.sql)

The `bloodbank` database models a simple network of blood banks, donors, recipients, inventory, donation events, transfer logs and staff. It supports:

- Tracking inventory levels by blood type at each bank
- Recording donations and requests
- Managing transfers between banks
- Simple user (staff) authentication

---

### [DatabaseManager.java](DatabaseManager.java)

A Java helper for managing the `bloodbank` MySQL database. Handles connection setup and provides methods to:

- `getBloodQuantity(bankID, bloodType)`: Returns stock (0 if invalid).
- `updateBloodQuantity(bankID, bloodType, newQty)`: Updates inventory.
- `insertDonation(donorName, bloodType, quantity)`: Updates stock, ensures donor exists, logs donation.
- `processBloodRequest(recipientName, bloodType, qty)`: Checks stock, deducts, creates approved request.
- `validateStaff(staffID, password)`: Verifies credentials.

Config:

- JDBC URL, user, pass hardcoded in constructor.
- Default bank ID = 1; valid types = A, B, AB, O.
