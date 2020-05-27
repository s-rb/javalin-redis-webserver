package ru.list.surkovr.model;

import ru.list.surkovr.api.VisitedLinksRequest;

import java.util.List;

public interface Repository {

    void saveObject(VisitedLinksRequest vLinks);

    List<String> getVisitedLinks(long from, long to);
}
