package com.exchange.obs

import com.exchange.obs.domain.Side


data class Order(
    val orderId: Int,
    val price: Double,
    val notional: Long,
    val timestamp: Long,        // assuming EpochMillis
    val side: Side
    //private String type;  Market/Limit
    //private String tif;   Time in force
)
