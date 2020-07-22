package org.oleggalimov.dbmonitoring.back.enumerations.oracle;
//TODO: код с душком, переделать на процедуры или вынести в конфигурацию.
public enum OracleAwrSQLQueries {
    AWR_PARAMS (" SELECT MIN(SNAPSHOTS.snap_id) AS MIN_SNAP_ID, MAX(SNAPSHOTS.snap_id) AS MAX_SNAP_ID,db_instances.dbid AS DB_ID, snapshots.instance_number AS INSTANCE_NUMBER\n" +
            " FROM DBA_HIST_SNAPSHOT SNAPSHOTS, DBA_HIST_DATABASE_INSTANCE DB_INSTANCES\n" +
            " WHERE \n" +
            " DB_INSTANCES.INSTANCE_NAME=? \n" +
            " AND SNAPSHOTS.begin_interval_time >= TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS.FF')\n" +
            " AND SNAPSHOTS.begin_interval_time <= TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS.FF')\n" +
            " AND DB_INSTANCES.DBID = SNAPSHOTS.DBID AND DB_INSTANCES.INSTANCE_NUMBER = SNAPSHOTS.INSTANCE_NUMBER  AND DB_INSTANCES.STARTUP_TIME = SNAPSHOTS.STARTUP_TIME\n" +
            "GROUP BY db_instances.dbid, snapshots.instance_number"),
    AWR_GET_DATA("SELECT OUTPUT FROM TABLE (SYS.DBMS_WORKLOAD_REPOSITORY.awr_report_html(?,  ?, ?, ?))"),
    AWR_GET_ID_BOUND("SELECT MIN(SNAP_ID) AS SNAP_ID FROM DBA_HIST_SNAPSHOT WHERE snap_id IN (?, ?) AND DBID=?");

    private String query;

    OracleAwrSQLQueries(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
