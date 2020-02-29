package org.oleggalimov.dbmonitoring.back.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.influxdb.InfluxDB;
import org.influxdb.dto.QueryResult;
import org.oleggalimov.dbmonitoring.back.builders.ResponseBuilder;
import org.oleggalimov.dbmonitoring.back.dto.PointsRequest;
import org.oleggalimov.dbmonitoring.back.dto.RestResponseBody;
import org.oleggalimov.dbmonitoring.back.enumerations.BodyItemKey;
import org.oleggalimov.dbmonitoring.back.enumerations.Messages;
import org.oleggalimov.dbmonitoring.back.utils.InfluxQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
public class Points {
    private InfluxDB influxDB;
    private final ResponseBuilder responseBuilder;

    @Autowired
    public Points(InfluxDB influxDB, ResponseBuilder responseBuilder) {
        this.influxDB = influxDB;
        this.responseBuilder = responseBuilder;
    }

    @Secured(value = {"ROLE_ADMIN", "ROLE_USER"})
    @PostMapping("/points")
    public String getPoints(@RequestBody PointsRequest request) throws JsonProcessingException {
        StringBuilder stringBuilder = new StringBuilder();
        QueryResult queryResult = null;
        try {
            switch (request.getDatabaseInstanceType()) {
                case MSSQL:
                case MYSQL:
                    return "";
                case ORACLE:
                    queryResult = influxDB.query(InfluxQueryBuilder.getOracleMetricsQuery(request));
                    break;
                case POSTGRES:
                    queryResult = influxDB.query(InfluxQueryBuilder.getPostgresMetricsQuery(request));
            }
            if (queryResult == null) {
                return responseBuilder.buildRestResponse(false, null, null, Collections.singletonList(Messages.INFLUX_NO_DATA_RETURNED.getMessageObject()));
            } else {
                RestResponseBody body = new RestResponseBody();
                body.setItem(BodyItemKey.POINTS.toString(), Collections.singletonList(queryResult));
                return responseBuilder.buildRestResponse(true, body, null, null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return responseBuilder.buildExceptionResponse(ex);
        }
    }
}
