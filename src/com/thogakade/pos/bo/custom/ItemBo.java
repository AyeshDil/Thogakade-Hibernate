package com.thogakade.pos.bo.custom;

import com.thogakade.pos.dto.ItemDto;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ItemBo {
    public boolean saveItem(ItemDto dto) throws SQLException, ClassNotFoundException;
    public boolean updateItem(ItemDto dto) throws SQLException, ClassNotFoundException;
    public boolean deleteItem(String code) throws SQLException, ClassNotFoundException;
    public ArrayList<ItemDto> searchItems(String searchText) throws SQLException, ClassNotFoundException;
}
