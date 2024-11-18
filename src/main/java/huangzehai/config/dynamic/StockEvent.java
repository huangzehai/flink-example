package huangzehai.config.dynamic;

// 定义股票事件
public class StockEvent {
    public String stockName;
    public double stockPrice;

    public StockEvent() {}

    public StockEvent(String stockName, double stockPrice) {
        this.stockName = stockName;
        this.stockPrice = stockPrice;
    }

    @Override
    public String toString() {
        return "StockEvent{" +
                "stockName='" + stockName + '\'' +
                ", stockPrice=" + stockPrice +
                '}';
    }
}
