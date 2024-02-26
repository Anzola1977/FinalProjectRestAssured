package utils;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import tests.BaseTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GorestApiWrappersNegative<T> {

    public void sendPostRequest(String endpoint, T request) {
        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post(endpoint)
                .then()
                .assertThat()
                .statusCode(401)
                .contentType(ContentType.JSON)
                .log().ifValidationFails()
                .body("message", equalTo("Authentication failed"));
    }

    public void sendPutRequest(RequestSpecification requestSpecification, String endpoint, T requestBody) {
        given()
                .spec(requestSpecification)
                .filter(new GlobalTokenFilter(BaseTest.getConfig("token")))
                .contentType(ContentType.JSON)
                .body(requestBody)
                //.log().all()
                .when()
                .put(endpoint)
                .then()
                //.log().all()
                .assertThat()
                .statusCode(404)
                .contentType(ContentType.JSON)
                .log().ifValidationFails()
                .body("message", equalTo("Resource not found"));
    }

    public void sendPatchRequest(RequestSpecification requestSpecification, String endpoint, T requestBody) {
        given()
                .spec(requestSpecification)
                .filter(new GlobalTokenFilter(BaseTest.getConfig("token")))
                .contentType(ContentType.JSON)
                .body(requestBody)
                //.log().all()
                .when()
                .patch(endpoint)
                .then()
                //.log().all()
                .assertThat()
                .statusCode(422)
                .contentType(ContentType.JSON)
                .log().ifValidationFails();
    }

    public void sendDeleteRequest(RequestSpecification requestSpecification, String endpoint) {
        given()
                .spec(requestSpecification)
                .filter(new GlobalTokenFilter(BaseTest.getConfig("token")))
                .contentType(ContentType.JSON)
                //.log().all()
                .when()
                .delete(endpoint)
                .then()
                //.log().all()
                .assertThat()
                .statusCode(404)
                .log().ifValidationFails()
                .body("message", equalTo("Resource not found"));
    }
}
