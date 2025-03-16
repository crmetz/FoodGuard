package com.app.foodguard.controller;

import com.app.foodguard.model.Alimento;
import com.app.foodguard.service.AlimentoService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class AlimentoController {
    @FXML private TableView<Alimento> foodTable;
    @FXML private TableColumn<Alimento, String> nameColumn;
    @FXML private TableColumn<Alimento, Integer> quantityColumn;
    @FXML private TableColumn<Alimento, String> categoryColumn;
    @FXML private TextField nameField;
    @FXML private TextField quantityField;
    @FXML private TextField categoryField;
    @FXML private Button addButton;
    @FXML private Button deleteButton;

    private AlimentoService alimentoService;
    private ObservableList<Alimento> foodObservableList;

    public void initialize() {
        // Initialize the service
        alimentoService = new AlimentoService();

        // Fetch all foods, ensuring it's not null
        foodObservableList = FXCollections.observableArrayList();
        if (alimentoService.getAllFoods() != null) {
            foodObservableList.addAll(alimentoService.getAllFoods());
        }

        // Set cell value factories for table columns
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        quantityColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getQuantity()));
        categoryColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCategory()));

        // Set the table items to the observable list
        foodTable.setItems(foodObservableList);
    }

    @FXML
    private void handleAddFood() {
        String name = nameField.getText();
        int quantity = Integer.parseInt(quantityField.getText());
        String category = categoryField.getText();

        // Create a new food item
        Alimento newFood = new Alimento(name, quantity, category);

        // Add the new food to the service and observable list
        alimentoService.addFood(newFood);
        foodObservableList.add(newFood);

        // Clear the input fields
        nameField.clear();
        quantityField.clear();
        categoryField.clear();
    }

    @FXML
    private void handleDeleteFood(MouseEvent event) {
        // Get the selected food item
        Alimento selectedFood = foodTable.getSelectionModel().getSelectedItem();
        if (selectedFood != null) {
            // Remove the food from the service and observable list
            alimentoService.removeFood(selectedFood);
            foodObservableList.remove(selectedFood);
        }
    }
}
