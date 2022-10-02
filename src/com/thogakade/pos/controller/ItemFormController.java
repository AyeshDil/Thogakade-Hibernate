package com.thogakade.pos.controller;

import com.jfoenix.controls.JFXButton;
import com.thogakade.pos.bo.BoFactory;
import com.thogakade.pos.bo.BoTypes;
import com.thogakade.pos.bo.custom.ItemBo;
import com.thogakade.pos.dao.DaoFactory;
import com.thogakade.pos.dao.DaoTypes;
import com.thogakade.pos.dao.custom.ItemDao;
import com.thogakade.pos.dao.custom.impl.ItemDaoImpl;
import com.thogakade.pos.dto.ItemDto;
import com.thogakade.pos.entity.Item;
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
import java.util.ArrayList;
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

    private ItemBo itemBo = BoFactory.getInstance().getBo(BoTypes.ITEM);

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

            ArrayList<ItemDto> iList = itemBo.searchItems(searchText);

            for(ItemDto i: iList) {
                Button btn = new Button("Delete");
                ItemTM tm = new ItemTM(
                        i.getCode(),
                        i.getDescription(),
                        i.getUnitPrice(),
                        i.getQtyOnHand(),
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
                            boolean isItemDeleted = itemBo.deleteItem(searchText);

                            if (isItemDeleted) {
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
                boolean isItemSaved = itemBo.saveItem(
                        new ItemDto(txtCode.getText(),
                                txtDescription.getText(),
                                Double.parseDouble(txtUnitPrice.getText()),
                                Integer.parseInt(txtQtyOnHand.getText()))
                );
                if (isItemSaved) {
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
                boolean isItemUpdated = itemBo.updateItem(
                        new ItemDto(txtCode.getText(),
                                txtDescription.getText(),
                                Double.parseDouble(txtUnitPrice.getText()),
                                Integer.parseInt(txtQtyOnHand.getText()))
                );
                if (isItemUpdated) {
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
