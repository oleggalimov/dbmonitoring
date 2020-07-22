package org.oleggalimov.dbmonitoring.back.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.oleggalimov.dbmonitoring.back.enumerations.DatabaseInstanceType;
import org.oleggalimov.dbmonitoring.back.enumerations.InfluxTimePeriod;

@Data
@NoArgsConstructor
public class PointsRequest {
    private String instanceId;
    private DatabaseInstanceType databaseInstanceType;
    private String measurement;
    private InfluxTimePeriod timePeriod;
}
