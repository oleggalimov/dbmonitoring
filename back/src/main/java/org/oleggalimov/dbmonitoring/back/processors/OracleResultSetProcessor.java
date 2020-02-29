package org.oleggalimov.dbmonitoring.back.processors;

import org.influxdb.dto.Point;
import org.oleggalimov.dbmonitoring.back.enumerations.OracleSQLMetricsQueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OracleResultSetProcessor implements ResultSetProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(OracleResultSetProcessor.class);


    @Override
    public Pair<String, List<Point>> transformResult(ResultSet resultSet, String instanceId, String measurement) {
        List<Point> points = new ArrayList<>();
        try {
            OracleSQLMetricsQueries metricsType = OracleSQLMetricsQueries.valueOf(measurement);
            List<Point> temp = null;
            switch (metricsType) {
                case ABSOLUTE_SYSTEM_CUSTOM:
                case RELATIVE_SYSTEM:
                    temp = parseSysMetrics(resultSet,metricsType);
                    break;
                case WAIT_EVENT:
                    temp = parseWaitEventsMetrics(resultSet);
                    break;
                case WAIT_CLASS:
                    temp = parseWaitClassMetrics(resultSet);
                    break;
                case TABLESPACE:
                    temp = parseTableSpaceMetrics(resultSet);
                    break;
            }
            if (!temp.isEmpty()) {
                points.addAll(temp);
            }
        } catch (IllegalArgumentException ex) {
            LOGGER.error("Can't get enum from string {}", measurement);
            ex.printStackTrace();
        } catch (SQLException e) {
            LOGGER.error("SQL exception on oracle metrics processing:  {}", e.getMessage());
            e.printStackTrace();
        }
        return Pair.of(instanceId, points);
    }

    private List<Point> parseSysMetrics(ResultSet resultSet, OracleSQLMetricsQueries type) throws SQLException {
        List<Point> points = new ArrayList<>();
        while (resultSet.next()) {
            String METRIC_NAME = resultSet.getString("METRIC_NAME").replace(" ", "_");
            float VALUE = resultSet.getFloat("VALUE");
            Point.Builder point = Point.measurement(type.name());
            point.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            point.tag("metric_name", METRIC_NAME);
            point.addField("value", VALUE);
            points.add(point.build());
        }
        return points;
    }

    private List<Point> parseWaitEventsMetrics(ResultSet resultSet) throws SQLException {
        List<Point> points = new ArrayList<>();
        while (resultSet.next()) {
            String WAIT_CLASS = resultSet.getString("WAIT_CLASS").replace(" ", "_");
            String WAIT_NAME = resultSet.getString("WAIT_NAME").replace(" ", "_");
            float CNT = resultSet.getFloat("CNT");
            float AVGMS = resultSet.getFloat("AVGMS");

            Point.Builder point = Point.measurement(OracleSQLMetricsQueries.WAIT_EVENT.name());
            point.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            point.tag("className", WAIT_CLASS);
            point.tag("metric_name", WAIT_NAME);
            point.addField("value", CNT);
            point.addField("latency", AVGMS);
            points.add(point.build());
        }
        return points;
    }

    private List<Point> parseWaitClassMetrics(ResultSet resultSet) throws SQLException {
        List<Point> points = new ArrayList<>();
        while (resultSet.next()) {
            String WAIT_CLASS = resultSet.getString("WAIT_CLASS").replace(" ", "_");
            float VALUE = resultSet.getFloat("VALUE");
            Point.Builder point = Point.measurement(OracleSQLMetricsQueries.WAIT_CLASS.name());
            point.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            point.tag("metric_name", WAIT_CLASS);
            point.addField("value", VALUE);
            points.add(point.build());
        }
        return points;
    }

    private List<Point> parseTableSpaceMetrics(ResultSet resultSet) throws SQLException {
        List<Point> points = new ArrayList<>();
        while (resultSet.next()) {
            String TABLESPACE_NAME = resultSet.getString("TABLESPACE_NAME").replace(" ", "_");
            Float PERC_USED = resultSet.getFloat("PERC_USED");

            Point.Builder point = Point.measurement(OracleSQLMetricsQueries.TABLESPACE.name());
            point.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            point.tag("metric_name", TABLESPACE_NAME);
            point.addField("value", PERC_USED);
            points.add(point.build());
        }
        return points;
    }


}
