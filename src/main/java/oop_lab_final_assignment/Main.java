package oop_lab_final_assignment;

public class Main 
{
    public static void main(String[] args)
    {
        // Get single instance of IOInterface
        ui.IOInterface io = ui.IOInterface.getInstance();

        // Show the main menu to start the program
        io.mainMenu();
    }
}
