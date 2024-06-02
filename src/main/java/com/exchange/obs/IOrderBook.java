package com.exchange.obs;

import com.exchange.obs.domain.Price;
import com.exchange.obs.domain.Side;

public interface IOrderBook {

    public void addOrder(Order order);

    public void removeOrder(Order order);

    public void modifyOrder(Order order);

    public void getOrders(char side);

    public Price getLevelPrice(Side side, int level);

    public void displayOrderBook();
}
