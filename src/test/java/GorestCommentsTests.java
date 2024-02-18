import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.example.CommentData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GorestCommentsTests extends BaseTest{

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = getConfig("baseURI");
    }

    @Test
    public void getDataPostsTest() {
        given()
                .when()
                .get("/comments")
                .then().assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .log().all()
                .body("$", hasSize(10));
    }

    @Test
    public void schemeValidationWithPath() {
        GorestApiWrappers.sendGetRequest(getConfig("commentIDPath"))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("gorest_comments_scheme.json"));
    }

    @Test
    public void commentCreatedTest() {
        CommentData commentData = GorestDataHelper.createCommentData();
        CommentData actualResponse = GorestApiWrappers.sendPostRequest(getConfig("commentsPath"), commentData, CommentData.class);
        assertEquals(commentData, actualResponse);
    }
}
