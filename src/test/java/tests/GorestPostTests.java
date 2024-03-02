package tests;

import io.restassured.RestAssured;
import org.example.PostData;
import org.junit.jupiter.api.*;
import utils.*;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.example.ConfigMap.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GorestPostTests extends BaseTest {

    public static int postID;
    public static PostData postData;

    @BeforeAll
    @Order(2)
    public static void tearUp() {
        postGetID();
        postGetEntity();
    }

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = getConfig(BASE_URI);
    }

    public static void postGetID() {
        postID = GorestApiWrappers.sendGetRequest(
                        getConfig(POSTS_PATH))
                .extract().jsonPath().getInt("[0]['id']");
    }

    public static void postGetEntity() {
        postData = GorestApiWrappers.sendGetRequest(
                        given().pathParam("id", postID),
                        getConfig(POST_ID_PATH))
                .extract().as(PostData.class);
    }

    @Test
    @Order(3)
    public void postGetTest() {
        GorestApiWrappers.sendGetRequest(
                        getConfig(POSTS_PATH))
                .assertThat()
                .body("$", hasSize(10));
    }

    @Test
    @Order(4)
    public void schemeValidationWithPath() {
        GorestApiWrappers.sendGetRequest(
                        given().pathParam("id", postID),
                        getConfig(POST_ID_PATH))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("gorest_posts_scheme.json"));
    }

    @Test
    public void postCreatedTest() {
        PostData postData = GorestPostDataHelper.createPostData();
        PostData actualResponse = GorestApiWrappers.sendPostRequest(
                getConfig(POSTS_PATH),
                postData,
                PostData.class);
        assertEquals(postData, actualResponse);
    }

    @Test
    public void postCreatedTestNegative() {
        PostData postData = GorestPostDataHelper.createPostData();
        GorestApiWrappersNegative<PostData> wrappersNegative = new GorestApiWrappersNegative<>();
        wrappersNegative.sendPostRequest(
                getConfig(POSTS_PATH),
                postData);
    }

    @Test
    @Order(6)
    public void postUpdatedTest() {
        postData = GorestPutDataHelper.updatePostData();
        PostData actualResponse = GorestApiWrappers.sendPutRequest(
                given().pathParam("id", postID),
                getConfig(POST_ID_PATH),
                postData,
                PostData.class);
        assertEquals(postData, actualResponse);
    }

    @Test
    @Order(5)
    public void postUpdatedTestNegative() {
        postData = GorestPutDataHelper.updatePostData();
        GorestApiWrappersNegative<PostData> wrappersNegative = new GorestApiWrappersNegative<>();
        wrappersNegative.sendPutRequest(
                given().pathParam("id", postID),
                getConfig(USERS_ID_PATH),//the path failed
                postData);
    }

    @Test
    @Order(7)
    public void postPatchTest() {
        postData = GorestPatchDataHelper.patchPostData();
        PostData actualResponse = GorestApiWrappers.sendPatchRequest(
                given().pathParam("id", postID),
                getConfig(POST_ID_PATH),
                postData,
                PostData.class);
        assertEquals(postData, actualResponse);
    }

    @Test
    @Order(8)
    public void postDeleteTest() {
        GorestApiWrappers.sendDeleteRequest(
                given().pathParam("id", postID),
                getConfig(POST_ID_PATH));
    }

    @Test
    public void postDeleteTestNegative() {
        postGetID();
        postDeleteTest();
        GorestApiWrappersNegative<PostData> wrappersNegative = new GorestApiWrappersNegative<>();
        wrappersNegative.sendDeleteRequest(//a user not exists
                given().pathParam("id", postID),
                getConfig(POST_ID_PATH));
    }
}
