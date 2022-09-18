package com.thogakade.pos.controller;

import com.thogakade.pos.db.Database;
import com.thogakade.pos.modal.Customer;
import com.thogakade.pos.view.tm.CustomerTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CustomerFormController {

    public TextField txtId;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtSalary;
    public TextField txtSearch;

    public void initialize(){
        searchCustomer();
    }

    private void searchCustomer() {
        ObservableList<CustomerTM> tmList = FXCollections.observableArrayList();
        for (Customer c:Database.customerTable
             ) {
            Button btn = new Button("Delete");
            CustomerTM tm = new CustomerTM(c.getId(),c.getName(),c.getAddress(),c.getSalary(),btn);
            tmList.add(tm);
        }

    }

    public void saveCustomerOnAction(ActionEvent actionEvent) {
        Customer c1 = new Customer(
                txtId.getText(),txtName.getText(),txtAddress.getText(),Double.parseDouble(txtSalary.getText()));

        boolean isSaved = Database.customerTable.add(c1);

        if (isSaved){
            new Alert(Alert.AlertType.CONFIRMATION,"Customer Saved").show();
        } else {
            new Alert(Alert.AlertType.WARNING,"Try again").show();
        }

    }
}
