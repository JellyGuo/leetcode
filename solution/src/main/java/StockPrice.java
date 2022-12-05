import java.util.TreeMap;

//2034. 股票价格波动
class StockPrice {
    TreeMap<Integer, Integer> t2p;
    TreeMap<Integer, Integer> p2c;

    public StockPrice() {
        t2p = new TreeMap<>();
        p2c = new TreeMap<>();
    }

    public void update(int timestamp, int price) {
        Integer pprice = t2p.getOrDefault(timestamp, null);
        if (pprice != null) {
            p2c.put(pprice, p2c.get(pprice) - 1);
            if (p2c.get(pprice) == 0) p2c.remove(pprice);
        }
        t2p.put(timestamp, price);
        p2c.put(price, p2c.getOrDefault(price, 0) + 1);
    }

    public int current() {
        return t2p.get(t2p.lastKey());
    }

    public int maximum() {
        return p2c.lastKey();
    }

    public int minimum() {
        return p2c.firstKey();
    }

//    int maxTimestamp;
//    Map<Integer, Integer> timePrices;
//    TreeMap<Integer, Integer> prices;
//
//    public StockPrice() {
//        maxTimestamp = 0;
//        timePrices = new HashMap<>();
//        prices = new TreeMap<>();
//    }
//
//    public void update(int timestamp, int price) {
//        maxTimestamp = Math.max(timestamp, maxTimestamp);
//        int prevPrice = timePrices.getOrDefault(timestamp, 0);
//        if (prevPrice > 0) {
//            prices.put(prevPrice, prices.get(prevPrice) - 1);
//            if (prices.get(prevPrice) == 0) {
//                prices.remove(prevPrice);
//            }
//        }
//        timePrices.put(timestamp, price);
//        prices.put(price, prices.getOrDefault(price, 0)+1);
//    }
//
//    public int current() {
//        return timePrices.get(maxTimestamp);
//    }
//
//    public int maximum() {
//        return prices.lastKey();
//    }
//
//    public int minimum() {
//        return prices.firstKey();
//    }
}