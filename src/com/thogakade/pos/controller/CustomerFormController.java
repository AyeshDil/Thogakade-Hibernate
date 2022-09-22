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
import java.sql.*;
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
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Thogakade", "root", "1234");
            String sql = "SELECT * FROM Customer WHERE name LIKE ? || address LIKE ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,searchText);
            statement.setString(2,searchText);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Button btn = new Button("Delete");
                CustomerTM tm = new CustomerTM(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDouble(4),
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
                            Class.forName("com.mysql.cj.jdbc.Driver");
                            Connection connection1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/Thogakade", "root", "1234");
                            String sql1 = "DELETE FROM Customer WHERE id=?";
                            PreparedStatement statement1 = connection.prepareStatement(sql1);
                            statement1.setString(1, tm.getId());
                            if (statement1.executeUpdate() > 0) {
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
        Customer c1 = new Customer(
                txtId.getText(), txtName.getText(), txtAddress.getText(), Double.parseDouble(txtSalary.getText()));

        if (btnSaveCustomer.getText().equalsIgnoreCase("Save Customer")) {
            // save code
            // Database
            try {
                // 1 step - driver load ram
                Class.forName("com.mysql.cj.jdbc.Driver");
                // 2 step - create connection
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Thogakade", "root", "1234");
                /*// 3 step - create statement
                Statement statement = connection.createStatement();
                // 4step - create query
                String sql = "INSERT INTO Customer VALUES('"+c1.getId()+"','"+
                        c1.getName()+"','"+c1.getAddress()+"','"+c1.getSalary()+"')";
                // 5 step - statement execute
                int isSaved = statement.executeUpdate(sql);*/

                // Advance prepared statements
                String sql = "INSERT INTO Customer VALUES(?,?,?,?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, c1.getId());
                statement.setString(2, c1.getName());
                statement.setString(3, c1.getAddress());
                statement.setDouble(4, c1.getSalary());


                if (statement.executeUpdate() > 0) {
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
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Thogakade", "root", "1234");
                String sql = "UPDATE Customer SEt name=?, address=?, salary=? WHERE id=?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, c1.getName());
                statement.setString(2, c1.getAddress());
                statement.setDouble(3, c1.getSalary());
                statement.setString(4, c1.getId());

                if (statement.executeUpdate() > 0) {
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
