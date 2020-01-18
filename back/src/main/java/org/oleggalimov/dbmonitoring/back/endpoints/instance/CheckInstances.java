package org.oleggalimov.dbmonitoring.back.endpoints.instance;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.oleggalimov.dbmonitoring.back.builders.ResponseBuilder;
import org.oleggalimov.dbmonitoring.back.dto.DataBaseInstance;
import org.oleggalimov.dbmonitoring.back.dto.RestResponseBody;
import org.oleggalimov.dbmonitoring.back.enumerations.BodyItemKey;
import org.oleggalimov.dbmonitoring.back.enumerations.DatabaseInstanceType;
import org.oleggalimov.dbmonitoring.back.utils.AbstractDataSourceFactory;
import org.oleggalimov.dbmonitoring.back.utils.TypedStatusQueryMapBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@RestController
public class CheckInstances {
    private final Logger LOGGER = LoggerFactory.getLogger(CheckInstances.class);
    private final EnumMap<DatabaseInstanceType, String> queries = TypedStatusQueryMapBuilder.getTypeQueryMap();

    private final CopyOnWriteArraySet<DataBaseInstance> instanceSet;
    private final ResponseBuilder responseBuilder;

    @Autowired
    public CheckInstances(CopyOnWriteArraySet<DataBaseInstance> instanceSet, ResponseBuilder responseBuilder) {
        this.instanceSet = instanceSet;
        this.responseBuilder = responseBuilder;
    }

    @GetMapping("check/instance/all")
    public String checkInstances() throws JsonProcessingException {
        try {
            Set<DataBaseInstance> resultSet = new HashSet<>();
            for (DataBaseInstance instance : instanceSet) {
                DataSource dataSource = AbstractDataSourceFactory.buildDataSource(instance);
                String query = queries.get(instance.getType());
                if (dataSource == null || query == null) {
                    LOGGER.error("Can't create data source (value = {}), or query is null (value = {})", dataSource, query);
                    DataBaseInstance temp = instance.clone();
                    temp.setStatus("Error with creation data source");
                    resultSet.add(temp);
                    continue;
                }
                try (Connection connection = dataSource.getConnection()) {
                    boolean result = connection.prepareStatement(query).execute();
                    DataBaseInstance temp = instance.clone();
                    if (result) {
                        temp.setStatus("OK");

                    } else {
                        temp.setStatus("FAIL");
                    }
                    resultSet.add(temp);
                } catch (SQLException e) {
                    LOGGER.error("Error on status check for instance: {}, error: {}", instance, e.getMessage());
                    DataBaseInstance temp = instance.clone();
                    temp.setStatus(e.getMessage());
                    resultSet.add(temp);
                }
            }
            RestResponseBody body = new RestResponseBody();
            body.setItem(BodyItemKey.INSTANCES.toString(), resultSet);
            return responseBuilder.buildRestResponse(true, body, null, null);
        } catch (Exception ex) {
            return responseBuilder.buildExceptionResponse(ex);
        }
    }
}
