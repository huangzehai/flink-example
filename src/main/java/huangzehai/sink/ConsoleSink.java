package huangzehai.sink;

import org.apache.flink.streaming.api.functions.sink.SinkFunction;

public class ConsoleSink<T> implements SinkFunction<T> {
    @Override
    public void invoke(T value, Context context) throws Exception {
        System.out.println("sink: " + value);
    }
}
