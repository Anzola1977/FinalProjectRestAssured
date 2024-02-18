import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.PostData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GorestPostTests extends BaseTest{

    public static int postID;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = getConfig("baseURI");
    }

    @Test
    public void getDataPostsTest() {
        given()
                .when()
                .get("/posts")
                .then().assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .log().all()
                .body("$", hasSize(10));
    }

    @Test
    public void getPostIDTest() {
        postID = given()
                .when()
                .get("/posts")
                .then().assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .log().all()
                .extract().jsonPath().getInt("[0]['id']");
        System.out.println(postID);
    }

    @Test
    public void schemeValidationWithPath() {
        GorestApiWrappers.sendGetRequest(getConfig("postIDPath"))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("gorest_posts_scheme.json"));
    }

    @Test
    public void postCreatedTest() {
        PostData postData = GorestDataHelper.createPostData();
        PostData actualResponse = GorestApiWrappers.sendPostRequest(getConfig("postsPath"), postData, PostData.class);
        assertEquals(postData, actualResponse);
    }
}
