package com.exchange.obs

import com.exchange.obs.domain.Price
import com.exchange.obs.domain.Side
import org.junit.jupiter.api.Assertions
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
    }

    @Test
    @DisplayName("Should Add the Order to correct side of the order book")
    fun addOrder() {
        /*      [Order1, 130.65, 1000 ,Time 1PM]
                [Order2, 130.60, 2000 ,Time 12:05PM]
                [Order2, 130.60, 2000 ,Time 1:05PM]
        */

        val today = LocalDateTime.now()

        val order1 = Order(
            1,
            130.65,
            1000.0,
            today.withHour(13).withMinute(0).withSecond(0).withNano(0).toEpochSecond(ZoneOffset.UTC),
            Side.BUY
        );
        val order2 = Order(
            2,
            130.60,
            2000.0,
            today.withHour(12).withMinute(5).withSecond(0).withNano(0).toEpochSecond(ZoneOffset.UTC),
            Side.BUY
        );
        val order3 = Order(
            3,
            130.60,
            2000.0,
            today.withHour(13).withMinute(5).withSecond(0).withNano(0).toEpochSecond(ZoneOffset.UTC),
            Side.BUY
        );

        // sorted by Arrival Time
        arrayOf(order1, order3, order2).forEach { orderBook.addOrder(it) };

        orderBook.displayOrderBook();

        Assertions.assertEquals(orderBook.getLevelPrice(Side.BUY, 1), Price(130.65))

    }


}