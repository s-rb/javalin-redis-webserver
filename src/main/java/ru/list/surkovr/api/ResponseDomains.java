package ru.list.surkovr.api;

import java.util.List;

public class ResponseDomains extends Response {

    private List<String> domains;

    public ResponseDomains() {
    }

    public ResponseDomains(String status, List<String> domains) {
        super(status);
        this.domains = domains;
    }

    public List<String> getDomains() {
        return domains;
    }

    public void setDomains(List<String> domains) {
        this.domains = domains;
    }

    public String getStatus() {
        return super.getStatus();
    }

    public void setStatus(String status) {
        super.setStatus(status);
    }
}
