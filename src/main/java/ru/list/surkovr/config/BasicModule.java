package ru.list.surkovr.config;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.name.Names;
import ru.list.surkovr.controllers.MainController;
import ru.list.surkovr.controllers.MainControllerImpl;
import ru.list.surkovr.model.AbstractDatabase;
import ru.list.surkovr.model.RedisDatabase;
import ru.list.surkovr.model.Repository;
import ru.list.surkovr.model.RepositoryImpl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BasicModule implements Module {

    public static final String APP_PROPERTIES = "app.properties";

    public void configure(Binder binder) {
        binder.bind(MainController.class).to(MainControllerImpl.class);
        binder.bind(AbstractDatabase.class).to(RedisDatabase.class);
        binder.bind(Repository.class).to(RepositoryImpl.class);
        loadProperties(binder);
    }

    private void loadProperties(Binder binder) {
        FileInputStream fileInputStream;
        Properties prop = new Properties();
        try {
            fileInputStream = new FileInputStream(APP_PROPERTIES);
            prop.load(fileInputStream);
            Names.bindProperties(binder, prop);
        } catch (IOException e) {
            binder.addError(e);
        }
    }
}