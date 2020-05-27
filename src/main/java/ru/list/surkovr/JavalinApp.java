package ru.list.surkovr;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import io.javalin.Javalin;
import ru.list.surkovr.controllers.MainController;

import static io.javalin.apibuilder.ApiBuilder.*;

public class JavalinApp {

    @Inject
    @Named("APP_PORT")
    public int appPort;
    @Inject
    @Named("URL_VISITED_LINKS")
    public String urlVisitedLinks;
    @Inject
    @Named("URL_VISITED_DOMAINS")
    public String urlVisitedDomains;
    @Inject
    @Named("APP_HOST")
    public String appHost;
    private MainController mainController;
    private Javalin app;

    @Inject
    public JavalinApp(MainController mainController) {
        this.mainController = mainController;
    }

    public void start() {
        app = Javalin.create();
        app.start(appHost, appPort);
        app.routes(() -> {
            path(urlVisitedLinks, () -> {
                post(ctx -> mainController.saveVisitedLinks(ctx));
            });
            path(urlVisitedDomains, () -> {
                get(ctx -> mainController.getVisitedDomains(ctx));
            });
        });
    }

    public void stop() {
        app.stop();
    }
}
