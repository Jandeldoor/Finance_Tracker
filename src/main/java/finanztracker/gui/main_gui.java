package finanztracker.gui;

import finanztracker.main.DatabaseManager;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;

import static finanztracker.gui.barchart_gui.*;
import static finanztracker.gui.calender_gui.openDateSelectionWindow;
import static finanztracker.gui.clearData_gui.openDataClearWindow;
import static finanztracker.gui.input_gui.openInputDialog;

public class main_gui {

    private final Stage stage;
    private Pane pane;

    public static LocalDate[] dates = new LocalDate[2];

    private boolean rentFilter = false, foodFilter = false, transportFilter = false, leisureFilter = false, otherFilter = false;

    public static TextField currentFinancialSituation;

    public static double currentMoney = getTotalAmount("Einnahmen")-getTotalAmount("Ausgaben");

    public main_gui(Stage stage) {
        this.stage = stage;
    }


    public void createGUI() {
        pane = new Pane();
        pane.getStyleClass().add("pane-background");

        currentFinancialSituation = new TextField();
        currentFinancialSituation.getStyleClass().add("text");
        currentFinancialSituation.setFont(new Font("Arial", 20));
        currentFinancialSituation.setEditable(false);
        currentFinancialSituation.setPrefWidth(150);
        currentFinancialSituation.setLayoutX(600);
        currentFinancialSituation.setLayoutY(412);


        //Erstelle alle Buttons
        createButtons();

        // Füge Balkendiagramme hinzu
        createBarCharts(pane);


        //Erstelle Checkboxen und Textfelder um Filter zu wählen
        createFilterBoxes(pane);


        //Erstellt das Textfeld mit aktuellem Finanz plus oder minus
        getCurrentFinancialSituation(currentFinancialSituation ,currentMoney);

        pane.getChildren().addAll(currentFinancialSituation);

        createScene(pane);
    }

    public static void getCurrentFinancialSituation(TextField tf, double currentMoney) {

        if(currentMoney >=0) {
            tf.setStyle("-fx-text-fill: lime;");
            tf.setText("+" + currentMoney);
        }else {
            tf.setStyle("-fx-text-fill: firebrick;");
            tf.setText("" + currentMoney);
        }
    }


    private void createButtons() {
        Button close = new Button();
        FontIcon powerIcon = new FontIcon(FontAwesomeSolid.POWER_OFF);
        powerIcon.setId("off-button");
        new button_gui(close, powerIcon, 2, 2,20, pane);

        Button calender = new Button();
        FontIcon calenderIcon = new FontIcon(FontAwesomeSolid.CALENDAR_ALT);
        calenderIcon.setId("filter-button");
        new button_gui(calender, calenderIcon, 32, 260,20, pane);
        addButtonHoverEffect(calender, "Wähle ein Zeitraum");
        TextField selectedDate = new TextField("Gesamt");
        selectedDate.setLayoutX(60);
        selectedDate.setLayoutY(267);
        selectedDate.setEditable(false);
        selectedDate.setFont(new Font("Arial", 14));
        selectedDate.getStyleClass().add("text");
        pane.getChildren().add(selectedDate);

        Button resetCalender = new Button();
        FontIcon resetCalenderIcon = new FontIcon(FontAwesomeSolid.SYNC);
        resetCalenderIcon.setId("reset-button");
        new button_gui(resetCalender, resetCalenderIcon, 4, 261,20, pane);
        addButtonHoverEffect(resetCalender, "Setze den Zeitraum zurück auf Gesamt");

        Button search = new Button();
        FontIcon searchIcon = new FontIcon(FontAwesomeSolid.SEARCH);
        searchIcon.setId("search-button");
        new button_gui(search, searchIcon, 9, 465,20, pane);
        addButtonHoverEffect(search, "Filter anwenden");


        Button incomePlus = new Button();
        FontIcon incomePlusIcon = new FontIcon(FontAwesomeSolid.PLUS_CIRCLE);
        incomePlusIcon.setId("income-button");
        new button_gui(incomePlus, incomePlusIcon, 50, 100,50, pane);
        addButtonHoverEffect(incomePlus, "Füge Einnahmen hinzu");

        Button spendingMinus = new Button();
        FontIcon spendingMinusIcon = new FontIcon(FontAwesomeSolid.MINUS_CIRCLE);
        spendingMinusIcon.setId("spending-button");
        new button_gui(spendingMinus, spendingMinusIcon, 50, 180,50, pane);
        addButtonHoverEffect(spendingMinus, "Füge Ausgaben hinzu");


        Button clearAllData = new Button();
        FontIcon clearData = new FontIcon(FontAwesomeSolid.EXCLAMATION_TRIANGLE);
        clearData.setId("clear-button");
        new button_gui(clearAllData, clearData, 760, 0,20, pane);
        addButtonHoverEffect(clearAllData,"Setze alle Daten zurück! Achtung nicht wiederrufbar!");


        // Füge Event-Handler zu den Buttons hinzu
        incomePlus.setOnAction(e -> openInputDialog("Einnahmen"));
        spendingMinus.setOnAction(e -> openInputDialog("Ausgaben"));
        calender.setOnAction(event -> openDateSelectionWindow(selectedDate));
        resetCalender.setOnAction(event -> selectedDate.setText("Gesamt"));
        search.setOnAction(event -> applyFilters());
        clearAllData.setOnAction(event -> openDataClearWindow());
        close.setOnAction(e -> stage.close());


    }

