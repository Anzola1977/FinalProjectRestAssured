import io.restassured.RestAssured;
import org.example.PostData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GorestPostTests extends BaseTest {

    public static int postID;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = getConfig("baseURI");
        postGetTest();
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
        GorestApiWrappers.sendGetRequest(
                        given().pathParam("id", postID),
                        getConfig("postIDPath"))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("gorest_posts_scheme.json"));
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
}
