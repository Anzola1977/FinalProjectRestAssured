package utils;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import tests.BaseTest;

import static io.restassured.RestAssured.given;

public class GorestApiWrappers{

    private final static int DEFAULT_STATUS_CODE = 200;

    public static <T> T sendPostRequest(String endpoint, T requestBody, Class<T> response) {
        return given()
                .filter(new GlobalTokenFilter(BaseTest.getConfig("token")))
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(endpoint)
                .then()
                .assertThat()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .log().ifValidationFails()
                .extract().as(response);
    }

    public static ValidatableResponse sendGetRequest(RequestSpecification requestSpecification, String callPath, int statusCode){
        return given()
                .spec(requestSpecification)
                .when()
                .get(callPath)
                .then()
                .statusCode(statusCode)
                .contentType(ContentType.JSON)
                .log().ifValidationFails();
    }

    public static ValidatableResponse sendGetRequest(RequestSpecification requestSpecification, String callPath){
        return sendGetRequest(requestSpecification, callPath, DEFAULT_STATUS_CODE);
    }

    public static ValidatableResponse sendGetRequest(String callPath){
        return sendGetRequest(given(), callPath, DEFAULT_STATUS_CODE);
    }

    public static <T> T sendPutRequest(RequestSpecification requestSpecification, String endpoint, T requestBody, Class<T> response) {
        return given()
                .spec(requestSpecification)
                .filter(new GlobalTokenFilter(BaseTest.getConfig("token")))
                .contentType(ContentType.JSON)
                .body(requestBody)
                .log().all()
                .when()
                .put(endpoint)
                .then()
                .log().all()
                .assertThat()
                .statusCode(DEFAULT_STATUS_CODE)
                .contentType(ContentType.JSON)
                .log().ifValidationFails()
                .extract().as(response);
    }

    public static <T> T sendPatchRequest(RequestSpecification requestSpecification, String endpoint, T requestBody, Class<T> response) {
        return given()
                .spec(requestSpecification)
                .filter(new GlobalTokenFilter(BaseTest.getConfig("token")))
                .contentType(ContentType.JSON)
                .body(requestBody)
                .log().all()
                .when()
                .patch(endpoint)
                .then()
                .log().all()
                .assertThat()
                .statusCode(DEFAULT_STATUS_CODE)
                .contentType(ContentType.JSON)
                .log().ifValidationFails()
                .extract().as(response);
    }

    public static void sendDeleteRequest(RequestSpecification requestSpecification, String endpoint) {
        given()
                .spec(requestSpecification)
                .filter(new GlobalTokenFilter(BaseTest.getConfig("token")))
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .delete(endpoint)
                .then()
                .log().all()
                .assertThat()
                .statusCode(204)
                .log().ifValidationFails();
    }
}
