package tests;

import io.restassured.RestAssured;
import org.example.PostData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import utils.*;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GorestPostTests extends BaseTest {

    public static int postID;
    public static PostData postData;

    @BeforeAll
    @Order(2)
    public static void tearUp() {
        postGetTest();
        schemeValidationWithPath();
    }

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = getConfig("baseURI");
    }

    public static void postGetTest() {
        postID = GorestApiWrappers.sendGetRequest(
                        getConfig("postsPath"))
                .assertThat()
                .body("$", hasSize(10))
                .extract().jsonPath().getInt("[0]['id']");
    }

    public static void schemeValidationWithPath() {
        postData = GorestApiWrappers.sendGetRequest(
                        given().pathParam("id", postID),
                        getConfig("postIDPath"))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("gorest_posts_scheme.json"))
                .extract().as(PostData.class);
    }

    @Test
    public void postCreatedTest() {
        PostData postData = GorestPostDataHelper.createPostData();
        PostData actualResponse = GorestApiWrappers.sendPostRequest(
                getConfig("postsPath"),
                postData,
                PostData.class);
        assertEquals(postData, actualResponse);
    }

    @Test
    public void postCreatedTestNegative() {
        PostData postData = GorestPostDataHelper.createPostData();
        GorestApiWrappersNegative<PostData> wrappersNegative = new GorestApiWrappersNegative<>();
        wrappersNegative.sendPostRequest(
                getConfig("postsPath"),
                postData);
    }

    @Test
    public void postUpdatedTest() {
        postGetTest();
        System.out.println(postData);
        postData = GorestPutDataHelper.updatePostData();
        PostData actualResponse = GorestApiWrappers.sendPutRequest(
                given().pathParam("id", postID),
                getConfig("postIDPath"),
                postData,
                PostData.class);
        assertEquals(postData, actualResponse);
    }

    @Test
    public void postUpdatedTestNegative() {
        System.out.println(postData);
        postData = GorestPutDataHelper.updatePostData();
        GorestApiWrappersNegative<PostData> wrappersNegative = new GorestApiWrappersNegative<>();
        wrappersNegative.sendPutRequest(
                given().pathParam("id", postID),
                getConfig("userIDPath"),//the path failed
                postData);
    }

    @Test
    public void postPatchTest() {
        System.out.println(postData);
        postData = GorestPatchDataHelper.patchPostData();
        PostData actualResponse = GorestApiWrappers.sendPatchRequest(
                given().pathParam("id", postID),
                getConfig("postIDPath"),
                postData,
                PostData.class);
        assertEquals(postData, actualResponse);
    }

    @Test
    public void postDeleteTest() {
        GorestApiWrappers.sendDeleteRequest(
                given().pathParam("id", postID),
                getConfig("postIDPath"));
    }

    @Test
    public void postDeleteTestNegative() {
        postGetTest();
        postDeleteTest();
        GorestApiWrappersNegative<PostData> wrappersNegative = new GorestApiWrappersNegative<>();
        wrappersNegative.sendDeleteRequest(//a user not exists
                given().pathParam("id", postID),
                getConfig("postIDPath"));
    }
}
