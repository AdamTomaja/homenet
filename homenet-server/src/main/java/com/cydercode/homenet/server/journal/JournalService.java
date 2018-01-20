package com.cydercode.homenet.server.journal;

import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.influxdb.InfluxDBTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class JournalService {

    @Autowired
    private JournalRowRepository repository;

    @Autowired
    private InfluxDBTemplate<Point> influxDBTemplate;


    public void write(String name, String description) {
        repository.save(JournalRow.builder()
                .name(name)
                .description(description)
                .date(new Date())
                .build());
    }

    public void saveMeasurement(String measurement, String tag, Map<String, Object> fields) {
        Point.Builder pointBuilder = Point.measurement(measurement)
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .tag("tag", tag);

        fields.forEach((k, v) -> {
            if (v instanceof Double) {
                pointBuilder.addField(k, (Double) v);
            }

            if (v instanceof Integer) {
                pointBuilder.addField(k, (Integer) v);
            }

            if (v instanceof String) {
                pointBuilder.addField(k, (String) v);
            }
        });

        influxDBTemplate.write(pointBuilder.build());
    }
}
