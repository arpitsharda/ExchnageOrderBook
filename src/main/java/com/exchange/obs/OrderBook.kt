package com.exchange.obs

import com.exchange.obs.domain.Price
import com.exchange.obs.domain.Side
import java.util.*

class OrderBook : IOrderBook {
    private val buySideOrderBook =
        TreeMap<Double, PriorityQueue<Order>>(Comparator.reverseOrder()) // least price priority 1
    private val sellSideOrderBook =
        TreeMap<Double, PriorityQueue<Order>>(Comparator.naturalOrder()) // highest price priority 1

    private val orderMap: MutableMap<Long, Order> = HashMap()

    override fun addOrder(order: Order) {
        val orderBook: TreeMap<Double, PriorityQueue<Order>> = getOrderBookBySide(order.side)

        orderBook.putIfAbsent(
            order.price,
            PriorityQueue(Comparator.comparing(Order::getId).thenComparing(Order::getTimestamp))
        )

        orderBook[order.price]?.add(order)
        this.orderMap[order.id] = order
    }

    override fun removeOrder(order: Order) {
    }

    override fun modifyOrder(order: Order) {
    }

    override fun getOrders(side: Char) {
    }

    override fun getLevelPrice(side: Side, level: Int): Price {
        val orderBook: TreeMap<Double, PriorityQueue<Order>> = getOrderBookBySide(side)
        return orderBook.keys.stream().skip((level - 1).toLong()).findFirst().map { Price(it) }.orElseThrow()
    }

    override fun displayOrderBook() {

        println("----------------------BID SIDE-------------------------------")
        printOrderBook(buySideOrderBook)

        println("----------------------OFFER SIDE-------------------------------")
        printOrderBook(sellSideOrderBook)
    }

    private fun printOrderBook(oneSideOrderBook: TreeMap<Double, PriorityQueue<Order>>) {
        var level = 1;
        oneSideOrderBook.forEach { levelEntry ->
            println("Price Level $level : ${levelEntry.key}")
            levelEntry.value.forEach {
                println("\t\t${it.id}, ${it.notional}, ${it.timestamp}")
            }
            level++
        }
    }

    private fun getOrderBookBySide(side: Side): TreeMap<Double, PriorityQueue<Order>> {
        return when (side) {
            Side.BUY -> buySideOrderBook
            Side.SELL -> sellSideOrderBook
        }
    }
}
