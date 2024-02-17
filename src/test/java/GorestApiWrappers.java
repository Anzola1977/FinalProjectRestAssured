import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.example.UserData;

import static io.restassured.RestAssured.given;

public class GorestApiWrappers{

    private final static int DEFAULT_STATUS_CODE = 200;

    public static UserData sendPostRequest(String endpoint, UserData requestBody) {
        return given()
                .filter(new GlobalTokenFilter(BaseTest.getConfig("token")))
                .contentType(ContentType.JSON)
                .body(requestBody)
                .log().all()
                .when()
                .post(endpoint)
                .then()
                .log().all()
                .assertThat()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .log().ifValidationFails()
                .extract().as(UserData.class);
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

    public static ValidatableResponse sendGetRequest(String callPath, int statusCode){
        return sendGetRequest(given(), callPath, statusCode);
    }

    public static ValidatableResponse sendGetRequest(RequestSpecification requestSpecification, String callPath){
        return sendGetRequest(requestSpecification, callPath, DEFAULT_STATUS_CODE);
    }

    public static ValidatableResponse sendGetRequest(String callPath){
        return sendGetRequest(given(), callPath, DEFAULT_STATUS_CODE);
    }
}