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

Manages MySQL interactions for the `bloodbank` schema, including stock queries and updates with validation.

- **Constructor**  
  Initializes JDBC connection to `jdbc:mysql://localhost:3306/bloodbank` using user and password.

- **isValidBloodType(type)**  
  Private helper to check against `VALID_BLOOD_TYPES = {"A","B","AB","O"}`.

- **getBloodQuantity(bankID, bloodType)**  
  - Throws `InvalidBloodTypeException` if type invalid.  
  - Returns current stock (0 if no record).

- **updateBloodQuantity(bankID, bloodType, newQty)**  
  - Throws `InvalidBloodTypeException` if type invalid.  
  - Throws `InvalidStaffOperationException` if `newQty < 0`.  
  - Updates inventory and returns `true` on success.

- **validateStaff(staffID, password)**  
  Calls `db.validateStaff` query and returns `true` if credentials match.

---

### [User.java](User.java)

Base for all users (Donor/Recipient), enforcing a common interface.

- **Fields**:
  - `String name` — user’s name
  - `String bloodType` — uppercase blood group

- **Constructor**: Stores `name` and `bloodType.toUpperCase()`.
- **Abstract** `showMenu(DatabaseManager db)`: Each subclass provides its own console menu.

---

### [Donor.java](Donor.java)

Represents a blood donor with simple console interactions.

- **Extends**: `User` (holds `name` and `bloodType`).
- **donateBlood(db, qty)**: Updates inventory via `db.insertDonation`, then prints a thank-you or failure message.
- **showMenu(db)**: Displays a two-option menu:
  1. **Donate Blood** — prompts quantity and calls `donateBlood`.
  2. **Request Blood** — prompts quantity and calls `db.processBloodRequest`, then prints confirmation on approval.
- **Note**: Uses a shared `Scanner` without closing `System.in` to allow continuous input.

---

### [Recipient.java](Recipient.java)

Represents a blood recipient with console-based request functionality.

- **Extends**: `User` (stores `name` and `bloodType`).
- **requestBlood(db, qty)**: Calls `db.processBloodRequest`; prints confirmation if approved.
- **showMenu(db)**: Displays a single-option menu:
  1. **Request Blood** — prompts quantity and invokes `requestBlood`.
- **Note**: Uses a shared `Scanner` without closing `System.in` for continuous input.

---

### [Staff.java](Staff.java)

Handles staff authentication and inventory management via a console menu.

- **Fields**: `staffID`, `password`.
- **login(db)**: Returns `db.validateStaff(staffID, password)`.
- **showMenu(db)**: Loops until logout offering:
  1. **Check Inventory** — prompts blood type, displays `db.getBloodQuantity(1, type)`.
  2. **Update Inventory** — prompts blood type & quantity, calls `db.updateBloodQuantity(1, type, qty)`, confirms outcome.
  3. **Logout** — exits menu.

- Note: Uses `Scanner` without closing `System.in`.

---

## ➡ _Custom Exceptions_:-

### [InsufficientBloodStockException.java](InsufficientBloodStockException.java)

A custom checked exception thrown when trying to process a blood operation but the available stock is insufficient.

- **Extends**: `Exception`
- **Constructor**: Takes a `String message` describing the shortage.

---

### [InvalidBloodTypeException.java](InvalidBloodTypeException.java)

A custom checked exception thrown when an operation receives an unrecognized blood type.

- **Extends**: `Exception`
- **Constructor**: Accepts a `String message` explaining the invalid type.

---

### [InvalidStaffOperationException.java](InvalidStaffOperationException.java)

A custom checked exception thrown when a staff-triggered operation is not permitted or fails validation.

- **Extends**: `Exception`
- **Constructor**: Accepts a `String message` detailing the invalid operation.
