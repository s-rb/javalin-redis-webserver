package ru.list.surkovr.model;

import ru.list.surkovr.api.VisitedLinksRequest;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class RepositoryImpl implements Repository {

    private final AbstractDatabase database;

    @Inject
    public RepositoryImpl(AbstractDatabase database) {
        this.database = database;
    }

    @Override
    public void saveObject(VisitedLinksRequest vLinks) {
        database.saveObject(vLinks);
    }

    @Override
    public List<String> getVisitedLinks(long from, long to) {
        return database.getListOfLinks(from, to);
    }
}
