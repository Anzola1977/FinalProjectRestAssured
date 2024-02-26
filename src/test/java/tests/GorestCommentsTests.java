package tests;

import io.restassured.RestAssured;
import org.example.CommentData;
import org.example.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.*;

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
        RestAssured.baseURI = BaseTest.getConfig("baseURI");
        commentGetTest();
        schemeValidationWithPath();
    }

    @Test
    public void commentGetTest() {
        commentID = GorestApiWrappers.sendGetRequest(
                        BaseTest.getConfig("commentsPath"))
                .assertThat()
                .body("$", hasSize(10))
                .extract().jsonPath().getInt("[0]['id']");
    }

    @Test
    public void schemeValidationWithPath() {
        commentData = GorestApiWrappers.sendGetRequest(
                        given().pathParam("id", commentID),
                        BaseTest.getConfig("commentIDPath"))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("gorest_comments_scheme.json"))
                .extract().as(CommentData.class);
    }

    @Test
    public void commentCreatedTest() {
        CommentData commentData = GorestPostDataHelper.createCommentData();
        CommentData actualResponse = GorestApiWrappers.sendPostRequest(
                BaseTest.getConfig("commentsPath"),
                commentData,
                CommentData.class);
        assertEquals(commentData, actualResponse);
    }

    @Test
    public void commentCreatedTestNegative() {
        CommentData commentData = GorestPostDataHelper.createCommentData();
        GorestApiWrappersNegative<CommentData> wrappersNegative = new GorestApiWrappersNegative<>();
        wrappersNegative.sendPostRequest(
                getConfig("commentsPath"),
                commentData);
    }

    @Test
    public void commentUpdatedTest() {
        System.out.println(commentData);
        commentData = GorestPutDataHelper.updateCommentData();
        CommentData actualResponse = GorestApiWrappers.sendPutRequest(
                given().pathParam("id", commentID),
                BaseTest.getConfig("commentIDPath"),
                commentData,
                CommentData.class);
        assertEquals(commentData, actualResponse);
    }

    @Test
    public void commentUpdatedTestNegative() {
        System.out.println(commentData);
        commentData = GorestPutDataHelper.updateCommentData();
        GorestApiWrappersNegative<CommentData> wrappersNegative = new GorestApiWrappersNegative<>();
        wrappersNegative.sendPutRequest(
                given().pathParam("id", commentID),
                getConfig("postIDPath"),//the path failed
                commentData);
    }

    @Test
    public void commentPatchTest() {
        System.out.println(commentData);
        commentData = GorestPatchDataHelper.patchCommentData();
        CommentData actualResponse = GorestApiWrappers.sendPatchRequest(
                given().pathParam("id", commentID),
                BaseTest.getConfig("commentIDPath"),
                commentData,
                CommentData.class);
        assertEquals(commentData, actualResponse);
    }

    @Test
    public void commentPatchTestWithQueryParamNegative() {
        GorestApiWrappersNegative<CommentData> wrappersNegative = new GorestApiWrappersNegative<>();
        wrappersNegative.sendPatchRequest(//a param failed
                given().pathParam("id", commentID).queryParam("email", "active"),
                getConfig("commentIDPath"),
                commentData);
    }

    @Test
    public void commentDeleteTest() {
        GorestApiWrappers.sendDeleteRequest(
                given().pathParam("id", commentID),
                BaseTest.getConfig("commentIDPath"));
    }

    @Test
    public void commentDeleteTestNegative() {
        commentDeleteTest();
        GorestApiWrappersNegative<CommentData> wrappersNegative = new GorestApiWrappersNegative<>();
        wrappersNegative.sendDeleteRequest(//a user not exists
                given().pathParam("id", commentID),
                getConfig("commentIDPath"));
    }
}