    private void createScene(Pane pane) {
        Scene scene = new Scene(pane,800, 500);
        Image Icon = new Image(Objects.requireNonNull(getClass().getResource("/Icon.png")).toExternalForm());

        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/main_gui_style.css")).toExternalForm());
        stage.getIcons().add(Icon);
        stage.setScene(scene);
        stage.setTitle("Finanztracker");
        stage.setResizable(false);
        stage.show();
    }

    // Methode für Hover-Effekt mit Schatten
    private void addButtonHoverEffect(Button button, String text) {
            Tooltip tooltip = new Tooltip(text);
            tooltip.setFont(new Font("Arial", 12));
            Tooltip.install(button, tooltip);
    }

    private void addFilterHoverEffect(FontIcon icon, String text) {
        Tooltip tooltip = new Tooltip(text);
        tooltip.setFont(new Font("Arial", 12));
        Tooltip.install(icon, tooltip);
    }

    private void createFilterBoxes(Pane pane) {

        //Checkbox Rent Filter
        FontIcon rentIcon = new FontIcon(FontAwesomeSolid.HOUSE_USER);
        rentIcon.setIconSize(20);
        rentIcon.setFill(Color.rgb(139, 69, 19));
        CheckBox rentCheckBox = new CheckBox("Miete");
        rentIcon.setLayoutX(20);
        rentIcon.setLayoutY(340);
        rentCheckBox.setLayoutX(45);
        rentCheckBox.setLayoutY(325);
        rentCheckBox.setTextFill(Color.WHITE);
        rentCheckBox.getStyleClass().add("check-box");


        //Checkbox Food Filter
        FontIcon foodIcon = new FontIcon(FontAwesomeSolid.APPLE_ALT);
        foodIcon.setIconSize(20);
        foodIcon.setFill(Color.DARKRED);
        CheckBox foodCheckBox = new CheckBox("Lebensmittel");
        foodIcon.setLayoutX(20);
        foodIcon.setLayoutY(370);
        foodCheckBox.setLayoutX(45);
        foodCheckBox.setLayoutY(355);
        foodCheckBox.setTextFill(Color.WHITE);
        foodCheckBox.getStyleClass().add("check-box");


        //Checkbox Transport Filter
        FontIcon transportIcon = new FontIcon(FontAwesomeSolid.TRAIN);
        transportIcon.setIconSize(20);
        transportIcon.setFill(Color.BLUE);
        CheckBox transportCheckbox = new CheckBox("Transport");
        transportIcon.setLayoutX(20);
        transportIcon.setLayoutY(400);
        transportCheckbox.setLayoutX(45);
        transportCheckbox.setLayoutY(385);
        transportCheckbox.setTextFill(Color.WHITE);
        transportCheckbox.getStyleClass().add("check-box");

        //Checkbox Freizeit Filter
        FontIcon leisureIcon = new FontIcon(FontAwesomeSolid.BASKETBALL_BALL);
        leisureIcon.setIconSize(20);
        leisureIcon.setFill(Color.ORANGE);
        CheckBox leisureCheckbox = new CheckBox("Freizeit");
        leisureIcon.setLayoutX(20);
        leisureIcon.setLayoutY(430);
        leisureCheckbox.setLayoutX(45);
        leisureCheckbox.setLayoutY(415);
        leisureCheckbox.setTextFill(Color.WHITE);
        leisureCheckbox.getStyleClass().add("check-box");


        //Checkbox sonstige Filter
        FontIcon otherIcon = new FontIcon(FontAwesomeSolid.WALLET);
        otherIcon.setIconSize(20);
        otherIcon.setFill(Color.GRAY);
        CheckBox otherCheckbox = new CheckBox("Sonstiges");
        otherIcon.setLayoutX(20);
        otherIcon.setLayoutY(460);
        otherCheckbox.setLayoutX(45);
        otherCheckbox.setLayoutY(445);
        otherCheckbox.setTextFill(Color.WHITE);
        otherCheckbox.getStyleClass().add("check-box");



        pane.getChildren().addAll(rentIcon, rentCheckBox, foodIcon, foodCheckBox, transportIcon, transportCheckbox, leisureIcon, leisureCheckbox, otherIcon, otherCheckbox);


        //Tooltips Filter
        addFilterHoverEffect(rentIcon, "Filter: Miete");
        addFilterHoverEffect(foodIcon, "Filter: Lebensmittel");
        addFilterHoverEffect(transportIcon, "Filter: Transport");
        addFilterHoverEffect(leisureIcon, "Filter: Freizeit");
        addFilterHoverEffect(otherIcon, "Filter: Sonstiges");


        //EventHandler Checkbox
        rentCheckBox.setOnAction(event -> rentFilter = !rentFilter);
        foodCheckBox.setOnAction(event -> foodFilter = !foodFilter);
        transportCheckbox.setOnAction(event -> transportFilter = !transportFilter);
        leisureCheckbox.setOnAction(event -> leisureFilter = !leisureFilter);
        otherCheckbox.setOnAction(event -> otherFilter = !otherFilter);
    }


