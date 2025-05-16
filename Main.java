package app;

public class Main {
    public static void main(String[] args) {
        // Get the single instance of IOInterface
        ui.IOInterface io = ui.IOInterface.getInstance();

        // Show the main menu to start the program
        io.mainMenu();
    }
}
