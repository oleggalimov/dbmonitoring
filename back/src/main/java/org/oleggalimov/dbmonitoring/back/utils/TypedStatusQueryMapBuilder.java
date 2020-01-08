package org.oleggalimov.dbmonitoring.back.utils;

import org.oleggalimov.dbmonitoring.back.enumerations.DatabaseInstanceType;

import java.util.EnumMap;

public class TypedStatusQueryMapBuilder {
    public static EnumMap<DatabaseInstanceType, String> getTypeQueryMap() {
        EnumMap<DatabaseInstanceType, String> result = new EnumMap<>(DatabaseInstanceType.class);
        result.put(DatabaseInstanceType.ORACLE, "SELECT 1 FROM DUAL");
        result.put(DatabaseInstanceType.POSTGRES, "SELECT 1 ");
        result.put(DatabaseInstanceType.MYSQL, "SELECT 1 FROM DUAL");
        result.put(DatabaseInstanceType.MSSQL, "SELECT 1 AS [Field]");
        return result;
    }
}
