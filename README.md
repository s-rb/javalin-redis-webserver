##  Простое Web-приложение по квалификационному заданию FunBox для разработчиков Java/Kotlin

![Java](https://img.shields.io/badge/-Java-05122A?style=flat&logo=Java&logoColor=FFA518) ![Javalin](https://img.shields.io/badge/-Javalin-05122A?style=flat&logo=Javalin) ![WebService](https://img.shields.io/badge/-WebService-05122A?style=flat) ![Guice](https://img.shields.io/badge/-Guice-05122A?style=flat&logo=guice) ![Redis](https://img.shields.io/badge/-Redis-05122A?style=flat&logo=redis&logoColor=yellow) ![JUnit](https://img.shields.io/badge/-JUnit-05122A?style=flat&logo=JUnit) ![Maven](https://img.shields.io/badge/-Maven-05122A?style=flat&logo=apachemaven&logoColor=fffffb) ![REST](https://img.shields.io/badge/-REST-05122A?style=flat)

#### Функции:
Простой учет посещенных ссылок (получение и возврат по запросу).
Приложение предоставляет JSON API по HTTP. 
Приложение предоставляет два HTTP ресурса: `/visited_links` и `/visited_domains`.

### Технологии:
* Использованы легковесные WEB и DI фреймворки **Javalin** и **Guice**.
* База данных - **Redis**, клиент - **RedissonClient**.
* Тестирование - **JUnit4**, **Unirest**, **AssertJ**. Покрытие **85%** строк.
* Управление зависимостями и сборка - **Maven**.
* Java 11, Lambda, Stream API, Optional.

Настройки приложения и подключения к БД в файле app.properties ([src/main/resources](app.properties))

В базе данных сохраняются все посещенные ссылки (полный путь с параметрами), по запросу выдаются только уникальные домены вида 
"funbox.ru".

В связи с малым объемом - логика не выделена в сервис, а релизована в контроллере.

#### Запросы:

Добавление массива ссылок в БД:

`POST /visited_links`

Тело:

`{
"links": [
"https://ya.ru",
"https://ya.ru?q=123",
"funbox.ru",
"https://stackoverflow.com/questions/11828270/how-to-exit-the-vim-editor"
]
}`

Ответ:

`{
"status": "ok"
}`

Получение списка уникальных доменов за промежуток времени. Время - в формате Long - количество 
миллисекунд прошедших с 1 января 1970 года в миллисекундах

`GET /visited_domains?from=1545221231&to=1545217638`

Ответ:

`{
"domains": [
"ya.ru",
"funbox.ru",
"stackoverflow.com"
],
"status": "ok"
}`

#### Использование
Запуск на локальном хосте из среды разработки:
* Запустить БД Redis.
* Указать в [app.properties](app.properties) параметры подключения к БД (host:port).
* Установить значения host и port для веб приложения.
* Запустить приложение через метод main в классе [App](src/main/java/ru/list/surkovr/App.java)
* Приложение готово принимать запросы и выдавать ответы (см.выше).

Запуск с настройками по умолчанию:
* Запустить приложенный jar-файл web_server.jar.
* Приложение имеет встроенный сервер, по-умолчанию запускается на локальном хосте (127.0.0.1), порт 7000.
* Файл app.properties должен находиться в одной папке с файлом web-server.jar.

Для ручного тестирования с помощью Postman приложен [json](FunBoxRedis.postman_collection.json) файл с готовыми запросами.