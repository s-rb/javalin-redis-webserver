package ru.list.surkovr.controllers;

import io.javalin.http.Context;

public interface MainController {

    void saveVisitedLinks(Context context);

    void getVisitedDomains(Context context);
}
