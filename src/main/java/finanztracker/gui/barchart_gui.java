package finanztracker.gui;

import finanztracker.main.DatabaseManager;
import javafx.application.Platform;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static finanztracker.gui.main_gui.*;

public class barchart_gui {

    private static XYChart.Series<String, Number> incomeSeries;
    private static XYChart.Series<String, Number> expenseSeries;

    public static void createBarCharts(Pane pane) {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Kategorie");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Betrag (€)");

        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setLayoutX(200);
        barChart.setLayoutY(50);
        barChart.setPrefSize(550, 400);

        barChart.getStyleClass().add("bar-chart-background");

        incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Einnahmen");

        expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Ausgaben");

        barChart.getData().addAll(incomeSeries, expenseSeries);
        pane.getChildren().add(barChart);

        updateBarChart(getTotalAmount("Einnahmen"), getTotalAmount("Ausgaben")); // Beim Start die Daten laden
    }


    public static void updateBarChart(double totalIncome, double totalExpense) {

        incomeSeries.getData().clear();
        XYChart.Data<String, Number> incomeData = new XYChart.Data<>("Einnahmen", totalIncome);
        incomeSeries.getData().add(incomeData);


        expenseSeries.getData().clear();
        XYChart.Data<String, Number> expenseData = new XYChart.Data<>("Ausgaben", totalExpense);
        expenseSeries.getData().add(expenseData);

        addHoverEffect(incomeSeries, "+");
        addHoverEffect(expenseSeries, "-");

        Platform.runLater(() -> {
            for (XYChart.Data<String, Number> data : incomeSeries.getData()) {
                if (data.getNode() != null) {
                    data.getNode().getStyleClass().add("bar-income"); // CSS-Klasse für Einnahmen
                }
            }
            for (XYChart.Data<String, Number> data : expenseSeries.getData()) {
                if (data.getNode() != null) {
                    data.getNode().getStyleClass().add("bar-expense"); // CSS-Klasse für Ausgaben
                }
            }
        });

        getCurrentFinancialSituation(currentFinancialSituation, totalIncome-totalExpense);
    }

    // Methode für Hover-Effekt mit Schatten
    private static void addHoverEffect(XYChart.Series<String, Number> series, String inout) {

        if(inout.equals("+")) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                Tooltip tooltip = new Tooltip("+" + data.getYValue().toString() + " €");
                addToolTip(data, tooltip);
            }
        }else if(inout.equals("-")) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                Tooltip tooltip = new Tooltip("-" + data.getYValue().toString() + " €");
                addToolTip(data, tooltip);
            }
        }



    }

    private static void addToolTip(XYChart.Data<String, Number> data, Tooltip tooltip) {
        Tooltip.install(data.getNode(), tooltip);

        data.getNode().setOnMouseEntered(event -> {
            DropShadow shadow = new DropShadow();
            shadow.setColor(Color.WHITE);
            shadow.setRadius(8); // Größe des Schattens
            data.getNode().setEffect(shadow);
        });

        data.getNode().setOnMouseExited(event -> data.getNode().setEffect(null)); // Schatten entfernen
    }


    public static double getTotalAmount(String tableName) {
        String sql = "SELECT SUM(betrag) FROM " + tableName;
        try (PreparedStatement pstmt = DatabaseManager.getConnection().prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
