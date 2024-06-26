Dry run before coding

Stream of order coming in  --> Update the order book --> Match and trigger execution

`
Order
{
long id;                    // id of the order
double price;               // limit price specified
double notional;            // Notional
long timestamp;             // Order Time
char side;                  // Direction   -- can be enum/sealed class

char tif;                   // time in force other contingency fields can be added    
char orderType              // Market/Limit
}
`

OrderBook [Ticker: BT] one book per share

List<OrderBookEntry> sorted by price, arrival time  Comparator.comparing(obe.price).thenComparing(time)

we need to bucket by level of pricing information so tree map can reduce the Complexity from O(n) to O(Log n)

TreeMap<Price, List<OrderBookEntry> still need the list to be sorted by Price and time in the bucket.
List can be placed by Priority Queue O(log(n)) vs O(n)


-- customer sells to the highest bidder
buySideOrderBook --> list of all bids Orders available to buy
[Order id, Price, Notional,Arrival time ]
* [Order1, 130.65, 1000 ,Time 1PM]
* [Order2, 130.60, 2000 ,Time 12:05PM]
* [Order2, 130.60, 2000 ,Time 1:05PM]


-- customer buys the lowest offer
sellSideOrderBook --> list of all offers Orders available to sell
[Order id, Price, Notional ]
* [Order3, 130.70, 5000 ,Time 12PM]
* [Order4, 130.68, 2000 ,Time 12PM]
* [Order5, 130.68, 2000 ,Time 12:05PM]
  // Best Execution
  <---------Bid----------|Spread|---------Offer---------->
  buy the lowest offer
  sell the highest Bid

New Order comes in to Buy 3000 BT @130.65 not matching or better price available so this order will be added to OrderBook buySide

Buy if any price <= 130.65

buySideOrderBook
[Order id, Price, Notional ]
* [Order1, 130.65, 1000 ,Time 1PM]
* [Order6, 130.65, 3000 ,Time 2:05PM]  -- when ever someone is willing to sell @130.65 this will get matched
* [Order2, 130.60, 2000 ,Time 12:05PM]
* [Order3, 130.60, 2000 ,Time 1:05PM]


New Order comes in to Sell 5000 BT @130.65. 2 orders available on bid order book available so this order will be matched by price, arrival time

Sell if any price >= 130.65

buySideOrderBook
[Order id, Price, Notional ]
* [Order1, 130.65, 1000 ,Time 1PM]     -- this will trigger execution of 1000 BT@130.65 First  [Remove Order1]
* [Order6, 130.65, 3000 ,Time 2:05PM]  -- this will trigger execution of 3000 BT@130.65 Second [Remove Order6]
* [Order2, 130.60, 2000 ,Time 12:05PM]
* [Order3, 130.60, 2000 ,Time 1:05PM]


To add ability to execute orders, Matching engine should
* Apply pre trade rules
* Add the Buy offer on the Bid side of the book.
* For Buy Order check Order book for Opposite side [i.e Offer book] for orders at a price equal to order price or better. i.e. equal or lower than order limit price
* If an eligible order is found in Offer book then remove/modify the Order and also matching order[s] on Offer book
* Send the trade information respective clients



**Other consideration**
Orders should have status so that they can be soft deleted
Limit/Market orders Implementation
Order book can be kept in memory but should be able to recreate the state by replaying logs on restart.
Order cache can be backed by some store.
Consider updating TreeMap and Hashmap to thread safe implementation. 
