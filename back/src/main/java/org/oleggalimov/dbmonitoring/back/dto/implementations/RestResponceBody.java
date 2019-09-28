package org.oleggalimov.dbmonitoring.back.dto.implementations;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import org.oleggalimov.dbmonitoring.back.dto.interfaces.CommonBody;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RestResponceBody implements CommonBody, Serializable {

    private Map<String, Object> items = new HashMap<>();

    public RestResponceBody() {
    }

    public RestResponceBody(HashMap<String, Object> items) {
        this.items = items;
    }

    public void setItems(HashMap<String, Object> items) {
        this.items = items;
    }

    public void setItem(String key, Object item) {
        this.items.put(key, item);
    }

    @Override
    @JsonAnyGetter
    public Map getItems() {
        return items;
    }
}
