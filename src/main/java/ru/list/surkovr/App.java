package ru.list.surkovr;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ru.list.surkovr.config.BasicModule;
import ru.list.surkovr.model.AbstractDatabase;

public class App {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new BasicModule());
        JavalinApp app = injector.getInstance(JavalinApp.class);
        app.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            injector.getInstance(AbstractDatabase.class).close();
            app.stop();
        }));
    }
}
