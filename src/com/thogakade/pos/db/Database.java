package com.thogakade.pos.db;

import com.thogakade.pos.modal.Customer;
import com.thogakade.pos.modal.Item;

import java.util.ArrayList;

public class Database {
    public static ArrayList<Customer> customerTable = new ArrayList<Customer>();
    public static ArrayList<Item> itemTable = new ArrayList<Item>();

    static {
        customerTable.add(new Customer("C001","Nimal","Kandy",25000));
        customerTable.add(new Customer("C002","Ajith","Colombo",23000));
        customerTable.add(new Customer("C003","Ruwan","Kandy", 26000));
        customerTable.add(new Customer("C004","Kumara","Jaffna",31000));

        itemTable.add(new Item("I-001","Pen",20,50));
        itemTable.add(new Item("I-002","Book",160,30));
        itemTable.add(new Item("I-003","Pencil",15,50));
        itemTable.add(new Item("I-004","Pencil box",500,20));
        itemTable.add(new Item("I-005","White board pen",1500,10));
    }

}
