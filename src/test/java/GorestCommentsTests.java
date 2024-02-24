import io.restassured.RestAssured;
import org.example.CommentData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GorestCommentsTests extends BaseTest {

    public static int commentID;
    public static CommentData commentData;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = getConfig("baseURI");
        commentGetTest();
        schemeValidationWithPath();
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
        commentData = GorestApiWrappers.sendGetRequest(
                        given().pathParam("id", commentID),
                        getConfig("commentIDPath"))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("gorest_comments_scheme.json"))
                .extract().as(CommentData.class);
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

    @Test
    public void commentUpdatedTest() {
        System.out.println(commentData);
        commentData = GorestPutDataHelper.updateCommentData();
        CommentData actualResponse = GorestApiWrappers.sendPutRequest(
                given().pathParam("id", commentID),
                getConfig("commentIDPath"),
                commentData,
                CommentData.class);
        assertEquals(commentData, actualResponse);
    }

    @Test
    public void commentPatchTest() {
        System.out.println(commentData);
        commentData = GorestPatchDataHelper.patchCommentData();
        CommentData actualResponse = GorestApiWrappers.sendPatchRequest(
                given().pathParam("id", commentID),
                getConfig("commentIDPath"),
                commentData,
                CommentData.class);
        assertEquals(commentData, actualResponse);
    }

    @Test
    public void commentDeleteTest() {
        System.out.println(commentData);
        GorestApiWrappers.sendDeleteRequest(
                given().pathParam("id", commentID),
                getConfig("commentIDPath"));
        GorestApiWrappers.sendGetRequest(
                        given().pathParam("id", commentID),
                        getConfig("commentIDPath"),
                        404)
                .assertThat()
                .body("message", equalTo("Resource not found"));
        GorestApiWrappers.sendGetRequest(
                        getConfig("commentsPath"))
                .assertThat()
                .body("$", hasSize(10));
    }
}
