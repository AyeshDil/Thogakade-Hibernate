package com.thogakade.pos.controller;

import com.jfoenix.controls.JFXButton;
import com.thogakade.pos.db.Database;
import com.thogakade.pos.modal.Customer;
import com.thogakade.pos.view.tm.CustomerTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.Optional;

public class CustomerFormController {

    public TextField txtId;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtSalary;
    public TextField txtSearch;
    public TableView<CustomerTM> tblCustomer;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colSalary;
    public TableColumn colOptions;
    public JFXButton btnSaveCustomer;
    public AnchorPane CustomerFromContext;

    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colOptions.setCellValueFactory(new PropertyValueFactory<>("btn"));

        searchCustomer();

        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (null != newValue) {
                setData(newValue);
            }
        });
    }

    private void setData(CustomerTM tm) {
        txtId.setText(tm.getId());
        txtName.setText(tm.getName());
        txtAddress.setText(tm.getAddress());
        txtSalary.setText(String.valueOf(tm.getSalary()));
        btnSaveCustomer.setText("Update Customer");
    }

    private void searchCustomer() {
        ObservableList<CustomerTM> tmList = FXCollections.observableArrayList();
        for (Customer c : Database.customerTable
        ) {
            Button btn = new Button("Delete");
            CustomerTM tm = new CustomerTM(c.getId(), c.getName(), c.getAddress(), c.getSalary(), btn);
            tmList.add(tm);

            // Delete btn OnAction
            btn.setOnAction(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                        "Are you sure whether do you want delete customer" + c.getId(),
                        ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> buttonType = alert.showAndWait();

                if (buttonType.get() == ButtonType.YES) {
                    boolean isRemoved = Database.customerTable.remove(c);
                    if (isRemoved) {
                        searchCustomer();
                        new Alert(Alert.AlertType.CONFIRMATION, "Customer deleted").show();
                    } else {
                        new Alert(Alert.AlertType.WARNING, "Try again").show();
                    }
                }
            });

        }
        tblCustomer.setItems(tmList);
    }

    public void saveCustomerOnAction(ActionEvent actionEvent) {
        Customer c1 = new Customer(
                txtId.getText(), txtName.getText(), txtAddress.getText(), Double.parseDouble(txtSalary.getText()));

        if (btnSaveCustomer.getText().equalsIgnoreCase("Save Customer")) {
            // save code
            boolean isSaved = Database.customerTable.add(c1);

            if (isSaved) {
                searchCustomer();
                clear();
                new Alert(Alert.AlertType.CONFIRMATION, "Customer Saved").show();
            } else {
                new Alert(Alert.AlertType.WARNING, "Try again").show();
            }
        } else {
            // update code
            for (int i = 0; i < Database.customerTable.size(); i++) {
                if (txtId.getText().equalsIgnoreCase(Database.customerTable.get(i).getId())) {
                    Database.customerTable.get(i).setName(txtName.getText());
                    Database.customerTable.get(i).setAddress(txtAddress.getText());
                    Database.customerTable.get(i).setSalary(Double.parseDouble(txtSalary.getText()));
                    searchCustomer();
                    clear();
                    new Alert(Alert.AlertType.INFORMATION, "Customer updated").show();
                }
            }
        }


    }

    private void clear() {
        txtId.clear();
        txtName.clear();
        txtAddress.clear();
        txtSalary.clear();
    }

    public void newCustomerOnAction(ActionEvent actionEvent) {
        btnSaveCustomer.setText("Save Customer");
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) CustomerFromContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/DashboardForm.fxml"))));
    }
}
