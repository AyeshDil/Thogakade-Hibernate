package com.thogakade.pos.controller;

import com.mysql.cj.xdevapi.PreparableStatement;
import com.thogakade.pos.db.DBConnection;
import com.thogakade.pos.db.Database;
import com.thogakade.pos.modal.ItemDetails;
import com.thogakade.pos.modal.Order;
import com.thogakade.pos.view.tm.ItemDetailsTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemDetailsFormController {
    public AnchorPane itemDetailsContext;
    public TableView<ItemDetailsTM> tblItems;
    public TableColumn colCode;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public TableColumn colTotal;

    public void initialize(){
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    public void loadOrderDetails(String id){

        try {
            String sql ="SELECT o.orderId, d.itemCode, d.orderId, d.unitPrice, d.qty " +
                    "FROM `Order` o Inner JOIN `Order Details` d ON o.orderId=d.orderId AND o.orderId=?";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            statement.setString(1,id);
            ResultSet resultSet = statement.executeQuery();

            ObservableList<ItemDetailsTM> tmList = FXCollections.observableArrayList();
            while (resultSet.next()){
                double tempUnitPrice = resultSet.getDouble(4);
                int tempQty = resultSet.getInt(5);
                double tempTotal = tempQty* tempUnitPrice;
                tmList.add(new ItemDetailsTM(
                        resultSet.getString(2),
                        tempUnitPrice,
                        tempQty,
                        tempTotal
                ));
            }
            tblItems.setItems(tmList);

        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage= (Stage) itemDetailsContext.getScene().getWindow();
        stage.setScene(new Scene(
                FXMLLoader.load(getClass().getResource("../view/DashboardForm.fxml"))
        ));
    }
}
