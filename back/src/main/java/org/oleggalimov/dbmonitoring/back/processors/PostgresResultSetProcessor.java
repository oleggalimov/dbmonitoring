package org.oleggalimov.dbmonitoring.back.processors;

import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PostgresResultSetProcessor implements ResultSetProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostgresResultSetProcessor.class);
    private static final List<String> KEYS = Arrays.asList(
            "datid", "numbackends", "xact_commit", "xact_rollback", "blks_read", "blks_hit", "tup_returned",
            "tup_fetched", "tup_inserted", "tup_updated", "tup_deleted", "conflicts", "temp_files", "temp_bytes", "deadlocks",
            "blk_read_time", "blk_write_time");

    @Override
    public Pair<String, List<Point>> transformResult(ResultSet resultSet, String instanceId, String measurement) {
        List<Point> points = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Point.Builder point = Point.measurement(measurement);
                point.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
                for (String key : KEYS) {
                    if (key.equalsIgnoreCase("blk_read_time") || key.equalsIgnoreCase("blk_write_time")) {
                        point.addField(key, resultSet.getDouble(key));
                    } else {
                        point.addField(key, resultSet.getLong(key));
                    }
                }
                points.add(point.build());
            }
            return Pair.of(instanceId, points);
        } catch (SQLException ex) {
            LOGGER.error("SQLException with postgres result set : {}", ex.getMessage());
            return null;
        }
    }
}
