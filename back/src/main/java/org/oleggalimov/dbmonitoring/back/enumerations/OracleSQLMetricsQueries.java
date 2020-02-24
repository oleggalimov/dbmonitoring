package org.oleggalimov.dbmonitoring.back.enumerations;
//TODO: код с душком, переделать на процедуры или вынести в конфигурацию.
public enum OracleSQLMetricsQueries {
    WAIT_CLASS(
            "SELECT n.wait_class, Round(m.time_waited / m.intsize_csec, 3) VALUE "
                    + "FROM   v$waitclassmetric m, v$system_wait_class n WHERE  m.wait_class_id = n.wait_class_id AND n.wait_class != 'Idle' "
                    + "UNION SELECT 'CPU', Round(value / 100, 3) VALUE FROM   v$sysmetric WHERE  metric_name = 'CPU Usage Per Sec' AND group_id = 2 "
                    + "UNION SELECT 'CPU_OS', ( Round(( prcnt.busy * parameter.cpu_count ) / 100, 3) - aas.cpu ) VALUE "
                    + "FROM   (SELECT value busy FROM   v$sysmetric WHERE  metric_name = 'Host CPU Utilization (%)' AND group_id = 2) prcnt, "
                    + "       (SELECT value cpu_count FROM   v$parameter WHERE  name = 'cpu_count') parameter, "
                    + "       (SELECT 'CPU', Round(value / 100, 3) cpu FROM   v$sysmetric WHERE  metric_name = 'CPU Usage Per Sec' AND group_id = 2) aas"),

    WAIT_EVENT(
            "SELECT n.wait_class wait_class, n.name wait_name, m.wait_count cnt, Round(10 * m.time_waited / Nullif(m.wait_count, 0), 3) avgms "
                    + "FROM   v$eventmetric m, v$event_name n WHERE  m.event_id = n.event_id AND n.name = 'enq: HW - contention'"),
    RELATIVE_SYSTEM("SELECT metric_name, value, metric_unit FROM   v$sysmetric WHERE  group_id = 2 AND metric_unit LIKE '%!%%' ESCAPE '!'"),
    ABSOLUTE_SYSTEM_CUSTOM("SELECT metric_name, value, metric_unit FROM   v$sysmetric WHERE  group_id = 2 AND metric_name in ('Physical Reads Per Sec', 'Physical Writes Per Sec', 'Logons Per Sec', 'Open Cursors Per Sec', 'User Commits Per Sec', 'User Rollbacks Per Sec', 'Redo Writes Per Sec', 'Physical Read Total IO Requests Per Sec', 'Executions Per Sec')"),
    TABLESPACE("SELECT tablespace_name, total_space, free_space, perc_used, percextend_used, max_size_mb, free_space_extend "
            + "FROM   (SELECT t1.tablespace_name, "
            + "               Round(used_space / 1024 / 1024) total_space, "
            + "               Round(Nvl(lib, 0) / 1024 / 1024) free_space, "
            + "               Round(100 * ( used_space - Nvl(lib, 0) ) / used_space, 1) perc_used, "
            + "               Round(100 * ( used_space - Nvl(lib, 0) ) / smax_bytes, 1) percextend_used, "
            + "               Round(Nvl(smax_bytes, 0) / 1024 / 1024) max_size_mb, "
            + "               Round(Nvl(smax_bytes - ( used_space - Nvl(lib, 0) ), 0) / 1024 / 1024) free_space_extend, "
            + "               nb_ext nb_ext "
            + "        FROM   (SELECT tablespace_name, SUM(bytes) used_space "
            + "                FROM   dba_data_files i "
            + "                GROUP  BY tablespace_name) t1, "
            + "               (SELECT tablespace_name, SUM(bytes)   lib, Max(bytes)   max_nb, Count(bytes) nb_ext "
            + "                FROM   dba_free_space "
            + "                GROUP  BY tablespace_name) t2, "
            + "               (SELECT tablespace_name, SUM(max_bytes) smax_bytes "
            + "                FROM   (SELECT tablespace_name, CASE "
            + "                                 WHEN autoextensible = 'YES' THEN "
            + "                                 Greatest(bytes, maxbytes) "
            + "                                 ELSE bytes "
            + "                               END max_bytes "
            + "                        FROM   dba_data_files i) "
            + "                GROUP  BY tablespace_name) t3 "
            + "        WHERE  t1.tablespace_name = t2.tablespace_name(+) AND t1.tablespace_name = t3.tablespace_name(+))");

    private String query;

    OracleSQLMetricsQueries(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
