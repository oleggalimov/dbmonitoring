package org.oleggalimov.dbmonitoring.back.processors;

import org.influxdb.dto.Point;
import org.springframework.data.util.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ResultSetProcessor {
    Pair<String, List<Point>> transformResult(ResultSet resultSet, String instanceId, String measurement);
}
