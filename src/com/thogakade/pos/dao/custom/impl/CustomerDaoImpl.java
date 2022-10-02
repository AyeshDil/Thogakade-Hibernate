package com.thogakade.pos.dao.custom.impl;

import com.thogakade.pos.dao.CrudUtil;
import com.thogakade.pos.dao.custom.CustomerDao;
import com.thogakade.pos.db.DBConnection;
import com.thogakade.pos.entity.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDaoImpl implements CustomerDao {
    @Override
    public boolean save(Customer c) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO Customer VALUES(?,?,?,?)";
        return CrudUtil.execute(sql,c.getId(),c.getName(),c.getAddress(),c.getSalary());
    }

    @Override
    public boolean update(Customer c) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE Customer SEt name=?, address=?, salary=? WHERE id=?";
        return CrudUtil.execute(sql,c.getName(),c.getAddress(),c.getSalary(),c.getId());
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM Customer WHERE id=?";
        return CrudUtil.execute(sql,id);
    }

    @Override
    public ArrayList<Customer> searchCustomers(String searchText) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM Customer WHERE name LIKE ? || address LIKE ?";
        ResultSet resultSet = CrudUtil.execute(sql,searchText,searchText);

        ArrayList<Customer> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(new Customer(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4)
            ));
        }
        return list;
    }
}
