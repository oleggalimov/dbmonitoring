package org.oleggalimov.dbmonitoring.back.dto;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Setter
public class RestResponseBody {

    private Map<String, Object> items = new HashMap<>();

    @JsonAnySetter
    public void setItem(String key, Object item) {
        this.items.put(key, item);
    }

    @JsonAnyGetter
    public Map<String, Object> getItems() {
        return items;
    }
}
