package com.thogakade.pos.bo;

import com.thogakade.pos.bo.custom.impl.CustomerBoImpl;
import com.thogakade.pos.bo.custom.impl.ItemBoImpl;
import com.thogakade.pos.dao.custom.impl.CustomerDaoImpl;
import com.thogakade.pos.dao.custom.impl.ItemDaoImpl;

public class BoFactory {
    //Factory method design patten
    private static BoFactory boFactory;
    private BoFactory(){}

    public static BoFactory getInstance(){
        return boFactory==null?(boFactory= new BoFactory()):boFactory;
    }

    public <T> T getBo(BoTypes type){
        switch (type){
            case CUSTOMER:
                return (T) new CustomerBoImpl();
            case ITEM:
                return (T) new ItemBoImpl();
            default:
                return null;
        }
    }

}
