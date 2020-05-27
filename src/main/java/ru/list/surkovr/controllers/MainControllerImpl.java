package ru.list.surkovr.controllers;

import io.javalin.http.Context;
import ru.list.surkovr.api.Response;
import ru.list.surkovr.api.ResponseDomains;
import ru.list.surkovr.api.VisitedLinksRequest;
import ru.list.surkovr.model.Repository;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class MainControllerImpl implements MainController {

    @Inject
    @Named("REGEX_PROTOCOL")
    private String regexProtocol;
    private final Repository repository;

    @Inject
    public MainControllerImpl(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void saveVisitedLinks(Context context) {
        VisitedLinksRequest vLinks;
        try {
            vLinks = context.bodyAsClass(VisitedLinksRequest.class);
            Optional.of(vLinks.getLinks()).orElseThrow();
            repository.saveObject(vLinks);
            context.status(200);
            context.json(new Response("ok"));
        } catch (Exception e) {
            e.printStackTrace();
            context.status(400);
            context.json(new Response("wrong request body"));
        }
    }

    @Override
    public void getVisitedDomains(Context context) {
        long from;
        long to;
        try {
            String fromS = Optional.ofNullable(context.queryParam("from")).orElseThrow();
            from = Long.parseLong(fromS);
            String toS = Optional.ofNullable(context.queryParam("to")).orElseThrow();
            to = Long.parseLong(toS);
        } catch (Exception e) {
            e.printStackTrace();
            context.status(400);
            context.json(new Response("Wrong parameters"));
            return;
        }
        List<String> links = repository.getVisitedLinks(from, to);
        if (links == null || links.isEmpty()) {
            context.status(200);
            context.json(new Response("There are no links"));
        } else {
            context.status(200);
            context.json(new ResponseDomains("ok", filterUniqueLinks(links)));
        }
    }

    private List<String> filterUniqueLinks(List<String> links) {
        return links.stream().map(s -> {
            String[] temp = s.replaceAll(regexProtocol, "")
                    .replaceAll("\\:.+", "").replaceAll("\\?.+", "")
                    .split("/")[0]
                    .split("\\."); // Удаляет субдомены, оставляем вид - google.ru
            int last = temp.length - 1;
            return last > 0 ? temp[last - 1] + "." + temp[last] : temp[last];
        }).distinct().collect(Collectors.toList());
    }
}
