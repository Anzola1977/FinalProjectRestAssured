package tests;

import io.restassured.RestAssured;
import org.example.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.*;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class GorestUsersTests extends BaseTest {

    public static int userID;
    public static UserData userData;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = getConfig("baseURI");
        userGetTest();
        schemeValidationWithPath();
    }

    @Test
    public void userGetTest() {
        userID = GorestApiWrappers.sendGetRequest(
                        getConfig("usersPath"))
                .assertThat()
                .body("$", hasSize(10))
                .extract().jsonPath().getInt("[0]['id']");
    }

    @Test
    public void schemeValidationWithPath() {
        userData = GorestApiWrappers.sendGetRequest(
                        given().pathParam("id", userID),
                        getConfig("userIDPath"))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("gorest_user_scheme.json"))
                .extract().as(UserData.class);
    }

    @Test
    public void userCreatedTest() {
        UserData userData = GorestPostDataHelper.createUserData();
        UserData actualResponse = GorestApiWrappers.sendPostRequest(
                getConfig("usersPath"),
                userData,
                UserData.class);
        assertEquals(userData, actualResponse);
    }

    @Test
    public void userUpdatedTest() {
        System.out.println(userData);
        userData = GorestPutDataHelper.updateUserData();
        UserData actualResponse = GorestApiWrappers.sendPutRequest(
                given().pathParam("id", userID),
                getConfig("userIDPath"),
                userData,
                UserData.class);
        assertEquals(userData, actualResponse);
    }

    @Test
    public void userPatchTest() {
        System.out.println(userData);
        userData = GorestPatchDataHelper.patchUserData();
        UserData actualResponse = GorestApiWrappers.sendPatchRequest(
                given().pathParam("id", userID),
                getConfig("userIDPath"),
                userData,
                UserData.class);
        assertEquals(userData, actualResponse);
    }

    @Test
    public void userPatchTestWithParam() {
        System.out.println(userData);
        userData = GorestPatchDataHelper.patchUserData("John Malcovich", "male");
        UserData actualResponse = GorestApiWrappers.sendPatchRequest(
                given().pathParam("id", userID),
                getConfig("userIDPath"),
                userData,
                UserData.class);
        assertEquals(userData, actualResponse);
    }

    @Test
    public void userPatchTestWithQueryParam() {
        System.out.println(userData);
        UserData actualResponse = GorestApiWrappers.sendPatchRequest(
                given().pathParam("id", userID).queryParam("name", "Andrew Zorro").queryParam("gender", "male"),
                getConfig("userIDPath"),
                userData,
                UserData.class);
        System.out.println(actualResponse);
    }

    @Test
    public void userDeleteTest() {
        System.out.println(userData);
        GorestApiWrappers.sendDeleteRequest(
                given().pathParam("id", userID),
                getConfig("userIDPath"));
        GorestApiWrappers.sendGetRequest(
                        given().pathParam("id", userID),
                        getConfig("userIDPath"),
                        404)
                .assertThat()
                .body("message", equalTo("Resource not found"));
        GorestApiWrappers.sendGetRequest(
                        getConfig("usersPath"))
                .assertThat()
                .body("$", hasSize(10));
    }
}
