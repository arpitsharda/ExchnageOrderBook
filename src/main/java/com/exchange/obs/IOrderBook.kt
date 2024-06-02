package com.exchange.obs

import com.exchange.obs.domain.Side

interface IOrderBook {
    fun addOrder(order: Order)

    fun removeOrder(orderId: Int)

    fun modifyOrder(orderId: Int, newNotional: Long)

    fun getOrders(side: Side): List<Order>

    fun getLevelPrice(side: Side, level: Int): Double

    fun getTotalVolume(side: Side, level: Int): Long

    fun displayOrderBook()
}
