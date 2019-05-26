package huangzehai.source;

import huangzehai.model.VehicleEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Iterator;
import java.util.stream.Stream;

public class CsvSource extends RichSourceFunction<VehicleEvent> {

    private String file;

    public CsvSource(String file) {
        this.file = file;
    }

    @Override
    public void run(SourceContext<VehicleEvent> ctx) throws Exception {
        Path path = Paths.get(this.getClass().getClassLoader().getResource(file).toURI());
        Stream<String> lines = Files.lines(path);
        Iterator<String> iterator = lines.skip(1).iterator();
        VehicleEvent vehicleEvent = new VehicleEvent();
        iterator.forEachRemaining(line -> {
            String[] items = line.split(",");
            vehicleEvent.setId(Integer.valueOf(StringUtils.trim(items[0])));
            vehicleEvent.setVin(StringUtils.trim(items[1]));
            vehicleEvent.setDateTime(LocalDateTime.parse(StringUtils.trim(items[2])));
            vehicleEvent.setEvent(StringUtils.trim(items[3]));
            vehicleEvent.setAlerts(StringUtils.trim(items[4]));
            long timestamp = vehicleEvent.getDateTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();
            ctx.collectWithTimestamp(vehicleEvent, timestamp);
            ctx.emitWatermark(new Watermark(timestamp-2000));
        });
    }

    @Override
    public void cancel() {

    }
}
