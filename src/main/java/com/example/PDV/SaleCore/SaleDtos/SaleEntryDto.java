package com.example.PDV.SaleCore.SaleDtos;

public class SaleEntryDto {

    private Integer boxId;

    private ItemsForSaleDto items;

    private InfoPaymentDto payment;

    public Integer getBoxId() {
        return boxId;
    }

    public void setBoxId(Integer boxId) {
        this.boxId = boxId;
    }

    public ItemsForSaleDto getItems() {
        return items;
    }

    public void setItems(ItemsForSaleDto items) {
        this.items = items;
    }

    public InfoPaymentDto getPayment() {
        return payment;
    }

    public void setPayment(InfoPaymentDto payment) {
        this.payment = payment;
    }
}
