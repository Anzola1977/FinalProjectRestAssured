package tests;

import io.restassured.RestAssured;
import org.example.CommentData;
import org.junit.jupiter.api.*;
import utils.*;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.example.ConfigMap.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GorestCommentsTests extends BaseTest {

    public static int commentID;
    public static CommentData commentData;

    @BeforeAll
    @Order(2)
    public static void tearUp() {
        commentGetID();
        commentGetEntity();
    }

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = BaseTest.getConfig(BASE_URI);
    }


    public static void commentGetID() {
        commentID = GorestApiWrappers.sendGetRequest(
                        BaseTest.getConfig(COMMENTS_PATH))
                .extract().jsonPath().getInt("[0]['id']");
    }

    public static void commentGetEntity() {
        commentData = GorestApiWrappers.sendGetRequest(
                        given().pathParam("id", commentID),
                        BaseTest.getConfig(COMMENT_ID_PATH))
                .extract().as(CommentData.class);
    }

    @Test
    @Order(3)
    public void commentGetTest() {
        GorestApiWrappers.sendGetRequest(
                        BaseTest.getConfig(COMMENTS_PATH))
                .assertThat()
                .body("$", hasSize(10));
    }

    @Test
    @Order(4)
    public void schemeValidationWithPath() {
        GorestApiWrappers.sendGetRequest(
                        given().pathParam("id", commentID),
                        BaseTest.getConfig(COMMENT_ID_PATH))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("gorest_comments_scheme.json"));
    }

    @Test
    public void commentCreatedTest() {
        CommentData commentData = GorestPostDataHelper.createCommentData();
        CommentData actualResponse = GorestApiWrappers.sendPostRequest(
                BaseTest.getConfig(COMMENTS_PATH),
                commentData,
                CommentData.class);
        assertEquals(commentData, actualResponse);
    }

    @Test
    public void commentCreatedTestNegative() {
        CommentData commentData = GorestPostDataHelper.createCommentData();
        GorestApiWrappersNegative<CommentData> wrappersNegative = new GorestApiWrappersNegative<>();
        wrappersNegative.sendPostRequest(
                getConfig(COMMENTS_PATH),
                commentData);
    }

    @Test
    @Order(5)
    public void commentUpdatedTest() {
        commentData = GorestPutDataHelper.updateCommentData();
        CommentData actualResponse = GorestApiWrappers.sendPutRequest(
                given().pathParam("id", commentID),
                BaseTest.getConfig(COMMENT_ID_PATH),
                commentData,
                CommentData.class);
        assertEquals(commentData, actualResponse);
    }

    @Test
    @Order(6)
    public void commentUpdatedTestNegative() {
        commentData = GorestPutDataHelper.updateCommentData();
        GorestApiWrappersNegative<CommentData> wrappersNegative = new GorestApiWrappersNegative<>();
        wrappersNegative.sendPutRequest(
                given().pathParam("id", commentID),
                getConfig(POST_ID_PATH),//the path failed
                commentData);
    }

    @Test
    @Order(7)
    public void commentPatchTest() {
        commentData = GorestPatchDataHelper.patchCommentData();
        CommentData actualResponse = GorestApiWrappers.sendPatchRequest(
                given().pathParam("id", commentID),
                BaseTest.getConfig(COMMENT_ID_PATH),
                commentData,
                CommentData.class);
        assertEquals(commentData, actualResponse);
    }

    @Test
    @Order(8)
    public void commentPatchTestWithQueryParamNegative() {
        GorestApiWrappersNegative<CommentData> wrappersNegative = new GorestApiWrappersNegative<>();
        wrappersNegative.sendPatchRequest(//a param failed
                given().pathParam("id", commentID).queryParam("email", "active"),
                getConfig(COMMENT_ID_PATH),
                commentData);
    }

    @Test
    @Order(9)
    public void commentDeleteTest() {
        GorestApiWrappers.sendDeleteRequest(
                given().pathParam("id", commentID),
                BaseTest.getConfig(COMMENT_ID_PATH));
    }

    @Test
    public void commentDeleteTestNegative() {
        commentGetID();
        commentDeleteTest();
        GorestApiWrappersNegative<CommentData> wrappersNegative = new GorestApiWrappersNegative<>();
        wrappersNegative.sendDeleteRequest(//a user not exists
                given().pathParam("id", commentID),
                getConfig(COMMENT_ID_PATH));
    }
}
