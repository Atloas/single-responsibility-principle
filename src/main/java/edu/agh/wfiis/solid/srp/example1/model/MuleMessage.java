package edu.agh.wfiis.solid.srp.example1.model;

import java.util.HashMap;
import java.util.Map;

public class MuleMessage {

    private Map<String, Object> inboundProperties;

    public String getHeader(String headerName) {
        return (String) inboundProperties.get(headerName);
    }

    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        for (Map.Entry<String, Object> inboundProperty : inboundProperties.entrySet()) {
            headers.put(inboundProperty.getKey(), (String) inboundProperty.getValue());
        }
        return headers;
    }

    public void setHeader(String headerName, String value) {
        if (inboundProperties == null) {
            inboundProperties = new HashMap<>();
        }
        inboundProperties.put(headerName, value);
    }
}
