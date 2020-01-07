package org.oleggalimov.dbmonitoring.back.services;

import org.influxdb.InfluxDB;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InfluxMetricsPersister {
    private static final Logger LOGGER = LoggerFactory.getLogger(InfluxMetricsPersister.class);
    private InfluxDB influxDB;

    @Autowired
    public InfluxMetricsPersister(InfluxDB influxDB) {
        this.influxDB = influxDB;
    }

    public void persistMetrics(Pair<String, List<Point>> metrics) {
        String dbName = metrics.getFirst();
        if (!databaseExists(dbName)) {
            Query createDB = new Query("CREATE DATABASE " + dbName);
            influxDB.query(createDB);
        }
        metrics.getSecond().forEach(point -> influxDB.write(dbName, "autogen", point));
    }

    private boolean databaseExists(String dbName) {
        Query selectDb = new Query("SHOW DATABASES");
        LOGGER.debug("Checking for database ({}) exists", dbName);
        List<QueryResult.Result> resultList = influxDB.query(selectDb).getResults();
        if (resultList == null || resultList.isEmpty()) {
            return false;
        }
        try {
            return resultList.stream()
                    .map(QueryResult.Result::getSeries)
                    .collect(Collectors.toList())
                    .stream()
                    .flatMap(List::stream)
                    .map(QueryResult.Series::getValues)
                    .flatMap(List::stream)
                    .flatMap(List::stream)
//                to collect all values to list use this
//                .collect(Collectors.toList());
                    .anyMatch(dbName::equals);
        } catch (Exception ex) {
            LOGGER.error("Error on processing  database exists resultSet");
            ex.printStackTrace();
            return false;
        }
    }
}
