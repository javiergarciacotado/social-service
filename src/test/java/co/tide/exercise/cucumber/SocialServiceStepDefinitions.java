package co.tide.exercise.cucumber;

import co.tide.exercise.configuration.PropertiesReader;
import co.tide.exercise.handler.factory.HandlerFactory;
import co.tide.exercise.handler.impl.story.GetStoryStoryHandler;
import co.tide.exercise.handler.impl.story.PostStoryStoryHandler;
import co.tide.exercise.handler.impl.story.PutStoryStoryHandler;
import co.tide.exercise.handler.impl.story.StoryHandler;
import co.tide.exercise.jdbc.dao.StoryRepository;
import co.tide.exercise.jdbc.dao.StoryRepositoryStub;
import co.tide.exercise.model.Story;
import co.tide.exercise.provider.StoryProvider;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.sun.net.httpserver.HttpServer;
import cucumber.api.DataTable;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Optional;

import static com.jayway.restassured.RestAssured.*;
import static org.junit.Assert.assertEquals;

public class SocialServiceStepDefinitions {

    private StoryRepository storyRepository;
    private HttpServer httpServer;
    private GeneratedData generatedData;
    private Response response;

    @Before
    public void setUp() throws Throwable {
        storyRepository = new StoryRepositoryStub();
        generatedData = new GeneratedData();
    }

    @After
    public void tearDown() {
        httpServer.stop(0);
    }


    @Given("^a Story is stored with$")
    public void loadStory(DataTable story) throws Throwable {
        List<Story> stories = story.asList(Story.class);
        stories.forEach(s -> storyRepository.create(s));
    }

    @Given("^the Server is listening$")
    public void startServer() throws Throwable {

        PropertiesReader propertiesReader = new PropertiesReader("/application.properties");

        String context = propertiesReader.get("social.service.application.context");
        int port = Integer.parseInt(propertiesReader.get("social.service.application.port"));

        RestAssured.basePath = context;
        RestAssured.port = port;

        httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.createContext(context, new StoryHandler(registerHandlers()));
        httpServer.start();
        System.out.println("****Server test started: Listening on port: " + port);
    }

    @When("^the Client does a \"(\\w+)\" request on \"([^\"]*)\"$")
    public void request(String method, String path) throws Throwable {
        generatedData.setUri("".equals(path) ? RestAssured.basePath : path);

        path = path.replaceAll("/story", "");

        if ("GET".equalsIgnoreCase(method)) {
            response = get(path);
        } else if ("POST".equalsIgnoreCase(method)) {
            response = post(path);
        } else if ("PUT".equalsIgnoreCase(method)) {
            response = put(path);
        }
    }

    @When("^the Client does a \"(\\w+)\" request on \"([^\"]*)\" and body$")
    public void requestWithBody(String method, String path, String requestBody) throws Throwable {
        generatedData.setUri("".equals(path) ? RestAssured.basePath : path);

        path = path.replaceAll("/story", "");
        response = given().body(requestBody).post(path);
    }

    @Then("^the Client should get a (\\d+) response$")
    public void verifyStatusCode(int code) throws Throwable {
        assertEquals(code, response.getStatusCode());
    }

    @And("^the Response contains popularity of (-?[0-9]*)$")
    public void checkPopularityResponse(int popularity) {
        final Gson json = new Gson();

//        from(response.getBody().print()).get("popularity");

        final JsonObject jsonObject = json.fromJson(response.getBody().print(), JsonObject.class);
        final JsonObject detailJson = jsonObject.getAsJsonObject("detail");
        final Optional<JsonObject> valueJson = Optional.ofNullable(detailJson.getAsJsonObject("value"));

        assertEquals(popularity, valueJson.orElse(detailJson).get("popularity").getAsInt());
    }

    @And("^the Response contains$")
    public void checkResponse(DataTable dataTable) {

        dataTable.asMaps(String.class, String.class).forEach(row -> {
            final String problemType = row.get("PROBLEM TYPE");
            final String problemTitle = generatedData.resolve(row.get("PROBLEM TITLE"));
            final String problemDetail = generatedData.resolve(row.get("PROBLEM DETAIL"));

            final Gson json = new Gson();
            final JsonObject jsonObject = json.fromJson(response.getBody().print(), JsonObject.class);
            final JsonObject detailJson = jsonObject.getAsJsonObject("detail");

            assertEquals(problemType, detailJson.get("problemType").getAsString());
            assertEquals(problemTitle, detailJson.get("title").getAsString());
            assertEquals(problemDetail, detailJson.get("detail").getAsString());
        });

    }


    private HandlerFactory registerHandlers() {
        HandlerFactory handlerFactory = new HandlerFactory();

        final StoryProvider storyProvider = new StoryProvider(storyRepository);

        handlerFactory.register("GET", () -> new GetStoryStoryHandler(storyProvider));
        handlerFactory.register("POST", () -> new PostStoryStoryHandler(storyProvider));
        handlerFactory.register("PUT", () -> new PutStoryStoryHandler(storyProvider));

        return handlerFactory;
    }

}

class GeneratedData {

    private String uri;

    public String resolve(String path) {
        path = path.replaceAll("\\{uri\\}", uri);
        return path;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
