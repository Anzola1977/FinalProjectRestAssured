package tests;

import io.restassured.RestAssured;
import org.example.TodoData;
import org.example.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.*;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GorestTodosTests extends BaseTest {

    public static int todoID;
    public static TodoData todoData;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = BaseTest.getConfig("baseURI");
        todoGetTest();
        schemeValidationWithPath();
    }

    @Test
    public void todoGetTest() {
        todoID = GorestApiWrappers.sendGetRequest(
                        BaseTest.getConfig("todosPath"))
                .assertThat()
                .body("$", hasSize(10))
                .extract().jsonPath().getInt("[0]['id']");
    }

    @Test
    public void schemeValidationWithPath() {
        todoData = GorestApiWrappers.sendGetRequest(
                        given().pathParam("id", todoID),
                        BaseTest.getConfig("todoIDPath"))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("gorest_todos_scheme.json"))
                .extract().as(TodoData.class);
    }

    @Test
    public void todoCreatedTest() {
        TodoData todoData = GorestPostDataHelper.createTodoData();
        TodoData actualResponse = GorestApiWrappers.sendPostRequest(
                BaseTest.getConfig("todosPath"),
                todoData,
                TodoData.class);
        assertEquals(todoData, actualResponse);
    }

    @Test
    public void todoCreatedTestNegative() {
        TodoData todoData = GorestPostDataHelper.createTodoData();
        GorestApiWrappersNegative<TodoData> wrappersNegative = new GorestApiWrappersNegative<>();
        wrappersNegative.sendPostRequest(
                getConfig("todosPath"),
                todoData);
    }

    @Test
    public void todoUpdatedTest() {
        System.out.println(todoData);
        todoData = GorestPutDataHelper.updateTodoData();
        TodoData actualResponse = GorestApiWrappers.sendPutRequest(
                given().pathParam("id", todoID),
                BaseTest.getConfig("todoIDPath"),
                todoData,
                TodoData.class);
        assertEquals(todoData, actualResponse);
    }

    @Test
    public void todoUpdatedTestNegative() {
        System.out.println(todoData);
        todoData = GorestPutDataHelper.updateTodoData();
        GorestApiWrappersNegative<TodoData> wrappersNegative = new GorestApiWrappersNegative<>();
        wrappersNegative.sendPutRequest(
                given().pathParam("id", todoID),
                getConfig("postIDPath"),//the path failed
                todoData);
    }

    @Test
    public void todoPatchTest() {
        System.out.println(todoData);
        todoData = GorestPatchDataHelper.patchTodoData();
        TodoData actualResponse = GorestApiWrappers.sendPatchRequest(
                given().pathParam("id", todoID),
                BaseTest.getConfig("todoIDPath"),
                todoData,
                TodoData.class);
        assertEquals(todoData, actualResponse);
    }

    @Test
    public void todoPatchTestWithQueryParamNegative() {
        GorestApiWrappersNegative<TodoData> wrappersNegative = new GorestApiWrappersNegative<>();
        wrappersNegative.sendPatchRequest(//a param failed
                given().pathParam("id", todoID).queryParam("title", "Andrew Zorro").queryParam("status", "active"),
                getConfig("todoIDPath"),
                todoData);
    }

    @Test
    public void todoDeleteTest() {
        GorestApiWrappers.sendDeleteRequest(
                given().pathParam("id", todoID),
                BaseTest.getConfig("todoIDPath"));
    }

    @Test
    public void todoDeleteTestNegative() {
        todoDeleteTest();
        GorestApiWrappersNegative<TodoData> wrappersNegative = new GorestApiWrappersNegative<>();
        wrappersNegative.sendDeleteRequest(//a user not exists
                given().pathParam("id", todoID),
                getConfig("todoIDPath"));
    }
}
