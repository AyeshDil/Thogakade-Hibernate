package com.thogakade.pos.dao;

import com.thogakade.pos.dao.custom.impl.CustomerDaoImpl;
import com.thogakade.pos.dao.custom.impl.ItemDaoImpl;

public class DaoFactory {
    //Factory method design patten
    private static DaoFactory daoFactory;
    private DaoFactory(){}

    public static DaoFactory getInstance(){
        return daoFactory==null?(daoFactory= new DaoFactory()):daoFactory;
    }

    public <T> T getDao(DaoTypes type){
        switch (type){
            case CUSTOMER:
                return (T) new CustomerDaoImpl();
            case ITEM:
                return (T) new ItemDaoImpl();
            default:
                return null;
        }
    }

}
