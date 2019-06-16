package huangzehai.window;

import huangzehai.model.VehicleEvent;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;

import java.util.Properties;

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
        FlinkKafkaConsumer010<VehicleEvent> kafkaConsumer =
                new FlinkKafkaConsumer010<>("rfid", new VehicleEventSchema(), properties);
        kafkaConsumer.setStartFromEarliest();
        kafkaConsumer.assignTimestampsAndWatermarks(new BoundedOutOfOrdernessGenerator());
        env.addSource(kafkaConsumer).print();
        env.execute(KafkaWindowExample.class.getSimpleName());
    }


}
