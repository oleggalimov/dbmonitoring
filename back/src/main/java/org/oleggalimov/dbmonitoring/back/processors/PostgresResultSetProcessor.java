package org.oleggalimov.dbmonitoring.back.processors;

import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PostgresResultSetProcessor implements ResultSetProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostgresResultSetProcessor.class);
    private static final List<String> KEYS = Arrays.asList(
            "datid", "datname", "numbackends", "xact_commit", "xact_rollback", "blks_read", "blks_hit", "tup_returned",
            "tup_fetched", "tup_inserted", "tup_updated", "tup_deleted", "conflicts", "temp_files", "temp_bytes, deadlocks",
            "blk_read_time", "blk_write_time");

    @Override
    public Map<String, Point> transformResult(ResultSet resultSet, String dbName, String measurement) throws SQLException {
        Map<String, Point> result = new HashMap<>();
        while (resultSet.next()) {
            Point.Builder point = Point.measurement(measurement);
            point.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            KEYS.forEach(key -> {
                try {
                    if (key.equalsIgnoreCase("datname")) {
                        point.addField("datname", resultSet.getString("datname"));
                    } else if (key.equalsIgnoreCase("blk_read_time") || key.equalsIgnoreCase("blk_write_time")) {
                        point.addField(key, resultSet.getDouble(key));
                    } else {
                        point.addField(key, resultSet.getLong(key));
                    }
                } catch (SQLException ex) {
                    LOGGER.debug("SQLException with key {}: {}", key, ex.getMessage());
                }

            });
            result.put(dbName, point.build());
        }
        return result;
    }
}
