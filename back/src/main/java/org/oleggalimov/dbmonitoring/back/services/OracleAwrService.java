package org.oleggalimov.dbmonitoring.back.services;

import oracle.jdbc.pool.OracleDataSource;
import org.oleggalimov.dbmonitoring.back.dto.DataBaseInstance;
import org.oleggalimov.dbmonitoring.back.entities.AwrDataParams;
import org.oleggalimov.dbmonitoring.back.utils.AbstractDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;

import static org.oleggalimov.dbmonitoring.back.enumerations.oracle.OracleAwrSQLQueries.AWR_GET_DATA;
import static org.oleggalimov.dbmonitoring.back.enumerations.oracle.OracleAwrSQLQueries.AWR_GET_ID_BOUND;
import static org.oleggalimov.dbmonitoring.back.enumerations.oracle.OracleAwrSQLQueries.AWR_PARAMS;

@Service
public class OracleAwrService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    ThreadLocal<Connection> connectionThreadLocal = new ThreadLocal<>();

    public boolean initConnection(DataBaseInstance instance) {
        connectionThreadLocal.remove();
        return setConnection(instance);
    }

    public AwrDataParams selectParams(String dbName, long beginInterval, long endInterval) {
        try {
            PreparedStatement preparedStatement = connectionThreadLocal.get().prepareStatement(AWR_PARAMS.getQuery());
            preparedStatement.setString(1, dbName);
            preparedStatement.setString(2, new Timestamp(beginInterval).toString());
            preparedStatement.setString(3, new Timestamp(endInterval).toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet == null || !resultSet.next()) {
                LOGGER.error("There is no awr-records");
                return null;
            }
            AwrDataParams awrDataParams = new AwrDataParams();
            awrDataParams.setInstanceNumber(resultSet.getLong("INSTANCE_NUMBER"));
            awrDataParams.setDbId(resultSet.getLong("DB_ID"));
            awrDataParams.setStartId(resultSet.getLong("MIN_SNAP_ID"));
            awrDataParams.setEndId(resultSet.getLong("MAX_SNAP_ID"));
            return awrDataParams;
        } catch (SQLException e) {
            LOGGER.error("Error on selecting AWR params: ", e);
            connectionThreadLocal.remove();
            return null;
        }
    }

    public String getAwr(AwrDataParams params) {
        try {
            Connection connection = this.connectionThreadLocal.get();
            Long nearSnapId = null;
            if (Objects.equals(params.getStartId(), params.getEndId())) {
                //проверить null. в общем случае - совпала начальная и конечная дата
                //пробуем извлечть +- id
                PreparedStatement statement = connection.prepareStatement(AWR_GET_ID_BOUND.getQuery());
                statement.setLong(1, params.getStartId() - 1);
                statement.setLong(2, params.getStartId() + 1);
                statement.setLong(3, params.getDbId());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet == null || !resultSet.next() || resultSet.getString(1) == null) {
                    LOGGER.error("No other snapshots exists. Can't get data from single AWR");
                    return null;
                }
                nearSnapId = resultSet.getLong("SNAP_ID");

            }
            PreparedStatement statement = connection.prepareStatement(AWR_GET_DATA.getQuery());
            statement.setLong(1, params.getDbId());
            statement.setLong(2, params.getInstanceNumber());
            if (nearSnapId != null) {
                Long minSnapId = params.getStartId() < nearSnapId ? params.getStartId() : nearSnapId;
                Long maxSnapId = params.getStartId() > nearSnapId ? params.getStartId() : nearSnapId;
                statement.setLong(3, minSnapId);
                statement.setLong(4, maxSnapId);
            } else {
                statement.setLong(3, params.getStartId());
                statement.setLong(4, params.getEndId());
            }
            ResultSet resultSet = statement.executeQuery();
            StringBuilder builder = new StringBuilder();
            while (resultSet.next()) {
                builder.append(resultSet.getString("OUTPUT"));
            }
            if (builder.length() == 0) {
                LOGGER.error("No data from AWR select");
                return null;
            }
            return builder.toString();
        } catch (SQLException e) {
            LOGGER.error("Exception on AWR select: ", e);
            return null;
        }
    }

    public void clearConnection() {
        connectionThreadLocal.remove();
    }

    private boolean setConnection(DataBaseInstance instance) {
        OracleDataSource oracleDataSource = (OracleDataSource) AbstractDataSourceFactory.buildDataSource(instance);
        if (oracleDataSource == null) {
            LOGGER.error("Exception in OracleDatabaseRequestTask: data source is null");
            return false;
        }
        try {
            Connection connection = oracleDataSource.getConnection();
            connectionThreadLocal.set(connection);
            return true;
        } catch (SQLException e) {
            LOGGER.error("Can't set connection: ", e);
            return false;
        }
    }
}
