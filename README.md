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

Provides JDBC-based access to the `bloodbank` MySQL schema, with built-in validation and error handling.

- **Purpose**  
  Manages database connection and implements methods to query and update blood inventory, plus staff authentication.

- **Connection Setup**  
  - Loads MySQL driver and connects to `jdbc:mysql://localhost:3306/bloodbank`  
  - Input Credentials
  - Logs success or prints connection errors

- **Blood-Type Validation**  
  Uses `VALID_BLOOD_TYPES = {"A","B","AB","O"}` to guard against invalid inputs.

- **getBloodQuantity(bankID, bloodType)**  
  - Throws `InvalidBloodTypeException` for unrecognized types  
  - Throws `NullPointerException` if the connection is missing  
  - Queries current stock, logs SQL errors, and returns quantity (0 if none)

- **updateBloodQuantity(bankID, bloodType, newQty)**  
  - Validates blood type and non-negative quantity (throws exceptions otherwise)  
  - Executes an update inside a try-with-resources block  
  - Returns `true` if the update succeeds; logs and returns `false` on SQL errors

- **validateStaff(staffID, password)**  
  - Verifies credentials via a SELECT query in a try-with-resources block  
  - Returns `true` on match; logs and returns `false` on SQL errors

- **Error Handling & Resource Management**  
  - Catches and logs `SQLException` or driver errors  
  - Uses try-with-resources to ensure `PreparedStatement` and `ResultSet` are closed  
  - Propagates custom exceptions for invalid operations  

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

Provides a console‐based interface for donors to record blood donations with input validation and error handling.

- **Purpose**  
  Guides a donor through entering a valid donation quantity, updates inventory, and acknowledges the contribution.

- **Fields**  
  - `name`: Donor’s name  
  - `bloodType`: Donor’s blood group

- **Donation Workflow**  
  - Prompts for donation quantity  
  - Validates positive integer input (reports invalid entries)  
  - Retrieves current stock via `DatabaseManager.getBloodQuantity`  
  - Updates stock with `DatabaseManager.updateBloodQuantity`  
  - Confirms new total and thanks the donor  
  - Handles:
    - Non-numeric input (`InputMismatchException`)  
    - Non-positive quantity (`IllegalArgumentException`)  
    - Invalid blood type or update errors (`InvalidBloodTypeException`, `InvalidStaffOperationException`)

- **Main Menu**  
  - **Donate Blood**: launches the donation workflow  
  - **Exit**: thanks the donor and ends the loop  
  - Invalid menu selections and input mismatches prompt retry messages

- **Accessors**  
  - `getName` / `setName`  
  - `getBloodType` / `setBloodType`

- **Input Management**  
  Uses a shared `Scanner` without closing `System.in` to support continuous input and recovery from invalid entries.  

---

### [Recipient.java](Recipient.java)

Provides a console‐based interface for recipients to request blood with validation and error handling.

- **Purpose**  
  Guides a recipient through entering a valid request quantity, checks stock, updates inventory, and confirms readiness.

- **Request Workflow**  
  - Prompts for quantity to request  
  - Validates positive integer input (reports non-numeric or non-positive entries)  
  - Retrieves current stock via `DatabaseManager.getBloodQuantity`  
  - Alerts if insufficient units available  
  - Deducts approved units using `DatabaseManager.updateBloodQuantity`  
  - Confirms remaining stock and readiness message

- **Main Menu**  
  - **Request Blood**: launches the request workflow  
  - **Exit**: thanks the recipient and exits the loop  
  - Invalid menu selections and input mismatches prompt retry messages

- **Error Handling**  
  Catches and displays messages for:  
  - `InputMismatchException` (non-numeric input)  
  - `IllegalArgumentException` (non-positive quantity)  
  - `InvalidBloodTypeException` & `InvalidStaffOperationException` (unexpected errors)

- **Accessors**  
  - `getName` / `setName`  
  - `getBloodType` / `setBloodType`

- **Input Management**  
  Uses a shared `Scanner` passed from the caller without closing `System.in` to allow continuous interaction and recovery from invalid input.  

---

### [Staff.java](Staff.java)

A console‐based interface for staff to authenticate and manage blood inventory with robust input validation.

- **Purpose**  
  Allows staff members to log in, view current stock levels, update inventory quantities, and continue until they choose to logout.

- **Authentication**  
  - Uses `staffID` and `password`  
  - Verifies credentials via `DatabaseManager.validateStaff`

- **Main Menu**  
  1. **Check Blood Inventory**  
     - Prompts for blood type  
     - Retrieves and displays available units, handling invalid‐type errors  
  2. **Update Blood Inventory**  
     - Prompts for blood type and new quantity  
     - Updates stock, handling invalid‐type and negative‐quantity errors  
  3. **Logout**  
     - Ends the menu loop  

- **Error Handling**  
  - Catches `InputMismatchException` for non‐numeric menu or quantity entries  
  - Displays messages for `InvalidBloodTypeException` and `InvalidStaffOperationException`

- **Accessors**  
  - `getStaffID` / `setStaffID`  
  - `getPassword` / `setPassword`

- **Input Management**  
  Uses a dedicated `Scanner` instance without closing `System.in` to allow continuous input and recovery from invalid entries.  

---

### [BloodBankApp.java](BloodBankApp.java)

Entry point for the console‐based Blood Bank Management System, coordinating donor/recipient and staff workflows.

- **Purpose**  
  Presents the main menu and routes users to donor/recipient or staff interfaces, handling overall application flow.

- **Main Menu**  
  1. **Donor/Recipient**  
     - Prompts for name and blood type  
     - Lets user choose role (Donor or Recipient)  
     - Instantiates the appropriate class and invokes its menu  
  2. **Staff Login**  
     - Prompts for staff ID and password  
     - Authenticates via `Staff.login`  
     - On success, enters the staff menu  
     - On failure, reports invalid credentials  
  3. **Exit**  
     - Gracefully terminates the application

- **User Flow**  
  - Continually loops until “Exit” is selected  
  - Delegates to `Donor`, `Recipient`, or `Staff` classes for role‐specific operations  

- **Error Handling**  
  - Catches non‐numeric menu entries (`NumberFormatException`)  
  - Handles unexpected input end (`NoSuchElementException`)  
  - Reports any other exceptions with their messages  

- **Input Management**  
  - Uses a single `Scanner` and trims empty lines  
  - Validates and parses string input to integers safely  
  - Closes the `Scanner` on application exit  

- **Integration**  
  Relies on `DatabaseManager` for persistence and the `Donor`, `Recipient`, and `Staff` classes for domain logic.  

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