    private void applyFilters() {
        LocalDate startDate = dates[0];
        LocalDate endDate = dates[1];

        // Einnahmen Abfrage
        StringBuilder incomeQuery = new StringBuilder("SELECT SUM(e.betrag) AS totalIncome FROM Einnahmen e WHERE 1=1");

        if (startDate != null) {
            incomeQuery.append(" AND e.datum >= '").append(startDate).append("'");
        }

        if (endDate != null) {
            incomeQuery.append(" AND e.datum <= '").append(endDate).append("'");
        }



        // Dynamische Filterbedingungen für Einnahmen
        boolean firstCategory = true;
        if (rentFilter) {
            if (firstCategory) {
                incomeQuery.append(" AND e.kategorie = 'Miete'");
                firstCategory = false;
            } else {
                incomeQuery.append(" OR e.kategorie = 'Miete'");
            }
        }
        if (foodFilter) {
            if (firstCategory) {
                incomeQuery.append(" AND e.kategorie = 'Lebensmittel'");
                firstCategory = false;
            } else {
                incomeQuery.append(" OR e.kategorie = 'Lebensmittel'");
            }
        }
        if (transportFilter) {
            if (firstCategory) {
                incomeQuery.append(" AND e.kategorie = 'Transport'");
                firstCategory = false;
            } else {
                incomeQuery.append(" OR e.kategorie = 'Transport'");
            }
        }
        if (leisureFilter) {
            if (firstCategory) {
                incomeQuery.append(" AND e.kategorie = 'Freizeit'");
                firstCategory = false;
            } else {
                incomeQuery.append(" OR e.kategorie = 'Freizeit'");
            }
        }
        if (otherFilter) {
            if (firstCategory) {
                incomeQuery.append(" AND e.kategorie = 'Sonstiges'");
                firstCategory = false;
            } else {
                incomeQuery.append(" OR e.kategorie = 'Sonstiges'");
            }
        }

        // Ausgaben Abfrage
        StringBuilder expenseQuery = new StringBuilder("SELECT SUM(a.betrag) AS totalExpense FROM Ausgaben a WHERE 1=1");

        if (startDate != null) {
            expenseQuery.append(" AND a.datum >= '").append(startDate).append("'");
        }

        if (endDate != null) {
            expenseQuery.append(" AND a.datum <= '").append(endDate).append("'");
        }


        // Dynamische Filterbedingungen für Ausgaben
        firstCategory = true; // Reset für Ausgaben-Filter
        if (rentFilter) {
            if (firstCategory) {
                expenseQuery.append(" AND a.kategorie = 'Miete'");
                firstCategory = false;
            } else {
                expenseQuery.append(" OR a.kategorie = 'Miete'");
            }
        }
        if (foodFilter) {
            if (firstCategory) {
                expenseQuery.append(" AND a.kategorie = 'Lebensmittel'");
                firstCategory = false;
            } else {
                expenseQuery.append(" OR a.kategorie = 'Lebensmittel'");
            }
        }
        if (transportFilter) {
            if (firstCategory) {
                expenseQuery.append(" AND a.kategorie = 'Transport'");
                firstCategory = false;
            } else {
                expenseQuery.append(" OR a.kategorie = 'Transport'");
            }
        }
        if (leisureFilter) {
            if (firstCategory) {
                expenseQuery.append(" AND a.kategorie = 'Freizeit'");
                firstCategory = false;
            } else {
                expenseQuery.append(" OR a.kategorie = 'Freizeit'");
            }
        }
        if (otherFilter) {
            if (firstCategory) {
                expenseQuery.append(" AND a.kategorie = 'Sonstiges'");
                firstCategory = false;
            } else {
                expenseQuery.append(" OR a.kategorie = 'Sonstiges'");
            }
        }


        try (PreparedStatement pstmtIncome = DatabaseManager.getConnection().prepareStatement(incomeQuery.toString());
             ResultSet rsIncome = pstmtIncome.executeQuery()) {

            double totalIncome = 0;
            if (rsIncome.next()) {
                totalIncome = rsIncome.getDouble("totalIncome");
            }

            try (PreparedStatement pstmtExpense = DatabaseManager.getConnection().prepareStatement(expenseQuery.toString());
                 ResultSet rsExpense = pstmtExpense.executeQuery()) {

                double totalExpense = 0;
                if (rsExpense.next()) {
                    totalExpense = rsExpense.getDouble("totalExpense");
                }

                // Update the bar chart with the total income and expenses
                updateBarChart(totalIncome, totalExpense);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
