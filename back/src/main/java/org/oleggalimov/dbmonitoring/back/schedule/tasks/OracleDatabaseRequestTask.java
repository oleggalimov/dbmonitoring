package org.oleggalimov.dbmonitoring.back.schedule.tasks;


import oracle.jdbc.pool.OracleDataSource;
import org.influxdb.dto.Point;
import org.oleggalimov.dbmonitoring.back.dto.DataBaseInstance;
import org.oleggalimov.dbmonitoring.back.enumerations.oracle.OracleMetricsSQLQueries;
import org.oleggalimov.dbmonitoring.back.processors.AbstractResultSetProcessorFactory;
import org.oleggalimov.dbmonitoring.back.processors.ResultSetProcessor;
import org.oleggalimov.dbmonitoring.back.utils.AbstractDataSourceFactory;
import org.springframework.data.util.Pair;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OracleDatabaseRequestTask extends AbstractDatabaseRequestTask {

    OracleDatabaseRequestTask(DataBaseInstance instance) {
        super(instance);
    }

    @Override
    public Pair<String, List<Point>> call() {
        ResultSetProcessor processor = AbstractResultSetProcessorFactory.getProcessor(instance.getType());
        if (processor==null) {
            LOGGER.error("Can't get ResultSetProcessor for instance: {}", instance);
            return null;
        }
        String instanceId = instance.getId();
        OracleDataSource oracleDataSource = (OracleDataSource) AbstractDataSourceFactory.buildDataSource(instance);
        if (oracleDataSource==null) {
            LOGGER.error("Exception in OracleDatabaseRequestTask: data source is null");
            return null;
        }
        try (Connection connection = oracleDataSource.getConnection()) {
            List<Point> pointList = new ArrayList<>();
            for (OracleMetricsSQLQueries query : OracleMetricsSQLQueries.values()) {
                List<Point> tempPointsList = getPoints(connection, query, processor);
                if (!tempPointsList.isEmpty()) {
                    pointList.addAll(tempPointsList);
                }
            }
            return Pair.of(instanceId, pointList);
        } catch (Exception ex) {
            LOGGER.error("Exception in OracleDatabaseRequestTask: {}", ex.getMessage());
            return null;
        }
    }

    private List<Point> getPoints(Connection connection, OracleMetricsSQLQueries queryType, ResultSetProcessor processor) throws SQLException {
        LOGGER.debug("Executing request for {} metrics", queryType.name());
        ResultSet resultSet = connection.prepareStatement(queryType.getQuery()).executeQuery();
        return processor.transformResult(resultSet, instance.getId(), queryType.name()).getSecond();
    }
}
