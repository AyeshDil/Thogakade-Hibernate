package com.thogakade.pos.controller;

import com.jfoenix.controls.JFXButton;
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

    private String searchText="";

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
        ObservableList<ItemTM> tmList = FXCollections.observableArrayList();
        for (Item i : Database.itemTable
        ) {
            if (i.getDescription().contains(text)){

                Button btn = new Button("Delete");
                ItemTM tm = new ItemTM(i.getCode(),i.getDescription(),
                        i.getUnitPrice(), i.getQtyOnHand(),btn);
                tmList.add(tm);


                // Delete btn OnAction
                btn.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Are you sure whether do you want delete item" + i.getCode(),
                            ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();

                    if (buttonType.get() == ButtonType.YES) {
                        boolean isRemoved = Database.itemTable.remove(i);
                        if (isRemoved) {
                            searchItem(searchText);
                            new Alert(Alert.AlertType.CONFIRMATION, "Item deleted").show();
                        } else {
                            new Alert(Alert.AlertType.WARNING, "Try again").show();
                        }
                    }
                });
            }



        }
        tblItem.setItems(tmList);
    }


    public void saveItemOnAction(ActionEvent actionEvent) {
        Item i1 = new Item(
                txtCode.getText(), txtDescription.getText(),
                Double.parseDouble(txtUnitPrice.getText()), Integer.parseInt(txtQtyOnHand.getText()));

        if (btnSaveItem.getText().equalsIgnoreCase("Save Item")) {
            // save code
            boolean isSaved = Database.itemTable.add(i1);

            if (isSaved) {
                searchItem(searchText);
                clear();
                new Alert(Alert.AlertType.CONFIRMATION, "Item Saved").show();
            } else {
                new Alert(Alert.AlertType.WARNING, "Try again").show();
            }
        } else {
            // update code
            for (int i = 0; i < Database.itemTable.size(); i++) {
                if (txtCode.getText().equalsIgnoreCase(Database.itemTable.get(i).getCode())) {
                    Database.itemTable.get(i).setCode(txtCode.getText());
                    Database.itemTable.get(i).setDescription(txtDescription.getText());
                    Database.itemTable.get(i).setUnitPrice(Double.parseDouble(txtUnitPrice.getText()));
                    Database.itemTable.get(i).setQtyOnHand(Integer.parseInt(txtQtyOnHand.getText()));
                    searchItem(searchText);
                    clear();
                    new Alert(Alert.AlertType.INFORMATION, "Item updated").show();
                }
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
