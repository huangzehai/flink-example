package huangzehai.window;

import huangzehai.model.VehicleEvent;
import huangzehai.sink.ConsoleSink;
import huangzehai.source.CsvSource;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;


public class WindowExample {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.enableCheckpointing(10000);
        DataStreamSource<VehicleEvent> messages = env.addSource(new CsvSource("messages.csv"));
        SingleOutputStreamOperator<Object> process = messages.keyBy(VehicleEvent::getVin).window(TumblingEventTimeWindows.of(Time.seconds(2L))).process(new ProcessWindowFunction<VehicleEvent, Object, String, TimeWindow>() {
            @Override
            public void process(String s, Context context, Iterable<VehicleEvent> elements, Collector<Object> out) throws Exception {
                elements.forEach(out::collect);
            }
        });
        process.addSink(new ConsoleSink<>());
        env.execute("Window Example");
    }
}
