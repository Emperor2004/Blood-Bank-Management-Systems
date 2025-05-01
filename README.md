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

Provides a console‐based interface for donors to record blood donations.

- **Purpose**  
  Guides donors through the donation process and updates inventory.

- **Donation Workflow**  
  - Prompts donor for quantity to donate  
  - Retrieves current stock via `DatabaseManager.getBloodQuantity`  
  - Updates total using `DatabaseManager.updateBloodQuantity`  
  - Confirms success with new stock level and a thank‐you message  
  - Reports errors if the update fails

- **Main Menu**  
  1. **Donate Blood**  
     - Calls the donation workflow  
  2. **Exit**  
     - Displays a farewell message and ends the loop  
     - Invalid selections prompt a retry message

- **Error Handling**  
  Catches and displays messages for:  
  - `InvalidBloodTypeException`  
  - `InvalidStaffOperationException`

- **Input Management**  
  Uses a shared `Scanner` passed from the caller without closing `System.in` to maintain continuous input.  


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


Implements a text‐based menu for staff to authenticate and manage blood bank inventory.

- **Purpose**  
  Enables staff users to securely log in, check current blood stock levels, update inventory quantities, and log out.

- **Authentication**  
  - Uses `staffID` and `password`  
  - Verifies credentials via `DatabaseManager.validateStaff`

- **Main Menu**  
  1. **Check Inventory**  
     - Prompts for blood type  
     - Retrieves and displays available units  
     - Handles invalid blood‐type errors  
  2. **Update Inventory**  
     - Prompts for blood type and new quantity  
     - Updates database stock  
     - Prevents negative quantities and reports errors  
  3. **Logout**  
     - Exits the menu loop

- **Error Handling**  
  Catches and displays messages for:
  - `InvalidBloodTypeException`  
  - `InvalidStaffOperationException`

- **Input Management**  
  Uses a single `Scanner` instance without closing `System.in` to support continuous user interaction.  









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
