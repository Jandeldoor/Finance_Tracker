package finanztracker.main;

import finanztracker.gui.main_gui;
import javafx.application.Application;
import javafx.stage.Stage;


public class finance_manager extends Application {



    @Override
    public void start(Stage stage) throws Exception {

        DatabaseManager.connectToDatabase();

        if (DatabaseManager.getConnection() != null && DatabaseManager.getConnection().isValid(0)) {
            System.out.println("Datenbankverbindung ist aktiv");
        } else {
            System.out.println("Datenbankverbindung ist nicht aktiv");
        }


        main_gui gui = new main_gui(stage);
        gui.createGUI();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
