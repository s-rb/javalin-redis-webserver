package ru.list.surkovr.api;

import java.util.List;

public class VisitedLinksRequest {

    private List<String> links;

    public VisitedLinksRequest() {
    }

    public VisitedLinksRequest(List<String> links) {
        this.links = links;
    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
        this.links = links;
    }
}
