import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class GorestUsersTests extends BaseTest{

    public static int userID;

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
                .body("$", hasSize(10));
    }

    @Test
    public void getUsersIDTest() {
        userID = given()
                .when()
                .get("/users")
                .then().assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .log().all()
                .extract().jsonPath().getInt("[0]['id']");
        System.out.println(userID);
    }

    @Test
    public void schemeValidationTest() {
        getUsersIDTest();
        given()
                .pathParam("id", userID)
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
        GorestApiWrappers.sendGetRequest(getConfig("userIDPath"))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("gorest_user_scheme.json"));
    }

    @Test
    public void userCreatedTest() {
        UserData userData = GorestDataHelper.createUserData();
        UserData actualResponse = GorestApiWrappers.sendPostRequest(getConfig("usersPath"), userData, UserData.class);
        assertEquals(userData, actualResponse);
    }


}
