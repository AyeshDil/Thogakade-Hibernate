package com.thogakade.pos.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "item")
public class Item {
    @Id
    @Column(name = "code")
    private String code;
    @Column(name = "description" , nullable = false)
    private String description;
    @Column(name = "unit_price", nullable = false)
    private double unitPrice;
    @Column(name = "qty_on_hand", nullable = false)
    private int qtyOnHand;

    public Item() {
    }

    public Item(String code, String description, double unitPrice, int qtyOnHand) {
        this.code = code;
        this.description = description;
        this.unitPrice = unitPrice;
        this.qtyOnHand = qtyOnHand;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQtyOnHand() {
        return qtyOnHand;
    }

    public void setQtyOnHand(int qtyOnHand) {
        this.qtyOnHand = qtyOnHand;
    }
}
