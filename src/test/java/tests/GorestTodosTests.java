package tests;

import io.restassured.RestAssured;
import org.example.TodoData;
import org.junit.jupiter.api.*;
import utils.*;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.example.ConfigMap.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GorestTodosTests extends BaseTest {

    public static int todoID;
    public static TodoData todoData;

    @BeforeAll
    @Order(2)
    public static void tearUp() {
        todoGetID();
        todoGetEntity();
    }

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = BaseTest.getConfig(BASE_URI);
    }

    public static void todoGetID() {
        todoID = GorestApiWrappers.sendGetRequest(
                        BaseTest.getConfig(TODOS_PATH))
                .extract().jsonPath().getInt("[0]['id']");
    }


    public static void todoGetEntity() {
        todoData = GorestApiWrappers.sendGetRequest(
                        given().pathParam("id", todoID),
                        BaseTest.getConfig(TODO_ID_PATH))
                .extract().as(TodoData.class);
    }

    @Test
    @Order(3)
    public void todoGetTest() {
        GorestApiWrappers.sendGetRequest(
                        BaseTest.getConfig(TODOS_PATH))
                .assertThat()
                .body("$", hasSize(10));
    }

    @Test
    @Order(4)
    public void schemeValidationWithPath() {
        GorestApiWrappers.sendGetRequest(
                        given().pathParam("id", todoID),
                        BaseTest.getConfig(TODO_ID_PATH))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("gorest_todos_scheme.json"));
    }

    @Test
    public void todoCreatedTest() {
        TodoData todoData = GorestPostDataHelper.createTodoData();
        TodoData actualResponse = GorestApiWrappers.sendPostRequest(
                BaseTest.getConfig(TODOS_PATH),
                todoData,
                TodoData.class);
        assertEquals(todoData, actualResponse);
    }

    @Test
    public void todoCreatedTestNegative() {
        TodoData todoData = GorestPostDataHelper.createTodoData();
        GorestApiWrappersNegative<TodoData> wrappersNegative = new GorestApiWrappersNegative<>();
        wrappersNegative.sendPostRequest(
                getConfig(TODOS_PATH),
                todoData);
    }

    @Test
    @Order(5)
    public void todoUpdatedTest() {
        todoData = GorestPutDataHelper.updateTodoData();
        TodoData actualResponse = GorestApiWrappers.sendPutRequest(
                given().pathParam("id", todoID),
                BaseTest.getConfig(TODO_ID_PATH),
                todoData,
                TodoData.class);
        assertEquals(todoData, actualResponse);
    }

    @Test
    @Order(6)
    public void todoUpdatedTestNegative() {
        todoData = GorestPutDataHelper.updateTodoData();
        GorestApiWrappersNegative<TodoData> wrappersNegative = new GorestApiWrappersNegative<>();
        wrappersNegative.sendPutRequest(
                given().pathParam("id", todoID),
                getConfig(POST_ID_PATH),//the path failed
                todoData);
    }

    @Test
    @Order(7)
    public void todoPatchTest() {
        todoData = GorestPatchDataHelper.patchTodoData();
        TodoData actualResponse = GorestApiWrappers.sendPatchRequest(
                given().pathParam("id", todoID),
                BaseTest.getConfig(TODO_ID_PATH),
                todoData,
                TodoData.class);
        assertEquals(todoData, actualResponse);
    }

    @Test
    @Order(8)
    public void todoPatchTestWithQueryParamNegative() {
        GorestApiWrappersNegative<TodoData> wrappersNegative = new GorestApiWrappersNegative<>();
        wrappersNegative.sendPatchRequest(//a param failed
                given().pathParam("id", todoID).queryParam("title", "Andrew Zorro").queryParam("status", "active"),
                getConfig(TODO_ID_PATH),
                todoData);
    }

    @Test
    @Order(9)
    public void todoDeleteTest() {
        GorestApiWrappers.sendDeleteRequest(
                given().pathParam("id", todoID),
                BaseTest.getConfig(TODO_ID_PATH));
    }

    @Test
    public void todoDeleteTestNegative() {
        todoGetID();
        todoDeleteTest();
        GorestApiWrappersNegative<TodoData> wrappersNegative = new GorestApiWrappersNegative<>();
        wrappersNegative.sendDeleteRequest(//a user not exists
                given().pathParam("id", todoID),
                getConfig(TODO_ID_PATH));
    }
}
