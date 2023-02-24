package carsharing;

public class Main {
    public static void main(String[] args) {
        Database.dbName = args.length > 1 ? args[1] : "defaultDatabase";
        Database.initDatabase();
        Menu.initUI();
    }
}
