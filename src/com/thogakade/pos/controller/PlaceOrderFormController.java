package com.thogakade.pos.controller;

import com.thogakade.pos.db.DBConnection;
import com.thogakade.pos.db.Database;
import com.thogakade.pos.modal.Customer;
import com.thogakade.pos.modal.Item;
import com.thogakade.pos.modal.ItemDetails;
import com.thogakade.pos.modal.Order;
import com.thogakade.pos.view.tm.CartTM;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class PlaceOrderFormController {
    public TextField txtDate;
    public ComboBox<String> cmbCustomerId;
    public ComboBox<String> cmbItemCode;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtSalary;
    public TextField txtDescription;
    public TextField txtUnitPrice;
    public TextField txtQtyOnHand;
    public TextField txtQty;
    public TableView<CartTM> tblCart;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public TableColumn colTotal;
    public TableColumn colOption;
    public Label lblTotal;
    public TextField txtOrderId;
    public AnchorPane placeOrderFormContext;

    public void initialize() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));

        setDateAndOrderId();
        loadAllCustomerId();
        loadAllItemCode();
        setOrderId();

        cmbCustomerId.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (null != newValue) {
                        setCustomerDetails();
                    }
                });

        cmbItemCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (null != newValue) {
                setItemDetails();
            }
        });


    }

    private void setOrderId() {
        try {
            String sql = " SELECT orderId FROM `Order` ORDER BY orderId DESC LIMIT 1"; // after 10 not working... (UNSINEG)
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Generate id
                String tempOrderId = resultSet.getString(1); // D-3
                String array[] = tempOrderId.split("-"); // [D,3]
                int tempNumber = Integer.parseInt(array[1]);
                int finalizeOrderId = tempNumber + 1;
                txtOrderId.setText("D-" + finalizeOrderId);
            } else {
                txtOrderId.setText("D-1");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setItemDetails() {
        try {
            String sql = "SELECT * FROM Item WHERE code=?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, cmbItemCode.getValue());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                txtDescription.setText(resultSet.getString(2));
                txtUnitPrice.setText(String.valueOf(resultSet.getDouble(3)));
                txtQtyOnHand.setText(String.valueOf(resultSet.getInt(4)));
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void setCustomerDetails() {

        try {
            String sql = "SELECT * FROM Customer WHERE id=?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, cmbCustomerId.getValue());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                txtName.setText(resultSet.getString(2));
                txtAddress.setText(resultSet.getString(3));
                txtSalary.setText(String.valueOf(resultSet.getDouble(4)));
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void loadAllItemCode() {

        try {
            String sql = "SELECT code FROM Item";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            ArrayList<String> idList = new ArrayList<>();
            while (resultSet.next()) {
                idList.add(resultSet.getString(1));
            }
            ObservableList<String> obList = FXCollections.observableArrayList(idList);
            cmbItemCode.setItems(obList);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadAllCustomerId() {

        try {
            String sql = "SELECT id FROM Customer";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            ArrayList<String> idList = new ArrayList<>();
            while (resultSet.next()) {
                idList.add(resultSet.getString(1));
            }
            ObservableList<String> obList = FXCollections.observableArrayList(idList);
            cmbCustomerId.setItems(obList);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setDateAndOrderId() {
        // Set Date
        /* Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String d = df.format(date);
        txtDate.setText(d);*/
        txtDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        // Set order id
    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) placeOrderFormContext.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/DashboardForm.fxml"))));
    }

    private boolean checkQty(String code, int qty) {

        try {
            String sql = "SELECT qtyOnHand FROM Item WHERE code=?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1, code);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int tempQty = resultSet.getInt(1);
                if (tempQty > qty) {
                    return true;
                } else {
                    return false;
                }
            }


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        /*for (Item i : Database.itemTable
        ) {
            if (code.equals(i.getCode())) {
                if (i.getQtyOnHand() >= qty) {
                    return true;
                } else {
                    return false;
                }
            }
        }*/
        return false;
    }


    ObservableList<CartTM> obList = FXCollections.observableArrayList();

    public void addToCartOnAction(ActionEvent actionEvent) {

        if (!checkQty(cmbItemCode.getValue(), Integer.parseInt(txtQty.getText()))) { // check invalid qty
            new Alert(Alert.AlertType.WARNING, "Invalid Qty").show();
            return;
        }

        double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        int qty = Integer.parseInt(txtQty.getText());
        double total = unitPrice * qty;
        Button btn = new Button("Delete");

        int row = isAlreadyExists(cmbItemCode.getValue());

        if (row == -1) {
            CartTM tm = new CartTM(cmbItemCode.getValue(), txtDescription.getText(),
                    unitPrice, qty, total, btn);
            obList.add(tm);
            tblCart.setItems(obList);
        } else {
            int tempQty = obList.get(row).getQty() + qty;
            double tempTotal = obList.get(row).getTotal() * tempQty;

            if (!checkQty(cmbItemCode.getValue(), tempQty)) {  // check qty stock again in the list
                new Alert(Alert.AlertType.WARNING, "Invalid Qty").show();
                return;
            }

            obList.get(row).setQty(tempQty);
            obList.get(row).setTotal(tempTotal);
            tblCart.refresh();
        }


        calculateTotal();
        clearFields();
        cmbItemCode.requestFocus();

        btn.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> buttonType = alert.showAndWait();
            if (buttonType.get() == ButtonType.YES) {
                for (CartTM tm : obList
                ) {
                    obList.remove(tm);
                    calculateTotal();
                    tblCart.refresh();
                }
            }
        });
    }

    private void clearFields() {
        txtDescription.clear();
        txtUnitPrice.clear();
        txtQtyOnHand.clear();
        txtQty.clear();
        cmbItemCode.setValue(null);
    }

    private int isAlreadyExists(String code) {
        for (int i = 0; i < obList.size(); i++) {
            if (obList.get(i).getCode().equals(code)) {
                return i;
            }
        }
        return -1;
    }

    private void calculateTotal() {
        double total = 0;
        for (CartTM tm : obList
        ) {
            total += tm.getTotal();
        }
        lblTotal.setText(String.valueOf(total));
    }

    public void placeOrderOnAction(ActionEvent actionEvent) throws SQLException {
        if (obList.isEmpty()) return;
        ArrayList<ItemDetails> details = new ArrayList<>();
        for (CartTM tm : obList
        ) {
            details.add(new ItemDetails(tm.getCode(), tm.getUnitPrice(), tm.getQty()));
        }

        Order order = new Order(
                txtOrderId.getText(), new Date(), Double.parseDouble(lblTotal.getText()),
                cmbCustomerId.getValue(), details
        );

        // Place order
        Connection con = null;
        try {

            con = DBConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            String sql = " INSERT `Order` VALUES (?,?,?,?)";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, order.getOrderId());
            statement.setString(2, txtDate.getText());
            statement.setDouble(3, order.getTotalCost());
            statement.setString(4, order.getCustomer());
            boolean isSaved = statement.executeUpdate() > 0;

            if (isSaved) { // if saved
                boolean isAllUpdated = updateQty(details);
                if (isAllUpdated) { // if all updated
                    con.commit();
                    new Alert(Alert.AlertType.CONFIRMATION, "Order placed").show();
                    clearAll();
                } else { // if not all updated
                    con.setAutoCommit(true);
                    con.rollback();
                    new Alert(Alert.AlertType.WARNING, "Try again").show();
                }
            } else { // if not saved
                con.setAutoCommit(true);
                con.rollback();
                new Alert(Alert.AlertType.WARNING, "Try again").show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.setAutoCommit(true);
        }
    }

    private boolean updateQty(ArrayList<ItemDetails> details) {
        try {
            for (ItemDetails d : details
            ) {
                String sql = " INSERT `Order Details` VALUES (?,?,?,?)";
                PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
                statement.setString(1, d.getCode());
                statement.setString(2, txtOrderId.getText());
                statement.setDouble(3, d.getUnitPrice());
                statement.setInt(4, d.getQty());

                boolean isOrderDetailsSaved = statement.executeUpdate() > 0;

                if (isOrderDetailsSaved) { // if order details saved
                    boolean isQtyUpdated = update(d);
                    if (!isQtyUpdated) {
                        return false;
                    }
                } else {
                    return false;
                }


            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return true;
    }

    private boolean update(ItemDetails d) {
        try {
            String sql = "UPDATE Item SET qtyOnHand=(qtyOnHand-?) WHERE code=?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setInt(1, d.getQty());
            statement.setString(2, d.getCode());
            return statement.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void clearAll() {
        obList.clear();
        calculateTotal();
        txtName.clear();
        txtAddress.clear();
        txtSalary.clear();
        cmbCustomerId.setValue(null);
        clearFields();
        cmbCustomerId.requestFocus();
        setOrderId();
    }
}
