package org.oleggalimov.dbmonitoring.back.entities;

import lombok.Data;

@Data
public class AwrDataParams {
    Long dbId;
    Long instanceNumber;
    Long startId;
    Long endId;
}
