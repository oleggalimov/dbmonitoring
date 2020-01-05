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
            Query createDB = new Query("CREATE DATABASE "+dbName);
            influxDB.query(createDB);
        }
        metrics.getSecond().forEach(point -> influxDB.write(dbName,"autogen", point));
    }

    private boolean databaseExists(String dbName) {
        Query selectDb = new Query("SHOW DATABASES");
        LOGGER.debug("Checking for database ({}) exists", dbName);
        QueryResult databases = influxDB.query(selectDb);
        LOGGER.debug("List of databases: {}", databases);
        return false;
    }
}
