package org.oleggalimov.dbmonitoring.back.endpoints.awr;

import oracle.sql.TIMESTAMP;
import org.oleggalimov.dbmonitoring.back.annotations.LogHttpEvent;
import org.oleggalimov.dbmonitoring.back.builders.ResponseBuilder;
import org.oleggalimov.dbmonitoring.back.dto.DataBaseInstance;
import org.oleggalimov.dbmonitoring.back.entities.AwrDataParams;
import org.oleggalimov.dbmonitoring.back.enumerations.DatabaseInstanceType;
import org.oleggalimov.dbmonitoring.back.enumerations.Errors;
import org.oleggalimov.dbmonitoring.back.services.OracleAwrService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

@RestController
public class AwrLoader {
    private final CopyOnWriteArraySet<DataBaseInstance> instanceSet;
    private final ResponseBuilder responseBuilder;
    private final OracleAwrService oracleAwrService;

    public AwrLoader(CopyOnWriteArraySet<DataBaseInstance> instanceSet, ResponseBuilder responseBuilder, OracleAwrService oracleAwrService) {
        this.instanceSet = instanceSet;
        this.responseBuilder = responseBuilder;
        this.oracleAwrService = oracleAwrService;
    }

    @GetMapping("awr/load")
    @LogHttpEvent(eventType = RequestMethod.GET, message = "awr/load")
    @Secured(value = {"ROLE_USER", "ROLE_USER_ADMIN", "ROLE_ADMIN"})
    public ResponseEntity<String> getAwr(
            @RequestParam String instanceId,
            @RequestParam long startDate,
            @RequestParam long endDate) throws IOException {
        Optional<DataBaseInstance> instanceOptional = instanceSet.stream()
                .filter(element -> element.getId().equalsIgnoreCase(instanceId))
                .findFirst();
        String responseString;
        if (!instanceOptional.isPresent()) {
            responseString = responseBuilder.buildRestResponse(false, null, Collections.singletonList(Errors.AWR_INSTANCE_NOT_FOUND.getError()), null);
            return getResponse(APPLICATION_JSON_UTF8, responseString);
        }
        DataBaseInstance instance = instanceOptional.get();
        if (instance.getType() != DatabaseInstanceType.ORACLE) {
            responseString = responseBuilder.buildRestResponse(false, null, Collections.singletonList(Errors.AWR_WRONG_INSTANCE_TYPE.getError()), null);
            return getResponse(APPLICATION_JSON_UTF8, responseString);
        }
        if (!oracleAwrService.initConnection(instance)) {
            responseString = responseBuilder.buildRestResponse(false, null, Collections.singletonList(Errors.AWR_CONNECT_ERROR.getError()), null);
            return getResponse(APPLICATION_JSON_UTF8, responseString);
        }
        AwrDataParams awrDataParams = oracleAwrService.selectParams(instance.getDatabase(), startDate, endDate);
        if (awrDataParams == null) {
            responseString = responseBuilder.buildRestResponse(false, null, Collections.singletonList(Errors.AWR_NOT_FOUND.getError()), null);
            return getResponse(APPLICATION_JSON_UTF8, responseString);
        }
        responseString = oracleAwrService.getAwr(awrDataParams);
        oracleAwrService.clearConnection();
        if (responseString == null) {
            responseString = responseBuilder.buildRestResponse(false, null, Collections.singletonList(Errors.AWR_NO_DATA.getError()), null);
            return getResponse(APPLICATION_JSON_UTF8, responseString);
        }
        return getResponse(APPLICATION_OCTET_STREAM, responseString);
    }

    private ResponseEntity<String> getResponse(MediaType responseType, String result) {

        if (responseType == APPLICATION_OCTET_STREAM) {
            return ResponseEntity
                    .ok()
                    .contentLength(result.length())
                    .contentType(APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"report.html\"")
                    .body(result);
        } else {
            return ResponseEntity
                    .ok()
                    .contentLength(result.length())
                    .contentType(APPLICATION_JSON_UTF8)
                    .body(result);
        }
    }
}
