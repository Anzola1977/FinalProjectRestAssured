import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class GorestTests extends BaseTest{

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = getConfig("baseURI");
    }

    @Test
    public void getDataUsersTest() {
        given()
                .when()
                .get("/users")
                .then().assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .log().all()
                .body("id", hasSize(10));
    }

    @Test
    public void schemeValidationTest() {
        int user_id = 6445993;
        given()
                .pathParam("id", user_id)
                .when()
                .get("/users/{id}")
                .then().assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .log().all()
                .body(matchesJsonSchemaInClasspath("gorest_user_scheme.json"));
    }

    @Test
    public void schemeValidationWithPath() {
        GorestApiWrappers.sendGetRequest(getConfig("objectIdPath"))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("gorest_user_scheme.json"));
    }

    @Test
    public void userCreatedTest() {
        UserData userData = GorestDataHelper.createUserData();
        UserData actualResponse = GorestApiWrappers.sendPostRequest(getConfig("objectPath"), userData);
        assertEquals(userData, actualResponse);
    }
}
