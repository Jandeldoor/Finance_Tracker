package finanztracker.gui;

import finanztracker.main.DatabaseManager;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

import static finanztracker.gui.barchart_gui.updateBarChart;

public class clearData_gui {

    public static void openDataClearWindow() {

        Stage dataClear = new Stage();
        dataClear.setTitle("Alle Daten Löschen?");
        dataClear.setResizable(false);


        Pane pane = new Pane();
        pane.getStyleClass().add("pane-background");

        TextArea textField = new TextArea();
        textField.setText("Möchten Sie wirklich alle \nDaten unwiderruflich Löschen?");
        textField.setEditable(false);
        textField.getStyleClass().add("text-area");
        textField.setPrefHeight(50);
        textField.setPrefWidth(190);
        textField.setLayoutX(5);
        textField.setLayoutY(10);

        Button okButton = new Button();
        new button_gui(okButton, "Löschen!", "button", 105, 125, 90, 20, pane);
        okButton.setId("erase-button");
        okButton.setOnAction(e ->{
            deleteAllData();
            dataClear.close();
        });


        Button cancleButton = new Button();
        new button_gui(cancleButton, "Abbrechen", "button", 5, 125, 90, 20, pane);
        cancleButton.setOnAction(e -> dataClear.close());

        pane.getChildren().addAll(textField);
        Scene scene = new Scene(pane, 200, 150);
        scene.getStylesheets().add(Objects.requireNonNull(clearData_gui.class.getResource("/clear_gui_style.css")).toExternalForm());
        dataClear.setScene(scene);
        dataClear.setResizable(false);
        dataClear.showAndWait();

    }


    private static void deleteAllData() {

        String deleteIncome = "DELETE FROM Einnahmen";
        String deleteExpense = "DELETE FROM Ausgaben";

        try (PreparedStatement ptsmtDeleteIncome = DatabaseManager.getConnection().prepareStatement(deleteIncome); PreparedStatement ptsmtDeleteExpense = DatabaseManager.getConnection().prepareStatement(deleteExpense);) {
            ptsmtDeleteExpense.executeUpdate();
            ptsmtDeleteIncome.executeUpdate();

            updateBarChart(0,0);
        }catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
