import Utils.GorestApiWrappers;
import Utils.GorestDataHelper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static Utils.GorestApiWrappers.sendGetRequest;
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
    public void getRequestTest() {
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
        int user_id = 6371363;
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
    public void validateListOfAllObjects() {
        sendGetRequest(getConfig("objectIdPath"))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("gorest_user_scheme.json"));
    }

    @Test
    public void entityPostRequestImprovedTest() {
        UserData gadgetPost = GorestDataHelper.createSampleGadgetPost();
        // или
        gadgetPost.setName("BLA-BLA");
        // или
        //GadgetPost gadgetPost = TestDataHelper.createSampleGadgetPost("Bla-bla-bla");

        UserData actualResponse =
                GorestApiWrappers.sendPostRequest(getConfig("objectPath"), gadgetPost);

        assertEquals(actualResponse, gadgetPost);
    }
}
