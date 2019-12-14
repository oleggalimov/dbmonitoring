package org.oleggalimov.dbmonitoring.back.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.HashMap;
import java.util.Map;

public class RestResponseBody {

    private Map<String, Object> items = new HashMap<>();

    public RestResponseBody() {
    }

    public RestResponseBody(HashMap<String, Object> items) {
        this.items = items;
    }

    public void setItems(HashMap<String, Object> items) {
        this.items = items;
    }

    @JsonAnySetter
    public void setItem(String key, Object item) {
        this.items.put(key, item);
    }

    @JsonAnyGetter
    public Map getItems() {
        return items;
    }
}
