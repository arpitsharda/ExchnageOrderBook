package com.exchange.obs

import com.exchange.obs.domain.Side
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

class OrderBookTest {

    private lateinit var orderBook: OrderBook;

    @BeforeEach
    fun setUp() {
        orderBook = OrderBook();
        addBidsToOrderBook();
        addOffersToOrderBook()
    }

    private fun addOffersToOrderBook() {
        /*
            [Order4, 130.70, 5000 ,Time 12PM]
            [Order5, 130.68, 2000 ,Time 12PM]
            [Order6, 130.68, 2000 ,Time 12:05PM]
        */

        val today = LocalDateTime.now()

        val order4 = Order(
            4,
            130.70,
            5000L,
            today.withHour(12).withMinute(0).withSecond(0).withNano(0).toEpochSecond(ZoneOffset.UTC),
            Side.SELL
        );
        val order5 = Order(
            5,
            130.68,
            2000L,
            today.withHour(12).withMinute(0).withSecond(0).withNano(0).toEpochSecond(ZoneOffset.UTC),
            Side.SELL
        );
        val order6 = Order(
            6,
            130.68,
            2000L,
            today.withHour(13).withMinute(5).withSecond(0).withNano(0).toEpochSecond(ZoneOffset.UTC),
            Side.SELL
        );

        // sorted by Arrival Time
        arrayOf(order4, order6, order5).forEach { orderBook.addOrder(it) };
    }


    private fun addBidsToOrderBook() {
        /*      [Order1, 130.65, 1000 ,Time 1PM]
                [Order2, 130.60, 2000 ,Time 12:05PM]
                [Order3, 130.60, 2000 ,Time 1:05PM]
        */

        val today = LocalDateTime.now()

        val order1 = Order(
            1,
            130.65,
            1000L,
            today.withHour(13).withMinute(0).withSecond(0).withNano(0).toEpochSecond(ZoneOffset.UTC),
            Side.BUY
        );
        val order2 = Order(
            2,
            130.60,
            2000L,
            today.withHour(12).withMinute(5).withSecond(0).withNano(0).toEpochSecond(ZoneOffset.UTC),
            Side.BUY
        );
        val order3 = Order(
            3,
            130.60,
            2000L,
            today.withHour(13).withMinute(5).withSecond(0).withNano(0).toEpochSecond(ZoneOffset.UTC),
            Side.BUY
        );

        // sorted by Arrival Time
        arrayOf(order1, order3, order2).forEach { orderBook.addOrder(it) };
    }

    @Test
    @DisplayName("Should Add the Order to correct side of the order book")
    fun addOrder() {
        orderBook.displayOrderBook();
        assertEquals(orderBook.getLevelPrice(Side.BUY, 1), 130.65)
        assertEquals(orderBook.getLevelPrice(Side.BUY, 2), 130.60)

        val totalVolumeLevel1 = orderBook.getTotalVolume(Side.BUY, 1)
        val totalVolumeLevel2 = orderBook.getTotalVolume(Side.BUY, 2)

        println("totalVolumeLevel1 = $totalVolumeLevel1")
        println("totalVolumeLevel2 = $totalVolumeLevel2")
    }

    @Test
    @DisplayName("Should remove an order from Order book when requested")
    fun removeOrder() {
        orderBook.displayOrderBook();
        orderBook.removeOrder(3)
        orderBook.displayOrderBook();
    }

    @Test
    @DisplayName("Should modify an order in Order book and update notional when requested")
    fun modifyOrder() {
        orderBook.displayOrderBook();
        orderBook.modifyOrder(3, 5000)
        orderBook.displayOrderBook();
    }

    @Test
    @DisplayName("Should get all order from Order book by side")
    fun getOrders() {
        orderBook.displayOrderBook();
        val buyOrders = orderBook.getOrders(Side.BUY)
        assertEquals(buyOrders.size, 3)

        val sellOrders = orderBook.getOrders(Side.SELL)
        assertEquals(sellOrders.size, 3)

        orderBook.addOrder(Order(
            7,
            130.65,
            2000L,
            LocalDateTime.now().withHour(13).withMinute(5).withSecond(0).withNano(0).toEpochSecond(ZoneOffset.UTC),
            Side.SELL
        ))
        orderBook.displayOrderBook();

        val sellOrdersAfterAdd = orderBook.getOrders(Side.SELL)

        assertEquals(sellOrdersAfterAdd.size, 4)
    }

    @Test
    fun getLevelPrice() {
        orderBook.displayOrderBook();
        assertEquals(orderBook.getLevelPrice(Side.BUY, 1), 130.65)
        assertEquals(orderBook.getLevelPrice(Side.SELL, 2), 130.7)
    }

    @Test
    fun getTotalVolume() {
        orderBook.displayOrderBook();
        assertEquals(orderBook.getTotalVolume(Side.BUY, 1), 1000)
        assertEquals(orderBook.getTotalVolume(Side.SELL, 2), 5000)
    }
}