import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.TodoData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GorestTodosTests extends BaseTest{

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = getConfig("baseURI");
    }

    @Test
    public void getDataTodosTest() {
        given()
                .when()
                .get("/todos")
                .then().assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .log().all()
                .body("$", hasSize(10));
    }

    @Test
    public void schemeValidationWithPath() {
        GorestApiWrappers.sendGetRequest(getConfig("todoIDPath"))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("gorest_todos_scheme.json"));
    }

    @Test
    public void todoCreatedTest() {
        TodoData todoData = GorestDataHelper.createTodoData();
        TodoData actualResponse = GorestApiWrappers.sendPostRequest(getConfig("todosPath"), todoData, TodoData.class);
        assertEquals(todoData, actualResponse);
    }
}
