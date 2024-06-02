package com.exchange.obs;

import com.exchange.obs.domain.Price;
import com.exchange.obs.domain.Side;

public interface IOrderBook {

    void addOrder(Order order);

    void removeOrder(Order order);

    void modifyOrder(Order order);

    void getOrders(char side);

    Price getLevelPrice(Side side, int level);

    void displayOrderBook();
}
