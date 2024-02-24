package tests;

import io.restassured.RestAssured;
import org.example.PostData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.*;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GorestPostTests extends BaseTest {

    public static int postID;
    public static PostData postData;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = getConfig("baseURI");
        postGetTest();
        schemeValidationWithPath();
    }

    @Test
    public void postGetTest() {
        postID = GorestApiWrappers.sendGetRequest(
                        getConfig("postsPath"))
                .assertThat()
                .body("$", hasSize(10))
                .extract().jsonPath().getInt("[0]['id']");
    }

    @Test
    public void schemeValidationWithPath() {
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
    public void postUpdatedTest() {
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
        System.out.println(postData);
        GorestApiWrappers.sendDeleteRequest(
                given().pathParam("id", postID),
                getConfig("postIDPath"));
        GorestApiWrappers.sendGetRequest(
                        given().pathParam("id", postID),
                        getConfig("postIDPath"),
                        404)
                .assertThat()
                .body("message", equalTo("Resource not found"));
        GorestApiWrappers.sendGetRequest(
                        getConfig("postsPath"))
                .assertThat()
                .body("$", hasSize(10));
    }
}
