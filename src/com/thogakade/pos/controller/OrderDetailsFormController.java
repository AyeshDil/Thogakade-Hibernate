package com.thogakade.pos.controller;

import com.thogakade.pos.db.DBConnection;
import com.thogakade.pos.view.tm.OrderTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class OrderDetailsFormController {
    public AnchorPane orderDetailsFormContext;
    public TableView<OrderTM> tblOrderDetails;
    public TableColumn colOrderId;
    public TableColumn colName;
    public TableColumn colDate;
    public TableColumn colTotal;
    public TableColumn colOption;


    public void initialize(){
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        loadOrder();
    }

    private void loadOrder() {

        try{
            String sql = "SELECT * FROM `Order`";
            PreparedStatement statement = DBConnection.getInstance().getConnection().prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            ObservableList<OrderTM> obList = FXCollections.observableArrayList();

            while (resultSet.next()){
                Button btn = new Button("View More");
                OrderTM tm = new OrderTM(
                        resultSet.getString(1),
                        resultSet.getString(4),
                        new Date(),
                        resultSet.getDouble(3),
                        btn);
                obList.add(tm);

                btn.setOnAction(event -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/ItemDetailsForm.fxml"));
                        Parent parent = loader.load();
                        ItemDetailsFormController controller = loader.getController();
                        controller.loadOrderDetails(tm.getOrderId());
                        Stage stage = new Stage();
                        stage.setScene(new Scene(parent));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            tblOrderDetails.setItems(obList);
        } catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }




    }

    public void backToHomeOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage= (Stage) orderDetailsFormContext.getScene().getWindow();
        stage.setScene(new Scene(
                FXMLLoader.load(getClass().getResource("../view/DashboardForm.fxml"))
        ));
    }
}
