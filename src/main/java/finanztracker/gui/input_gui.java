package finanztracker.gui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import java.util.Date;

import static finanztracker.gui.barchart_gui.getTotalAmount;
import static finanztracker.gui.barchart_gui.updateBarChart;
import static finanztracker.main.DatabaseManager.saveToDatabase;

public class input_gui {

    public static void openInputDialog(String category) {
        Stage dialog = new Stage();
        dialog.setTitle("Gib die " + category + " ein");

        VBox dialogLayout = new VBox(10);
        dialogLayout.setPadding(new javafx.geometry.Insets(20));

        TextField textField = new TextField();
        textField.setPromptText("Betrag");
        textField.setText("0.00 €");
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("^\\d{1,7}(\\.\\d{0,2})? €$")) {
                if (!newValue.endsWith(" €")) {
                    textField.setText(newValue + " €");
                }
            } else {
                textField.setText(oldValue);
            }
        });

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("Miete", "Freizeit", "Sonstiges", "Lebensmittel", "Transport");
        comboBox.setValue("Miete");

        Button btnOk = new Button("Okay");
        Button btnCancel = new Button("Abbrechen");

        btnOk.setOnAction(e -> {
            String inputText = textField.getText();
            String selectedCategory = comboBox.getValue();
            String amountText = inputText.replace(" €", "");
            double amount = Double.parseDouble(amountText);

            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            saveToDatabase(amount, selectedCategory, date, category);
            updateBarChart(getTotalAmount("Einnahmen"), getTotalAmount("Ausgaben"));  // Balkendiagramme aktualisieren

            dialog.close();
        });

        btnCancel.setOnAction(e -> dialog.close());

        dialogLayout.getChildren().addAll(new Label("Betrag für " + category + ":"), textField,
                new Label("kategorie:"), comboBox, btnOk, btnCancel);

        Scene dialogScene = new Scene(dialogLayout, 300, 250);
        dialog.setScene(dialogScene);
        dialog.setResizable(false);
        dialog.showAndWait();
    }
}
