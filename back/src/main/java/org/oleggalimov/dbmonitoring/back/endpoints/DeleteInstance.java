package org.oleggalimov.dbmonitoring.back.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.oleggalimov.dbmonitoring.back.builders.ResponseBuilder;
import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonDbInstance;
import org.oleggalimov.dbmonitoring.back.enumerations.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.concurrent.CopyOnWriteArraySet;

@RestController
public class DeleteInstance {
    private final CopyOnWriteArraySet<CommonDbInstance> instanceSet;
    private final ResponseBuilder responseBuilder;

    @Autowired
    public DeleteInstance(CopyOnWriteArraySet<CommonDbInstance> instanceSet, ResponseBuilder builder) {
        this.instanceSet = instanceSet;
        this.responseBuilder = builder;
    }

    @DeleteMapping("deleteinstance/{id}")
    public String getInstance(@PathVariable String id) throws JsonProcessingException {
        try {
            if (id == null || id.equals("")) {
                throw new Exception("Id is incorrect or empty.");
            } else if (instanceSet == null) {
                throw new Exception("Instances not configured!");
            }
            boolean wasRemoved = false;
            for (CommonDbInstance instance : instanceSet) {
                if (instance.getId().equals(id)) {
                    wasRemoved = instanceSet.remove(instance);
                    break;
                }
            }
            if (!wasRemoved) {
                responseBuilder.buildErrorResponse(Collections.singletonList(Messages.DBINSTANCE_IS_NOT_DELETED.getMessageObject()));
                throw new Exception("Instance was not deleted due to error");
            }
            return responseBuilder.buildRestResponse(true, null, null, Collections.singletonList(Messages.DBINSTANCE_DELETED.getMessageObject()));
        } catch (Exception ex) {
            ex.printStackTrace();
            return responseBuilder.buildExceptionResponse(ex);
        }
    }
}