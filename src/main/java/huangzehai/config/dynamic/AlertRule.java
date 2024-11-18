package huangzehai.config.dynamic;

// 定义告警规则
public class AlertRule {
    public String stockName;
    public double priceThreshold;

    public AlertRule() {}

    public AlertRule(String stockName, double priceThreshold) {
        this.stockName = stockName;
        this.priceThreshold = priceThreshold;
    }

    @Override
    public String toString() {
        return "AlertRule{" +
                "stockName='" + stockName + '\'' +
                ", priceThreshold=" + priceThreshold +
                '}';
    }
}