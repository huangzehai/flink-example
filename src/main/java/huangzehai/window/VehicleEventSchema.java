package huangzehai.window;

import huangzehai.model.VehicleEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.serialization.DeserializationSchema;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class VehicleEventSchema implements DeserializationSchema<VehicleEvent>, SerializationSchema<VehicleEvent> {

    @Override
    public VehicleEvent deserialize(byte[] bytes) throws IOException {
        String line = new String(bytes, Charset.forName("UTF-8"));
        String[] items = line.split(",");
        VehicleEvent vehicleEvent = new VehicleEvent();
        vehicleEvent.setId(Integer.valueOf(StringUtils.trim(items[0])));
        vehicleEvent.setVin(StringUtils.trim(items[1]));
        vehicleEvent.setDateTime(LocalDateTime.parse(StringUtils.trim(items[2])));
        vehicleEvent.setEvent(StringUtils.trim(items[3]));
        vehicleEvent.setAlerts(StringUtils.trim(items[4]));
        long timestamp = vehicleEvent.getDateTime().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        vehicleEvent.setTimestamp(timestamp);
        return vehicleEvent;
    }

    @Override
    public boolean isEndOfStream(VehicleEvent vehicleEvent) {
        return false;
    }

    @Override
    public byte[] serialize(VehicleEvent vehicleEvent) {
        return new byte[0];
    }

    @Override
    public TypeInformation<VehicleEvent> getProducedType() {
        return TypeInformation.of(VehicleEvent.class);
    }
}
