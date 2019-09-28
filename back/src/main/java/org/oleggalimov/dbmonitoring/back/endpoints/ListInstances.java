package org.oleggalimov.dbmonitoring.back.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.oleggalimov.dbmonitoring.back.builders.ResponceBuilder;
import org.oleggalimov.dbmonitoring.back.dto.implementations.RestResponceBody;
import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonDbInstance;
import org.oleggalimov.dbmonitoring.back.enumerations.BodyItemKeys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CopyOnWriteArraySet;

@RestController
public class ListInstances {
    private final CopyOnWriteArraySet<CommonDbInstance> instanceSet;
    private final ResponceBuilder responceBuilder;

    @Autowired
    public ListInstances(CopyOnWriteArraySet<CommonDbInstance> instanceSet, ResponceBuilder builder) {
        this.instanceSet = instanceSet;
        this.responceBuilder = builder;
    }

    @GetMapping("/list")
    public String getInstances() throws JsonProcessingException {
        try {
            RestResponceBody body = new RestResponceBody();
            if (instanceSet == null) {
                throw new Exception("Instances not configured!");
            } else {
                body.setItem(BodyItemKeys.INSTANCES.toString(), instanceSet);
                return responceBuilder.buildRestResponce(true, body, null, null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return responceBuilder.buildExceptionResponce(ex);
        }
    }
}
