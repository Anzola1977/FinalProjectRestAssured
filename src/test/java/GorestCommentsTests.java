import io.restassured.RestAssured;
import org.example.CommentData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GorestCommentsTests extends BaseTest {

    public static int commentID;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = getConfig("baseURI");
        commentGetTest();
    }

    @Test
    public void commentGetTest() {
        commentID = GorestApiWrappers.sendGetRequest(
                        getConfig("commentsPath"))
                .assertThat()
                .body("$", hasSize(10))
                .extract().jsonPath().getInt("[0]['id']");
    }

    @Test
    public void schemeValidationWithPath() {
        GorestApiWrappers.sendGetRequest(
                        given().pathParam("id", commentID),
                        getConfig("commentIDPath"))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("gorest_comments_scheme.json"));
    }

    @Test
    public void commentCreatedTest() {
        CommentData commentData = GorestPostDataHelper.createCommentData();
        CommentData actualResponse = GorestApiWrappers.sendPostRequest(
                getConfig("commentsPath"),
                commentData,
                CommentData.class);
        assertEquals(commentData, actualResponse);
    }
}
