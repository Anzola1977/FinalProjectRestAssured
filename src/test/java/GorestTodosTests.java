import io.restassured.RestAssured;
import org.example.TodoData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GorestTodosTests extends BaseTest {

    public static int todoID;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = getConfig("baseURI");
        todoGetTest();
    }

    @Test
    public void todoGetTest() {
        todoID = GorestApiWrappers.sendGetRequest(
                        getConfig("todosPath"))
                .assertThat()
                .body("$", hasSize(10))
                .extract().jsonPath().getInt("[0]['id']");
    }

    @Test
    public void schemeValidationWithPath() {
        GorestApiWrappers.sendGetRequest(
                        given().pathParam("id", todoID),
                        getConfig("todoIDPath"))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("gorest_todos_scheme.json"));
    }

    @Test
    public void todoCreatedTest() {
        TodoData todoData = GorestPostDataHelper.createTodoData();
        TodoData actualResponse = GorestApiWrappers.sendPostRequest(
                getConfig("todosPath"),
                todoData,
                TodoData.class);
        assertEquals(todoData, actualResponse);
    }
}
