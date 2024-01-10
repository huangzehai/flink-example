package huangzehai.window;

import huangzehai.model.VehicleEvent;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

import java.util.Properties;
import java.util.stream.StreamSupport;

public class KafkaWindowExample {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        FlinkKafkaProducer011<String> myProducer = new FlinkKafkaProducer011<String>(
//                "192.168.0.104:9092",            // broker list
//                "test",                  // target topic
//                new SimpleStringSchema());   // serialization schema

        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.enableCheckpointing(10000);
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "192.168.0.104:9092");
        properties.setProperty("group.id", "test");
        FlinkKafkaConsumer<VehicleEvent> kafkaConsumer =
                new FlinkKafkaConsumer<>("rfid", new VehicleEventSchema(), properties);
        kafkaConsumer.setStartFromEarliest();
        kafkaConsumer.assignTimestampsAndWatermarks(new BoundedOutOfOrdernessGenerator());
        DataStreamSource<VehicleEvent> vehicleEvents = env.addSource(kafkaConsumer);
        vehicleEvents.print("before window: ");
        SingleOutputStreamOperator<Object> process = vehicleEvents.rebalance()
                .keyBy(VehicleEvent::getVin)
                .window(TumblingEventTimeWindows.of(Time.seconds(4)))
                .trigger(MyEventTimeTrigger.create())
                .process(new ProcessWindowFunction<VehicleEvent, Object, String, TimeWindow>() {
                    @Override
                    public void process(String s, Context context, Iterable<VehicleEvent> iterable, Collector<Object> collector) throws Exception {
                        StreamSupport.stream(iterable.spliterator(), false).sorted().forEach(collector::collect);
                    }
                });
        process.print("after window: ");
        env.execute(KafkaWindowExample.class.getSimpleName());
    }


}
