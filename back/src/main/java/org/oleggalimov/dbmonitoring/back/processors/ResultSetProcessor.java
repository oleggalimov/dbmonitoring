package org.oleggalimov.dbmonitoring.back.processors;

import org.influxdb.dto.Point;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public interface ResultSetProcessor {
    Map<String, Point> transformResult(ResultSet resultSet, String dbName, String measurement) throws SQLException;
}
