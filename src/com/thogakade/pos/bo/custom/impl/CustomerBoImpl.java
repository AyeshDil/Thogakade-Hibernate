package com.thogakade.pos.bo.custom.impl;

import com.thogakade.pos.bo.custom.CustomerBo;
import com.thogakade.pos.dao.DaoFactory;
import com.thogakade.pos.dao.DaoTypes;
import com.thogakade.pos.dao.custom.CustomerDao;
import com.thogakade.pos.dto.CustomerDto;
import com.thogakade.pos.entity.Customer;

import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerBoImpl implements CustomerBo {

    private CustomerDao dao = DaoFactory.getInstance().getDao(DaoTypes.CUSTOMER);

    @Override
    public boolean saveCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException {
        return dao.save(
                new Customer(dto.getId(), dto.getName(), dto.getAddress(), dto.getSalary())
        );
    }

    @Override
    public boolean updateCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException {
        return dao.update(
                new Customer(dto.getId(), dto.getName(), dto.getAddress(), dto.getSalary())
        );
    }

    @Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
        return dao.delete(id);
    }

    @Override
    public ArrayList<CustomerDto> searchCustomer(String searchText) throws SQLException, ClassNotFoundException {
        ArrayList<Customer> entities = dao.searchCustomers(searchText);
        ArrayList<CustomerDto> dtoList = new ArrayList<>();
        for (Customer c: entities){
            dtoList.add(new CustomerDto(c.getId(),c.getName(),c.getAddress(),c.getSalary()));
        }

        return dtoList;
    }
}
