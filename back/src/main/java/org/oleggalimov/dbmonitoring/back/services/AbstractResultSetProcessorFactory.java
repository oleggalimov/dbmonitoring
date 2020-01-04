package org.oleggalimov.dbmonitoring.back.services;

import org.oleggalimov.dbmonitoring.back.enumerations.DatabaseInstanceType;
import org.oleggalimov.dbmonitoring.back.processors.PostgresResultSetProcessor;
import org.oleggalimov.dbmonitoring.back.processors.ResultSetProcessor;

public class AbstractResultSetProcessorFactory {

    public static ResultSetProcessor getProcessor(DatabaseInstanceType type) {
        ResultSetProcessor result = null;
        switch (type) {
            case MYSQL:
            case MSSQL:
            case ORACLE:
                break;
            case POSTGRES:
                result = new PostgresResultSetProcessor();
        }
        return result;
    }

}
