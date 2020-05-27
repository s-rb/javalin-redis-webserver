import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.list.surkovr.App;
import ru.list.surkovr.api.VisitedLinksRequest;

import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class IntTests {

    public static final String PROPERTIES_PATH = "app.properties";
    private static String appHostUrl;
    private static int appPort;
    private static String urlVisitedLinks;
    private static String urlVisitedDomains;
    private VisitedLinksRequest request;
    private long startTime;
    private long uploadedTime;

    @BeforeClass
    public static void start() {
        App.main(new String[]{});
        setProperties();
        Unirest.config().defaultBaseUrl("http://" + appHostUrl + ":" + appPort);
    }

    @Before
    public void setUp() {
        startTime = System.currentTimeMillis();
    }

    @Test
    public void POST_to_save_elements_of_links_to_database() {
        HttpResponse<JsonNode> response = makePostRequest();

        JsonObject expectedObject = new JsonObject();
        expectedObject.addProperty("status", "ok");

        Assert.assertEquals(200, response.getStatus());
        assertThat(response.getBody().toString()).isEqualTo(expectedObject.toString());
    }

    private HttpResponse<JsonNode> makePostRequest() {
        String mockDomain1 = "mock.Domain1" + new Random().nextInt();
        String mockDomain2 = "mock.Domain2" + new Random().nextInt();
        String mockDomain3 = "mock.Domain3" + new Random().nextInt();

        JsonObject requestBody = new JsonObject();
        request = new VisitedLinksRequest();
        request.setLinks(List.of(mockDomain1, mockDomain2, mockDomain3));
        JsonArray array = new JsonArray();
        request.getLinks().forEach(array::add);
        requestBody.add("links", array);

        startTime = System.currentTimeMillis();
        HttpResponse<JsonNode> response = Unirest.post(urlVisitedLinks)
                .body(requestBody).asJson();
        uploadedTime = System.currentTimeMillis();
        return response;
    }

    @Test
    public void GET_visited_links_valid() {
        makePostRequest();
        HttpResponse<JsonNode> response = Unirest.get(urlVisitedDomains)
                .queryString("from", startTime)
                .queryString("to", uploadedTime).asJson();

        List<String> resultList = new LinkedList<>();
        response.getBody().getObject().getJSONArray("domains").forEach(s -> resultList.add(s.toString()));
        Assert.assertEquals(response.getStatus(), 200);
        Assert.assertArrayEquals(resultList.stream().sorted(String::compareTo).toArray(),
                request.getLinks().stream().sorted(String::compareTo).toArray());
    }

    @Test
    public void GET_visited_links_no_links() {
        HttpResponse<JsonNode> response = Unirest.get(urlVisitedDomains)
                .queryString("from", System.currentTimeMillis())
                .queryString("to", System.currentTimeMillis()).asJson();

        JsonObject expectedObject = new JsonObject();
        expectedObject.addProperty("status", "There are no links");

        Assert.assertEquals(200, response.getStatus());
        assertThat(response.getBody().toString()).isEqualTo(expectedObject.toString());
    }

    @Test
    public void GET_visited_links_wrong_params() {
        makePostRequest();
        HttpResponse<JsonNode> response = Unirest.get(urlVisitedDomains)
                .queryString("from", "")
                .queryString("to", "dsa").asJson();

        JsonObject expectedObject = new JsonObject();
        expectedObject.addProperty("status", "Wrong parameters");

        Assert.assertEquals(400, response.getStatus());
        assertThat(response.getBody().toString()).isEqualTo(expectedObject.toString());
    }

    @Test
    public void GET_visited_links_no_params() {
        makePostRequest();
        HttpResponse<JsonNode> response = Unirest.get(urlVisitedDomains).asJson();

        JsonObject expectedObject = new JsonObject();
        expectedObject.addProperty("status", "Wrong parameters");

        Assert.assertEquals(400, response.getStatus());
        assertThat(response.getBody().toString()).isEqualTo(expectedObject.toString());
    }

    @Test
    public void POST_visited_link_null_links_array() {
        JsonObject requestBody = new JsonObject();
        requestBody.add("links", null);

        HttpResponse<JsonNode> response = Unirest.post(urlVisitedLinks)
                .body(requestBody).asJson();

        JsonObject expectedObject = new JsonObject();
        expectedObject.addProperty("status", "wrong request body");

        Assert.assertEquals(400, response.getStatus());
        assertThat(response.getBody().toString()).isEqualTo(expectedObject.toString());
    }

    @Test
    public void POST_visited_link_null_json_body() {
        JsonObject requestBody = new JsonObject();

        HttpResponse<JsonNode> response = Unirest.post(urlVisitedLinks)
                .body(requestBody).asJson();

        JsonObject expectedObject = new JsonObject();
        expectedObject.addProperty("status", "wrong request body");

        Assert.assertEquals(400, response.getStatus());
        assertThat(response.getBody().toString()).isEqualTo(expectedObject.toString());
    }

    private static void setProperties() {
        FileInputStream fileInputStream;
        Properties prop = new Properties();
        try {
            fileInputStream = new FileInputStream(PROPERTIES_PATH);
            prop.load(fileInputStream);
            appPort = Integer.parseInt(prop.getProperty("APP_PORT"));
            appHostUrl = prop.getProperty("APP_HOST");
            urlVisitedLinks = prop.getProperty("URL_VISITED_LINKS");
            urlVisitedDomains = prop.getProperty("URL_VISITED_DOMAINS");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
