package Vue.Components;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Vue.Components.DataTable.StudentRow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

// Panneau qui calcule et affiche les statistiques des eleves.
public class StatisticsPanel {
    private final String title;
    private final String backgroundColor;
    private final String textColor;
    private List<StudentRow> rows;

    public StatisticsPanel(String title) {
        this(title, "#FFFFFF", "#1A1A1A");
    }

    public StatisticsPanel(String title, String backgroundColor, String textColor) {
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
        this.rows = new ArrayList<>();
    }

    public void setRows(List<StudentRow> rows) {
        this.rows = rows == null ? new ArrayList<>() : new ArrayList<>(rows);
    }

    public VBox build() {
        SummaryStats stats = computeStats();

        VBox root = new VBox(14);
        root.setPadding(new Insets(16));
        root.setAlignment(Pos.TOP_LEFT);
        root.setStyle(
            "-fx-background-color: " + backgroundColor + ";"
                + "-fx-border-color: #D1D9E2;"
                + "-fx-border-radius: 10;"
                + "-fx-background-radius: 10;"
        );

        Label titleLabel = new LabelCustom(title, 22, textColor, true).build();
        Label subtitleLabel = new LabelCustom("Resume des donnees affichees", 13, "#64748B", false).build();

        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(12);

        grid.add(buildCard("Total eleves", String.valueOf(stats.total)), 0, 0);
        grid.add(buildCard("Moyenne classe", formatDouble(stats.average)), 1, 0);
        grid.add(buildCard("Admis (>=10)", String.valueOf(stats.passed)), 0, 1);
        grid.add(buildCard("Echec (<10)", String.valueOf(stats.failed)), 1, 1);
        grid.add(buildCard("< 18 ans", String.valueOf(stats.ageUnder18)), 0, 2);
        grid.add(buildCard("18 a 20 ans", String.valueOf(stats.age18To20)), 1, 2);
        grid.add(buildCard("21 ans et +", String.valueOf(stats.age21Plus)), 0, 3);
        grid.add(buildCard("Excellents (>=16)", String.valueOf(stats.excellent)), 1, 3);
        grid.add(buildCard("Min / Max", formatDouble(stats.minGrade) + " / " + formatDouble(stats.maxGrade)), 0, 4);

        PieChart agePieChart = buildAgePieChart(stats);

        root.getChildren().addAll(titleLabel, subtitleLabel, grid, agePieChart);
        return root;
    }

    private PieChart buildAgePieChart(SummaryStats stats) {
        ObservableList<PieChart.Data> pieData;

        if (stats.total == 0) {
            pieData = FXCollections.observableArrayList(
                new PieChart.Data("Aucune donnee", 1)
            );
        } else {
            pieData = FXCollections.observableArrayList(
                new PieChart.Data("< 18 ans", stats.ageUnder18),
                new PieChart.Data("18 a 20 ans", stats.age18To20),
                new PieChart.Data("21 ans et +", stats.age21Plus)
            );
        }

        PieChart chart = new PieChart(pieData);
        chart.setTitle("Repartition par tranche d'age");
        chart.setLegendVisible(true);
        chart.setLabelsVisible(true);
        chart.setClockwise(true);
        chart.setPrefHeight(280);
        return chart;
    }

    private VBox buildCard(String labelText, String valueText) {
        VBox card = new VBox(6);
        card.setPrefWidth(240);
        card.setPadding(new Insets(10));
        card.setStyle(
            "-fx-background-color: #F8FAFC;"
                + "-fx-border-color: #E2E8F0;"
                + "-fx-border-radius: 8;"
                + "-fx-background-radius: 8;"
        );

        Label label = new LabelCustom(labelText, 12, "#475569", false).build();
        Label value = new LabelCustom(valueText, 20, "#0F172A", true).build();

        card.getChildren().addAll(label, value);
        return card;
    }

    private SummaryStats computeStats() {
        SummaryStats stats = new SummaryStats();
        stats.total = rows.size();

        if (rows.isEmpty()) {
            stats.minGrade = 0.0;
            stats.maxGrade = 0.0;
            return stats;
        }

        double sum = 0.0;
        stats.minGrade = Double.MAX_VALUE;
        stats.maxGrade = -Double.MAX_VALUE;

        for (StudentRow row : rows) {
            double grade = row.getGrade();
            sum += grade;

            if (grade >= 10.0) {
                stats.passed++;
            } else {
                stats.failed++;
            }

            if (grade >= 16.0) {
                stats.excellent++;
            }

            if (row.getAge() < 18) {
                stats.ageUnder18++;
            } else if (row.getAge() <= 20) {
                stats.age18To20++;
            } else {
                stats.age21Plus++;
            }

            if (grade < stats.minGrade) {
                stats.minGrade = grade;
            }
            if (grade > stats.maxGrade) {
                stats.maxGrade = grade;
            }
        }

        stats.average = sum / rows.size();
        return stats;
    }

    private String formatDouble(double value) {
        return String.format(Locale.ROOT, "%.2f", value);
    }

    private static class SummaryStats {
        private int total;
        private int passed;
        private int failed;
        private int ageUnder18;
        private int age18To20;
        private int age21Plus;
        private int excellent;
        private double average;
        private double minGrade;
        private double maxGrade;
    }

}
