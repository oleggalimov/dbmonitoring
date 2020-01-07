package org.oleggalimov.dbmonitoring.back.schedule.tasks;

import org.influxdb.dto.Point;
import org.oleggalimov.dbmonitoring.back.dto.DataBaseInstance;
import org.oleggalimov.dbmonitoring.back.processors.AbstractResultSetProcessorFactory;
import org.oleggalimov.dbmonitoring.back.processors.ResultSetProcessor;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.data.util.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostgresDatabaseRequestTask extends AbstractDatabaseRequestTask {

    PostgresDatabaseRequestTask(DataBaseInstance instance) {
        super(instance);
    }

    @Override
    public Pair<String, List<Point>> call() throws Exception {
        ResultSetProcessor processor = AbstractResultSetProcessorFactory.getProcessor(instance.getType());
        if (processor == null) {
            LOGGER.error("Can't get ResultSetProcessor for instance: {}", instance);
            return null;
        }
        LOGGER.debug("Executing request to {} with params: {}", instance.getType(), this.instance.toString());
        final String REQUEST_STAT = "select * from pg_stat_database where datname=?";
        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setServerNames(new String[]{instance.getHost()});
        pgSimpleDataSource.setPortNumbers(new int[]{instance.getPort()});
        pgSimpleDataSource.setDatabaseName(instance.getDatabase());
        pgSimpleDataSource.setUser(instance.getUser().getLogin());
        pgSimpleDataSource.setPassword(instance.getUser().getPassword());
        Map<String, Point> result = new HashMap<>();
        try (Connection connection = pgSimpleDataSource.getConnection()) {
            PreparedStatement statSqlRequest = connection.prepareStatement(REQUEST_STAT);
            statSqlRequest.setString(1, instance.getDatabase());
            ResultSet resultSet = statSqlRequest.executeQuery();
            return processor.transformResult(resultSet, instance.getId(), "pg_stat_database");
        } catch (Exception ex) {
            LOGGER.error("Exception in PostgresDatabaseRequestTask: {}", ex.getMessage());
            return null;
        }
    }
}
