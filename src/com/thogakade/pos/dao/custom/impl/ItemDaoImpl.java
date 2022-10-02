package com.thogakade.pos.dao.custom.impl;

import com.thogakade.pos.dao.CrudUtil;
import com.thogakade.pos.dao.custom.ItemDao;
import com.thogakade.pos.db.DBConnection;
import com.thogakade.pos.entity.Item;
import com.thogakade.pos.view.tm.ItemTM;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.CacheRequest;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemDaoImpl implements ItemDao {
    @Override
    public boolean save(Item i) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Item VALUES(?,?,?,?)";
        return CrudUtil.execute(sql,i.getCode(),i.getDescription(),i.getUnitPrice(),i.getQtyOnHand());
    }

    @Override
    public boolean update(Item i) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Item SET description=?, unitPrice=?, qtyOnHand=? WHERE code=?";
        return CrudUtil.execute(sql,i.getDescription(),i.getUnitPrice(),i.getQtyOnHand(),i.getCode());
    }

    @Override
    public boolean delete(String code) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Item WHERE code=?";
        return CrudUtil.execute(sql, code);
    }

    @Override
    public ArrayList<Item> searchItems(String searchText) throws SQLException, ClassNotFoundException {
        ObservableList<ItemTM> tmList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM Item WHERE description LIKE ?";
        ResultSet resultSet = CrudUtil.execute(sql, searchText);

        ArrayList<Item> iList = new ArrayList<>();
        while (resultSet.next()) {
            iList.add(new Item(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getDouble(3),
                            resultSet.getInt(4)
                    )
            );
        }
        return iList;
    }
}
