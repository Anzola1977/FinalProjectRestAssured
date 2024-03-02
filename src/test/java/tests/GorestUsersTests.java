package tests;

import io.restassured.RestAssured;
import org.example.UserData;
import org.junit.jupiter.api.*;
import utils.*;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.example.ConfigMap.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GorestUsersTests extends BaseTest {

    public static int userID;
    public static UserData userData;

    @BeforeAll
    @Order(2)
    public static void tearUp() {
        userGetID();
        userGetEntity();
    }

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = getConfig(BASE_URI);
    }

    public static void userGetID() {
        userID = GorestApiWrappers.sendGetRequest(
                        getConfig(USERS_PATH))
                .extract().jsonPath().getInt("[0]['id']");
    }

    public static void userGetEntity() {
        userData = GorestApiWrappers.sendGetRequest(
                        given().pathParam("id", userID),
                        getConfig(USERS_ID_PATH))
                .extract().as(UserData.class);
    }

    @Test
    @Order(3)
    public void userGetTest() {
        GorestApiWrappers.sendGetRequest(
                        getConfig(USERS_PATH))
                .assertThat()
                .body("$", hasSize(10));
    }

    @Test
    @Order(4)
    public void schemeValidationWithPath() {
        GorestApiWrappers.sendGetRequest(
                        given().pathParam("id", userID),
                        getConfig(USERS_ID_PATH))
                .assertThat()
                .body(matchesJsonSchemaInClasspath("gorest_user_scheme.json"));
    }

    @Test
    public void userCreatedTest() {
        UserData userData = GorestPostDataHelper.createUserData();
        UserData actualResponse = GorestApiWrappers.sendPostRequest(
                getConfig(USERS_PATH),
                userData,
                UserData.class);
        assertEquals(userData, actualResponse);
    }

    @Test
    public void userCreatedTestNegative() {
        UserData userData = GorestPostDataHelper.createUserData();
        GorestApiWrappersNegative<UserData> wrappersNegative = new GorestApiWrappersNegative<>();
        wrappersNegative.sendPostRequest(//we haven't authentication
                getConfig(USERS_PATH),
                userData);
    }

    @Test
    @Order(5)
    public void userUpdatedTest() {
        userData = GorestPutDataHelper.updateUserData();
        UserData actualResponse = GorestApiWrappers.sendPutRequest(
                given().pathParam("id", userID),
                getConfig(USERS_ID_PATH),
                userData,
                UserData.class);
        assertEquals(userData, actualResponse);
    }

    @Test
    @Order(6)
    public void userUpdatedTestNegative() {
        userData = GorestPutDataHelper.updateUserData();
        GorestApiWrappersNegative<UserData> wrappersNegative = new GorestApiWrappersNegative<>();
        wrappersNegative.sendPutRequest(
                given().pathParam("id", userID),
                getConfig(POST_ID_PATH),//the path failed
                userData);
    }

    @Test
    @Order(7)
    public void userPatchTest() {
        userData = GorestPatchDataHelper.patchUserData();
        UserData actualResponse = GorestApiWrappers.sendPatchRequest(
                given().pathParam("id", userID),
                getConfig(USERS_ID_PATH),
                userData,
                UserData.class);
        assertEquals(userData, actualResponse);
    }

    @Test
    @Order(8)
    public void userPatchTestWithParam() {
        userData = GorestPatchDataHelper.patchUserData("John Malcovich", "male");
        UserData actualResponse = GorestApiWrappers.sendPatchRequest(
                given().pathParam("id", userID),
                getConfig(USERS_ID_PATH),
                userData,
                UserData.class);
        assertEquals(userData, actualResponse);
    }

    @Test
    @Order(9)
    public void userPatchTestWithQueryParam() {
        UserData actualResponse = GorestApiWrappers.sendPatchRequest(
                given().pathParam("id", userID).queryParam("name", "Andrew Zorro").queryParam("gender", "male"),
                getConfig(USERS_ID_PATH),
                userData,
                UserData.class);
        System.out.println(actualResponse);
    }

    @Test
    @Order(10)
    public void userPatchTestWithQueryParamNegative() {
        GorestApiWrappersNegative<UserData> wrappersNegative = new GorestApiWrappersNegative<>();
        wrappersNegative.sendPatchRequest(//a param failed
                given().pathParam("id", userID).queryParam("name", "Andrew Zorro").queryParam("gender", "active"),
                getConfig(USERS_ID_PATH),
                userData);
    }

    @Test
    @Order(11)
    public void userDeleteTest() {
        GorestApiWrappers.sendDeleteRequest(
                given().pathParam("id", userID),
                getConfig(USERS_ID_PATH));
    }

    @Test
    public void userDeleteTestNegative() {
        userGetID();
        userDeleteTest();
        GorestApiWrappersNegative<UserData> wrappersNegative = new GorestApiWrappersNegative<>();
        wrappersNegative.sendDeleteRequest(//a user not exists
                given().pathParam("id", userID),
                getConfig(USERS_ID_PATH));
    }
}
