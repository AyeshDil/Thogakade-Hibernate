package com.thogakade.pos.controller;

import com.jfoenix.controls.JFXButton;
import com.thogakade.pos.bo.BoFactory;
import com.thogakade.pos.bo.BoTypes;
import com.thogakade.pos.bo.custom.CustomerBo;
import com.thogakade.pos.dao.DaoFactory;
import com.thogakade.pos.dao.DaoTypes;
import com.thogakade.pos.dao.custom.CustomerDao;
import com.thogakade.pos.dao.custom.impl.CustomerDaoImpl;
import com.thogakade.pos.dto.CustomerDto;
import com.thogakade.pos.entity.Customer;
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

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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

    private CustomerBo customerBo = BoFactory.getInstance().getBo(BoTypes.CUSTOMER);

    private String searchText = "";

    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colOptions.setCellValueFactory(new PropertyValueFactory<>("btn"));

        searchCustomer(searchText);

        tblCustomer.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (null != newValue) {
                setData(newValue);
            }
        });

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchText = newValue;
            searchCustomer(searchText);
        });
    }

    private void setData(CustomerTM tm) {
        txtId.setText(tm.getId());
        txtName.setText(tm.getName());
        txtAddress.setText(tm.getAddress());
        txtSalary.setText(String.valueOf(tm.getSalary()));
        btnSaveCustomer.setText("Update Customer");
    }

    private void searchCustomer(String text) {
        String searchText="%"+text+"%";
        try {
            ObservableList<CustomerTM> tmList = FXCollections.observableArrayList();

            ArrayList<CustomerDto> cList = customerBo.searchCustomer(searchText);

            for (CustomerDto c:cList) {
                Button btn = new Button("Delete");
                CustomerTM tm = new CustomerTM(
                        c.getId(),
                        c.getName(),
                        c.getAddress(),
                        c.getSalary(),
                        btn);
                tmList.add(tm);

                // Delete btn OnAction
                btn.setOnAction(event -> {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Are you sure whether do you want delete customer " + tm.getId(),
                            ButtonType.YES, ButtonType.NO);
                    Optional<ButtonType> buttonType = alert.showAndWait();

                    if (buttonType.get() == ButtonType.YES) {

                        try {
                            boolean isDeletedCustomer = customerBo.deleteCustomer(tm.getId());
                            if (isDeletedCustomer) {
                                searchCustomer(searchText);
                                new Alert(Alert.AlertType.CONFIRMATION, "Customer deleted").show();
                            } else {
                                new Alert(Alert.AlertType.WARNING, "Try again").show();
                            }
                        } catch (ClassNotFoundException | SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            tblCustomer.setItems(tmList);

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveCustomerOnAction(ActionEvent actionEvent) {


        if (btnSaveCustomer.getText().equalsIgnoreCase("Save Customer")) {
            // save code
            // Database
            try {
                boolean isCustomerSaved = customerBo.saveCustomer(
                        new CustomerDto(
                                txtId.getText(),
                                txtName.getText(),
                                txtAddress.getText(),
                                Double.parseDouble(txtSalary.getText())
                        )
                );

                if (isCustomerSaved) {
                    searchCustomer(searchText);
                    clear();
                    new Alert(Alert.AlertType.CONFIRMATION, "Customer Saved").show();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try again").show();
                }

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }


        } else {
            // update code
            try {
                boolean isCustomerUpdated = customerBo.updateCustomer(
                        new CustomerDto(
                                txtId.getText(),
                                txtName.getText(),
                                txtAddress.getText(),
                                Double.parseDouble(txtSalary.getText())
                        )
                );

                if (isCustomerUpdated) {
                    searchCustomer(searchText);
                    clear();
                    new Alert(Alert.AlertType.INFORMATION, "Customer updated").show();
                } else {
                    new Alert(Alert.AlertType.WARNING, "Try Again").show();

                }

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
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
