package com.exchange.obs

import com.exchange.obs.domain.Side
import java.util.*
import kotlin.streams.toList

class OrderBook : IOrderBook {
    private val buySideOrderBook =
        TreeMap<Double, PriorityQueue<Order>>(Comparator.reverseOrder()) // least price priority 1
    private val sellSideOrderBook =
        TreeMap<Double, PriorityQueue<Order>>(Comparator.naturalOrder()) // highest price priority 1

    private val orderMap: MutableMap<Int, Order> = HashMap()

    override fun addOrder(order: Order) {
        val orderBook: TreeMap<Double, PriorityQueue<Order>> = getOrderBookBySide(order.side)

        orderBook.putIfAbsent(
            order.price,
            PriorityQueue(Comparator.comparing(Order::price).thenComparing(Order::timestamp))
        )
        orderBook[order.price]?.add(order)
        this.orderMap[order.orderId] = order
    }

    override fun removeOrder(orderId: Int) {
        val order: Order? = orderMap[orderId]
        order?.let {
            val orderBookBySide = getOrderBookBySide(it.side)
            val ordersInBook: PriorityQueue<Order>? = orderBookBySide[order.price]
            ordersInBook?.remove(order)
            ordersInBook?.ifEmpty { orderBookBySide.remove(order.price) }
            orderMap.remove(orderId)
        }
    }

    override fun modifyOrder(orderId: Int, newNotional: Long) {
        val order = orderMap[orderId]
        order?.let {
            removeOrder(orderId)
            val updatedOrder = order.copy(notional = newNotional)
            addOrder(updatedOrder)
        }
    }

    override fun getOrders(side: Side): List<Order> {
        val orderBook: TreeMap<Double, PriorityQueue<Order>> = getOrderBookBySide(side)
        return orderBook.values.stream().flatMap { it.stream() }.toList()
    }

    override fun getLevelPrice(side: Side, level: Int): Double {
        val orderBook: TreeMap<Double, PriorityQueue<Order>> = getOrderBookBySide(side)
        return orderBook.keys.stream().skip((level - 1).toLong())
            .findFirst()
            .map { it }.orElseThrow()
    }

    override fun getTotalVolume(side: Side, level: Int): Long {
        val levelPrice = getLevelPrice(side, level)
        val orderBook: TreeMap<Double, PriorityQueue<Order>> = getOrderBookBySide(side)
        return orderBook[levelPrice].orEmpty().stream().mapToLong { it.notional }.sum()
    }

    override fun displayOrderBook() {

        println("----------------------BID SIDE-------------------------------")
        printOrderBook(buySideOrderBook)

        println("----------------------OFFER SIDE-------------------------------")
        printOrderBook(sellSideOrderBook)
    }

    private fun printOrderBook(oneSideOrderBook: TreeMap<Double, PriorityQueue<Order>>) {
        var level = 1
        oneSideOrderBook.forEach { levelEntry ->
            println("Price Level $level : ${levelEntry.key}")
            levelEntry.value.forEach {
                println("\t\t${it.orderId}, ${it.notional}, ${it.timestamp}")
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
