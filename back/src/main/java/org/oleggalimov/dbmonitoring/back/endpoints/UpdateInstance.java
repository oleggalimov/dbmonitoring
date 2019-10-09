package org.oleggalimov.dbmonitoring.back.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.oleggalimov.dbmonitoring.back.builders.ResponseBuilder;
import org.oleggalimov.dbmonitoring.back.dto.implementations.DbInstanceImpl;
import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonDbInstance;
import org.oleggalimov.dbmonitoring.back.enumerations.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.concurrent.CopyOnWriteArraySet;

@RestController
public class UpdateInstance {
    private final CopyOnWriteArraySet<CommonDbInstance> instanceSet;
    private final ResponseBuilder responseBuilder;

    @Autowired
    public UpdateInstance(CopyOnWriteArraySet<CommonDbInstance> instanceSet, ResponseBuilder builder) {
        this.instanceSet = instanceSet;
        this.responseBuilder = builder;
    }

    @PutMapping(value = "updateinstance")
    public String update(@RequestBody(required = true) DbInstanceImpl requestInstance) throws JsonProcessingException {
        try {
            if (instanceSet == null) {
                throw new Exception("Instances not configured!");
            } else if (!instanceSet.contains(requestInstance)) {
                throw new Exception("No such instance!");
            }
            instanceSet.remove(requestInstance);
            instanceSet.add(requestInstance);
            return responseBuilder.buildRestResponse(true, null, null, Collections.singletonList(Messages.DBINSTANCE_UPDATED.getMessageObject()));
        } catch (Exception ex) {
            ex.printStackTrace();
            return responseBuilder.buildExceptionResponse(ex);
        }
    }
}
