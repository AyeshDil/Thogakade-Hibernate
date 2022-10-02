package com.thogakade.pos.dao.custom;

import com.thogakade.pos.dao.CrudDao;
import com.thogakade.pos.entity.Customer;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CustomerDao extends CrudDao<Customer, String> {
    public ArrayList<Customer> searchCustomers(String searchText) throws SQLException, ClassNotFoundException;
}
