package huangzehai.config.dynamic;

import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction;
import org.apache.flink.util.Collector;

public class StockAlertApp {
    public static void main(String[] args) throws Exception {
        // 创建 Flink 执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 创建股票数据流（Socket 数据源）
        DataStream<StockEvent> stockStream = env
                .socketTextStream("localhost", 9999) // 数据格式: 股票名称,价格 (e.g., "AAPL,123.45")
                .map(line -> {
                    String[] parts = line.split(",");
                    return new StockEvent(parts[0], Double.parseDouble(parts[1]));
                });

        // 创建告警规则流（Socket 数据源）
        DataStream<AlertRule> alertRuleStream = env
                .socketTextStream("localhost", 8888) // 数据格式: 股票名称,价格阈值 (e.g., "AAPL,120.00")
                .map(line -> {
                    String[] parts = line.split(",");
                    return new AlertRule(parts[0], Double.parseDouble(parts[1]));
                });

        // 定义广播状态描述符
        MapStateDescriptor<String, Double> alertRulesStateDescriptor =
                new MapStateDescriptor<>("AlertRules", String.class, Double.class);

        // 将告警规则流广播
        BroadcastStream<AlertRule> broadcastAlertRuleStream = alertRuleStream.broadcast(alertRulesStateDescriptor);

        // 将股票数据流与广播规则流连接，并处理
        stockStream.connect(broadcastAlertRuleStream)
                .process(new BroadcastProcessFunction<StockEvent, AlertRule, String>() {
                    @Override
                    public void processElement(StockEvent stockEvent, ReadOnlyContext ctx, Collector<String> out) throws Exception {
                        // 获取广播规则
                        Double priceThreshold = ctx.getBroadcastState(alertRulesStateDescriptor).get(stockEvent.stockName);
                        if (priceThreshold != null && stockEvent.stockPrice < priceThreshold) {
                            out.collect("ALERT: " + stockEvent.stockName + " price is " + stockEvent.stockPrice +
                                    " (threshold: " + priceThreshold + ")");
                        }
                    }

                    @Override
                    public void processBroadcastElement(AlertRule alertRule, Context ctx, Collector<String> out) throws Exception {
                        // 更新广播规则
                        ctx.getBroadcastState(alertRulesStateDescriptor).put(alertRule.stockName, alertRule.priceThreshold);
                    }
                })
                .print(); // 将告警信息打印到控制台

        env.execute("Stock Alert Application");
    }
}