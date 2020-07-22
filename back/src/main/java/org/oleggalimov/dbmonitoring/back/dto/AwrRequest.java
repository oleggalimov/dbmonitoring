package org.oleggalimov.dbmonitoring.back.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AwrRequest {
    String instanceId;
    String beginInterval;
    String endInterval;
}
