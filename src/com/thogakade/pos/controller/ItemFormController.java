package com.thogakade.pos.controller;

import com.jfoenix.controls.JFXButton;
import com.thogakade.pos.db.DBConnection;
import com.thogakade.pos.db.Database;
import com.thogakade.pos.modal.Customer;
import com.thogakade.pos.modal.Item;
import com.thogakade.pos.view.tm.CustomerTM;
import com.thogakade.pos.view.tm.ItemTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class ItemFormController {
    public AnchorPane ItemFormContext;
    public TextField txtCode;
    public TextField txtDescription;
    public TextField txtUnitPrice;
    public TextField txtQtyOnHand;
    public TextField txtSearch;
    public JFXButton btnSaveItem;
    public TableView<ItemTM> tblItem;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colUnitPrice;
    public TableColumn colQtyOnHand;
    public TableColumn colOptions;

    private String searchText = "";

    public void initialize() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        colOptions.setCellValueFactory(new PropertyValueFactory<>("btn"));

        searchItem(searchText);

        tblItem.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (null != newValue) {
                setData(newValue);
            }
        });

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText = newValue;
            searchItem(searchText);
        });
    }

    private void setData(ItemTM tm) {
        txtCode.setText(tm.getCode());
        txtDescription.setText(tm.getDescription());
        txtUnitPrice.setText(String.valueOf(tm.getUnitPrice()));
        txtQtyOnHand.setText(String.valueOf(tm.getQtyOnHand()));
        btnSaveItem.setText("Update Item");
    }


    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ItemFormContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/DashboardForm.fxml"))));
    }

    public void newItemOnAction(ActionEvent actionEvent) {
        btnSaveItem.setText("Save Item");
    }

    private void searchItem(String text) {
        String searchText = "%" + text + "%";
        try {
            ObservableList<ItemTM> tmList = FXCollections.observableArrayList();
            String sql = "SELECT * FROM Item WHERE description LIKE ?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, searchText);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Button btn = new Button("Delete");
                ItemTM tm = new ItemTM(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getDouble(3),
                        resultSet.getInt(4),
                        btn);
                tmList.add(tm);

                // Delete btn
                btn.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Are you sure whether do you want delete item" + tm.getCode(),
                            ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();

                    if (buttonType.get() == ButtonType.YES) {
                        try {
                            String sql1 = "DELETE FROM Item WHERE code=?";
                            PreparedStatement statement1 = DBConnection.getInstance().getConnection().prepareStatement(sql1);
                            statement1.setString(1, tm.getCode());

                            if (statement1.executeUpdate() > 0) {
                                searchItem(searchText);
                                new Alert(Alert.AlertType.CONFIRMATION, "Item deleted").show();
                            } else {
                                new Alert(Alert.AlertType.WARNING, "Try again").show();
                            }
                        } catch (ClassNotFoundException | SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
            tblItem.setItems(tmList);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }


    public void saveItemOnAction(ActionEvent actionEvent) {
        Item i1 = new Item(
                txtCode.getText(), txtDescription.getText(),
                Double.parseDouble(txtUnitPrice.getText()), Integer.parseInt(txtQtyOnHand.getText()));

        if (btnSaveItem.getText().equalsIgnoreCase("Save Item")) {
            // save code
            try {
                String sql = "INSERT INTO Item VALUES(?,?,?,?)";
                PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                statement.setString(1, i1.getCode());
                statement.setString(2, i1.getDescription());
                statement.setDouble(3, i1.getUnitPrice());
                statement.setInt(4, i1.getQtyOnHand());

                if (statement.executeUpdate() > 0) {
                    searchItem(searchText);
                    clear();
                    new Alert(Alert.AlertType.CONFIRMATION, "Item Saved").show();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try again").show();
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }

        } else {
            // update code
            try {
                String sql = "UPDATE Item SET description=?, unitPrice=?, qtyOnHand=? WHERE code=?";
                PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                statement.setString(1, i1.getDescription());
                statement.setDouble(2, i1.getUnitPrice());
                statement.setInt(3, i1.getQtyOnHand());
                statement.setString(4, i1.getCode());

                if (statement.executeUpdate() > 0) {
                    searchItem(searchText);
                    clear();
                    new Alert(Alert.AlertType.INFORMATION, "Item updated").show();
                }


            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }

        }

    }

    private void clear() {
        txtCode.clear();
        txtDescription.clear();
        txtUnitPrice.clear();
        txtQtyOnHand.clear();
    }

}
