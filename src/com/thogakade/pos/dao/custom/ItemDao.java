package com.thogakade.pos.dao.custom;

import com.thogakade.pos.dao.CrudDao;
import com.thogakade.pos.entity.Customer;
import com.thogakade.pos.entity.Item;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ItemDao extends CrudDao<Item, String> {
    public ArrayList<Item> searchItems(String searchText) throws SQLException, ClassNotFoundException;
}
