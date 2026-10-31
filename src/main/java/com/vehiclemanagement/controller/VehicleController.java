package com.vehiclemanagement.controller;

import com.vehiclemanagement.model.Vehicle;
import com.vehiclemanagement.service.VehicleService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;

public class VehicleController {
    private final VehicleService service;
    private final ObservableList<Vehicle> rows = FXCollections.observableArrayList();
    private TableView<Vehicle> table;
    private Label totalLabel, availableLabel, inUseLabel;
    private TextField numberField, nameField, yearField, rateField, searchField;
    private ComboBox<String> typeBox, statusBox;

    public VehicleController(VehicleService service) {
        this.service = service;
    }

    public Parent createView() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f3f6fb;");
        root.setTop(header());
        root.setCenter(mainContent());
        refresh();
        return root;
    }

    private VBox header() {
        Label title = new Label("VEHICLE MANAGEMENT SYSTEM");
        title.setFont(Font.font("Arial", 25));
        title.setTextFill(Color.WHITE);
        Label sub = new Label("Manage your vehicles in one place");
        sub.setTextFill(Color.web("#dbeafe"));
        VBox box = new VBox(7, title, sub);
        box.setPadding(new Insets(22, 30, 22, 30));
        box.setStyle("-fx-background-color: #172554;");
        return box;
    }

    private VBox mainContent() {
        VBox content = new VBox(18);
        content.setPadding(new Insets(24));
        content.getChildren().addAll(dashboard(), formPanel(), tablePanel());
        return content;
    }

    private HBox dashboard() {
        totalLabel = new Label("0");
        availableLabel = new Label("0");
        inUseLabel = new Label("0");
        HBox cards = new HBox(15,
            statCard("TOTAL VEHICLES", totalLabel, "#1d4ed8"),
            statCard("AVAILABLE", availableLabel, "#15803d"),
            statCard("IN USE", inUseLabel, "#b45309"));
        cards.setAlignment(Pos.CENTER);
        return cards;
    }

    private VBox statCard(String heading, Label value, String color) {
        Label title = new Label(heading);
        title.setTextFill(Color.web("#64748b"));
        title.setFont(Font.font(12));
        value.setFont(Font.font("Arial", 27));
        value.setTextFill(Color.web(color));
        VBox card = new VBox(8, title, value);
        card.setPadding(new Insets(16));
        card.setMinWidth(190);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                      "-fx-border-color: #e2e8f0; -fx-border-radius: 10;");
        HBox.setHgrow(card, Priority.ALWAYS);
        return card;
    }

    private VBox formPanel() {
        Label heading = new Label("Vehicle Details");
        heading.setFont(Font.font("Arial", 18));
        numberField = new TextField(); numberField.setPromptText("TN01AB1234");
        nameField = new TextField(); nameField.setPromptText("Vehicle name");
        typeBox = new ComboBox<>(FXCollections.observableArrayList("Car", "Bike", "SUV", "Van", "Bus", "Truck"));
        typeBox.setPromptText("Select type");
        yearField = new TextField(); yearField.setPromptText("2024");
        statusBox = new ComboBox<>(FXCollections.observableArrayList("Available", "In Use", "Under Maintenance"));
        statusBox.setValue("Available");
        rateField = new TextField(); rateField.setPromptText("Daily rate (₹)");

        GridPane grid = new GridPane();
        grid.setHgap(12); grid.setVgap(9);
        addField(grid, "Vehicle Number", numberField, 0, 0);
        addField(grid, "Vehicle Name", nameField, 1, 0);
        addField(grid, "Vehicle Type", typeBox, 2, 0);
        addField(grid, "Model Year", yearField, 0, 1);
        addField(grid, "Status", statusBox, 1, 1);
        addField(grid, "Daily Rate", rateField, 2, 1);
        for (int i = 0; i < 3; i++) GridPane.setHgrow(grid.getChildren().get(i), Priority.ALWAYS);

        Button add = button("Add Vehicle", "#2563eb");
        Button update = button("Update Selected", "#0f766e");
        Button delete = button("Delete Selected", "#dc2626");
        Button clear = button("Clear", "#64748b");
        add.setOnAction(e -> addVehicle());
        update.setOnAction(e -> updateVehicle());
        delete.setOnAction(e -> deleteVehicle());
        clear.setOnAction(e -> clearForm());
        HBox actions = new HBox(10, add, update, delete, clear);

        VBox panel = new VBox(13, heading, grid, actions);
        panel.setPadding(new Insets(18));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                       "-fx-border-color: #e2e8f0; -fx-border-radius: 10;");
        return panel;
    }

    private void addField(GridPane grid, String label, Control control, int col, int row) {
        VBox field = new VBox(5, new Label(label), control);
        control.setMaxWidth(Double.MAX_VALUE);
        grid.add(field, col, row);
    }

    private VBox tablePanel() {
        Label heading = new Label("Vehicle Records");
        heading.setFont(Font.font("Arial", 18));
        searchField = new TextField();
        searchField.setPromptText("Search by number, name or type...");
        searchField.textProperty().addListener((obs, oldValue, newValue) -> filter(newValue));

        table = new TableView<>();
        table.setItems(rows);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(260);
        addColumn("Vehicle Number", "number");
        addColumn("Name", "name");
        addColumn("Type", "type");
        addColumn("Year", "year");
        addColumn("Status", "status");
        TableColumn<Vehicle, String> rate = new TableColumn<>("Daily Rate (₹)");
        rate.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            String.format("%.2f", data.getValue().getDailyRate())));
        table.getColumns().add(rate);
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, selected) -> {
            if (selected != null) fillForm(selected);
        });

        VBox panel = new VBox(12, heading, searchField, table);
        panel.setPadding(new Insets(18));
        panel.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                       "-fx-border-color: #e2e8f0; -fx-border-radius: 10;");
        return panel;
    }

    private void addColumn(String heading, String property) {
        TableColumn<Vehicle, String> column = new TableColumn<>(heading);
        column.setCellValueFactory(data -> {
            Vehicle v = data.getValue();
            String value = switch (property) {
                case "number" -> v.getNumber();
                case "name" -> v.getName();
                case "type" -> v.getType();
                case "year" -> String.valueOf(v.getYear());
                default -> v.getStatus();
            };
            return new javafx.beans.property.SimpleStringProperty(value);
        });
        table.getColumns().add(column);
    }

    private Button button(String text, String color) {
        Button button = new Button(text);
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-background-color: " + color + "; -fx-background-radius: 6; -fx-padding: 9 15;");
        return button;
    }

    private void addVehicle() {
        try {
            service.add(readForm());
            refresh(); clearForm();
            message("Vehicle added successfully.");
        } catch (Exception ex) { message(ex.getMessage()); }
    }

    private void updateVehicle() {
        Vehicle selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) { message("Select a vehicle from the table first."); return; }
        try {
            service.update(selected.getNumber(), readForm());
            refresh(); clearForm();
            message("Vehicle updated successfully.");
        } catch (Exception ex) { message(ex.getMessage()); }
    }

    private void deleteVehicle() {
        Vehicle selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) { message("Select a vehicle from the table first."); return; }
        service.delete(selected.getNumber());
        refresh(); clearForm();
        message("Vehicle deleted successfully.");
    }

    private Vehicle readForm() {
        String number = numberField.getText().trim();
        String name = nameField.getText().trim();
        String type = typeBox.getValue() == null ? "" : typeBox.getValue();
        String status = statusBox.getValue() == null ? "Available" : statusBox.getValue();
        int year = Integer.parseInt(yearField.getText().trim());
        double rate = Double.parseDouble(rateField.getText().trim());
        if (number.isBlank() || name.isBlank() || type.isBlank())
            throw new IllegalArgumentException("Please fill in all vehicle details.");
        return new Vehicle(number, name, type, year, status, rate);
    }

    private void fillForm(Vehicle v) {
        numberField.setText(v.getNumber());
        nameField.setText(v.getName());
        typeBox.setValue(v.getType());
        yearField.setText(String.valueOf(v.getYear()));
        statusBox.setValue(v.getStatus());
        rateField.setText(String.valueOf(v.getDailyRate()));
    }

    private void clearForm() {
        numberField.clear(); nameField.clear(); yearField.clear(); rateField.clear();
        typeBox.setValue(null); statusBox.setValue("Available");
        if (table != null) table.getSelectionModel().clearSelection();
    }

    private void refresh() {
        List<Vehicle> all = service.getAll();
        rows.setAll(all);
        if (totalLabel != null) {
            totalLabel.setText(String.valueOf(all.size()));
            availableLabel.setText(String.valueOf(all.stream().filter(v -> v.getStatus().equals("Available")).count()));
            inUseLabel.setText(String.valueOf(all.stream().filter(v -> v.getStatus().equals("In Use")).count()));
        }
    }

    private void filter(String query) {
        String q = query == null ? "" : query.toLowerCase().trim();
        rows.setAll(service.getAll().stream().filter(v ->
            v.getNumber().toLowerCase().contains(q) ||
            v.getName().toLowerCase().contains(q) ||
            v.getType().toLowerCase().contains(q)).toList());
    }

    private void message(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Vehicle Management System");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
