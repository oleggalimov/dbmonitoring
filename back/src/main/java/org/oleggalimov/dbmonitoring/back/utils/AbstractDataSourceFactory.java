package org.oleggalimov.dbmonitoring.back.utils;

import oracle.jdbc.pool.OracleDataSource;
import org.oleggalimov.dbmonitoring.back.dto.DataBaseInstance;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;

public class AbstractDataSourceFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDataSourceFactory.class);
    public static DataSource buildDataSource(DataBaseInstance instance) {
        try {
            switch (instance.getType()) {
                case ORACLE:
                    OracleDataSource oracleDataSource = new OracleDataSource();
                    oracleDataSource.setServerName(instance.getHost());
                    oracleDataSource.setPortNumber(instance.getPort());
                    oracleDataSource.setDatabaseName(instance.getDatabase());
                    oracleDataSource.setUser(instance.getUser().getLogin());
                    oracleDataSource.setPassword(instance.getUser().getPassword());
                    oracleDataSource.setDriverType("thin");
                    return oracleDataSource;
                case POSTGRES:
                    PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
                    pgSimpleDataSource.setServerNames(new String[]{instance.getHost()});
                    pgSimpleDataSource.setPortNumbers(new int[]{instance.getPort()});
                    pgSimpleDataSource.setDatabaseName(instance.getDatabase());
                    pgSimpleDataSource.setUser(instance.getUser().getLogin());
                    pgSimpleDataSource.setPassword(instance.getUser().getPassword());
                    return pgSimpleDataSource;
                case MYSQL:
                case MSSQL:
                    return null;
            }
        } catch (SQLException e) {
            LOGGER.error("Error creating data source for instance: {}, error is: {}", instance, e.getMessage());
        }
        return null;
    }
}
