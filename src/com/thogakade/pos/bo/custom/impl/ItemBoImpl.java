package com.thogakade.pos.bo.custom.impl;

import com.thogakade.pos.bo.custom.ItemBo;
import com.thogakade.pos.dao.DaoFactory;
import com.thogakade.pos.dao.DaoTypes;
import com.thogakade.pos.dao.custom.ItemDao;
import com.thogakade.pos.dto.CustomerDto;
import com.thogakade.pos.dto.ItemDto;
import com.thogakade.pos.entity.Customer;
import com.thogakade.pos.entity.Item;

import java.sql.SQLException;
import java.util.ArrayList;

public class ItemBoImpl implements ItemBo {

    private ItemDao itemDao = DaoFactory.getInstance().getDao(DaoTypes.ITEM);

    @Override
    public boolean saveItem(ItemDto dto) throws SQLException, ClassNotFoundException {
        return itemDao.save(
                new Item(dto.getCode(), dto.getDescription(), dto.getUnitPrice(), dto.getQtyOnHand())
        );
    }

    @Override
    public boolean updateItem(ItemDto dto) throws SQLException, ClassNotFoundException {
        return itemDao.update(
                new Item(dto.getCode(), dto.getDescription(), dto.getUnitPrice(), dto.getQtyOnHand())
        );

    }

    @Override
    public boolean deleteItem(String code) throws SQLException, ClassNotFoundException {
        return itemDao.delete(code);
    }

    @Override
    public ArrayList<ItemDto> searchItems(String searchText) throws SQLException, ClassNotFoundException {
        ArrayList<Item> entities = itemDao.searchItems(searchText);
        ArrayList<ItemDto> dtoList = new ArrayList<>();
        for (Item i: entities){
            dtoList.add(new ItemDto(i.getCode(),i.getDescription(),i.getUnitPrice(),i.getQtyOnHand()));
        }

        return dtoList;
    }
}
