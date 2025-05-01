// User.java
public abstract class User {
    protected String name;
    protected String bloodType;

    public User(String name, String bloodType) {
        this.name = name;
        this.bloodType = bloodType.toUpperCase();
    }

    public abstract void showMenu(DatabaseManager db);
}
