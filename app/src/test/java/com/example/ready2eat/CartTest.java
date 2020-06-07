package com.example.ready2eat;

import com.example.ready2eat.Model.Order;
import com.example.ready2eat.View.Cart;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CartTest {

    @Test
    public void totalPriceCorrect() {

        Cart cart = new Cart();

        Order order1 = new Order("2", "25.2", "0");
        Order order2 = new Order("3", "12", "10");

        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);

        float actual = (float) 82.8;
        float delta = (float) .1;

        float total = cart.calculateTotal(orders);

        assertEquals(total, actual, delta);
    }

    @Test
    public void totalPriceIncorrect() {

        Cart cart = new Cart();

        Order order1 = new Order("2", "25.2", "0");
        Order order2 = new Order("3", "12", "10");

        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);

        float actual = (float) 70;
        float delta = (float) .1;

        float total = cart.calculateTotal(orders);

        assertNotEquals(total, actual, delta);
    }
}