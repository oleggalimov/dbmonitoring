package org.oleggalimov.dbmonitoring.back.schedule.tasks;

import org.oleggalimov.dbmonitoring.back.dto.DataBaseInstance;
import org.oleggalimov.dbmonitoring.back.enumerations.DatabaseInstanceType;

public class DatabaseRequestTaskFactory {
    public static AbstractDatabaseRequestTask getTask(DataBaseInstance instance) {
        if (instance == null) {
            return null;
        }
        DatabaseInstanceType type = instance.getType();
        switch (type) {
            case ORACLE:
                return new OracleDatabaseRequestTask(instance);
            case POSTGRES:
                return new PostgresDatabaseRequestTask(instance);
            case MYSQL:
                return new MySQLDatabaseRequestTask(instance);
            case MSSQL:
                return new MsSQLDatabaseRequestTask(instance);
            default:
                return null;
        }
    }
}
