package com.starlab.moon.webfluxdemo.domain;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CartItem {

    private Item item;
    private int quantity;

    private CartItem() {}

    public void increment() {
        this.quantity++;
    }

    public void decrement() { this.quantity--;}

    public CartItem(Item item) {
        this.item = item;
        this.quantity = 1;
    }
}
