package com.exchange.obs;


import com.exchange.obs.domain.Side;

public class Order {
    private final long id;
    private final double price;
    private final double notional;
    private final long timestamp;       // assuming EpochMillis
    private final Side side;

    public Order(long id, double price, double notional, long timestamp, Side side) {
        this.id = id;
        this.price = price;
        this.notional = notional;
        this.timestamp = timestamp;
        this.side = side;
    }

    public long getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public double getNotional() {
        return notional;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Side getSide() {
        return side;
    }

    //private String type;  Market/Limit
    //private String tif;   Time in force
}
