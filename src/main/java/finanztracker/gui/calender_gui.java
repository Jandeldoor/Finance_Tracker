package finanztracker.gui;

import javafx.util.StringConverter;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class calender_gui {


    // Methode zum Öffnen des neuen Fensters, in dem der Benutzer den Zeitraum auswählt
    public static void openDateSelectionWindow(TextField selectedDate) {
        // Erstelle das neue Fenster
        Stage secondaryStage = new Stage();
        secondaryStage.initModality(Modality.APPLICATION_MODAL); // Verhindert, dass der Benutzer das Hauptfenster nutzt, während das Popup geöffnet ist
        secondaryStage.setTitle("Zeitraum");

        // Erstelle zwei DatePicker für Start- und Enddatum
        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");


        convertDates(startDatePicker, formatter);

        convertDates(endDatePicker, formatter);

        // Button, um den Zeitraum zu bestätigen
        Button confirmButton = new Button("Zeitraum bestätigen");
        confirmButton.setOnAction(e -> {

            // Hole die ausgewählten Daten
            String startDate = startDatePicker.getValue() != null ? startDatePicker.getValue().format(formatter) : "Nicht ausgewählt";
            String endDate = endDatePicker.getValue() != null ? endDatePicker.getValue().format(formatter) : "Nicht ausgewählt";

            // Ausgabe der ausgewählten Daten (dies kann später durch Logik zum Filtern von Daten ersetzt werden)
            System.out.println("Startdatum: " + startDate + ", Enddatum: " + endDate);

            main_gui.dates[0] = startDatePicker.getValue();
            main_gui.dates[1] = endDatePicker.getValue();


            selectedDate.setText(startDate + " - " + endDate);
            secondaryStage.close(); // Schließe das Popup
        });

        // Layout für das Auswahl-Fenster
        Pane pane = new Pane(startDatePicker, endDatePicker, confirmButton);
        startDatePicker.setLayoutX(0);
        startDatePicker.setLayoutY(0);

        endDatePicker.setLayoutX(0);
        endDatePicker.setLayoutY(30);

        confirmButton.setLayoutX(0);
        confirmButton.setLayoutY(100);

        pane.getStyleClass().add("pane-background");
        startDatePicker.getStyleClass().add("date-picker");
        endDatePicker.getStyleClass().add("date-picker");

        Scene scene = new Scene(pane, 200, 150);
        scene.getStylesheets().add(Objects.requireNonNull(calender_gui.class.getResource("/calender_style.css")).toExternalForm());
        secondaryStage.setScene(scene);
        secondaryStage.setResizable(false);
        secondaryStage.show();
    }

    private static void convertDates(DatePicker datePicker, DateTimeFormatter formatter) {
        datePicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? formatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String text) {
                return (text != null && !text.isEmpty()) ? LocalDate.parse(text, formatter) : null;
            }
        });
    }
}
