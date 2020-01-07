package org.oleggalimov.dbmonitoring.back.services;

import org.influxdb.dto.Query;
import org.oleggalimov.dbmonitoring.back.dto.PointsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfluxQueryBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(InfluxQueryBuilder.class);

    public static Query getOracleMetricsQuery(PointsRequest request) {
        StringBuilder queryStringBuilder = new StringBuilder()
                .append("SELECT mean(\"value\") AS \"mean_value\" FROM \"")
                .append(request.getDataBaseId())
                .append("\".\"autogen\".\"")
                .append(request.getMeasurement())
                .append("\" WHERE time > ")
                .append(request.getTimePeriod().getPeriod())
                .append(" AND time < now()")
                .append(" GROUP BY time(1m), \"metric_name\" FILL(null)");
        LOGGER.debug("Prepared query for Oracle metrics: {}", queryStringBuilder.toString());
        return new Query(queryStringBuilder.toString(), request.getDataBaseId());
    }

    public static Query getPostgresMetricsQuery(PointsRequest request) {
        StringBuilder queryStringBuilder = new StringBuilder()
                .append("SELECT mean(\"blk_read_time\") AS \"mean_blk_read_time\", mean(\"blk_write_time\") AS \"mean_blk_write_time\"")
                .append(", mean(\"blks_hit\") AS \"mean_blks_hit\", mean(\"blks_red\") AS \"mean_blks_read\",")
                .append(" mean(\"datid\") AS \"mean_datid\", mean(\"conflicts\") AS \"mean_conflicts\", ")
                .append("mean(\"deadlocks\") AS \"mean_deadlocks\", mean(\"numbackends\") AS \"mean_numbackends\",")
                .append(" mean(\"temp_bytes\") AS \"mean_temp_bytes\", mean(\"temp_files\") AS \"mean_temp_files\",")
                .append(" mean(\"tup_deleted\") AS \"mean_tup_deleted\", mean(\"tup_fetched\") AS \"mean_tup_fetched\",")
                .append(" mean(\"tup_inserted\") AS \"mean_tup_inserted\", mean(\"tup_returned\") AS \"mean_tup_returned\",")
                .append(" mean(\"tup_updated\") AS \"mean_tup_updated\", mean(\"xact_commit\") AS \"mean_xact_commit\",")
                .append(" mean(\"xact_rollback\") AS \"mean_xact_rollback\" FROM \"")
                .append(request.getDataBaseId())
                .append("\".\"autogen\".\"")
                .append(request.getMeasurement())
                .append("\" WHERE time > ")
                .append(request.getTimePeriod().getPeriod())
                .append(" AND time < now()")
                .append(" GROUP BY time(1m), \"metric_name\" FILL(null)");
        LOGGER.debug("Prepared query for Postgres metrics: {}", queryStringBuilder.toString());
        return new Query(queryStringBuilder.toString(), request.getDataBaseId());
    }
}
