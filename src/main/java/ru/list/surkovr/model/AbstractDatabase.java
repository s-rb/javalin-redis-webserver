package ru.list.surkovr.model;

import ru.list.surkovr.api.VisitedLinksRequest;

import java.util.List;

public interface AbstractDatabase {

    void init();

    void saveObject(VisitedLinksRequest vLinks);

    List<String> getListOfLinks(long from, long to);

    void close();
}
