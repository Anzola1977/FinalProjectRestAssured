import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

public class HomeWorkTest extends BaseTest {

//    NewPost postEntity = new NewPost();
//    Data data = new Data();
//    public static String id;

    @BeforeEach
    public void setUp() {
        // "Документация" по вызовам - https://restful-api.dev/
        RestAssured.baseURI = "https://api.restful-api.dev/";
    }

    // Ваша задача написать по одному тесту на каждый вызов:
    // 1. Для вызова GET LIST OF ALL OBJECTS проверьте,
    // что количество объектов в ответном массиве равно 13
    @Test
    public void getRequestTest() {
        given()
                .when()
                .get("/objects")
                .then().assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .log().all()
                .body("id", hasSize(6));
    }


    // 2. Для вызова GET LIST OF OBJECTS BY IDS проверьте,
// что при вызове с параметрами id 10 и 12 (одновременно),
// у обоих значение Capacity = 64 GB
    @Test
    public void getRequestTestWithID() {
        String firstID = "10";
        String secondID = "12";
        given()
                .when()
                .get(String.format("/objects?id=%s&id=%s", firstID, secondID))
                .then().assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .log().all()
                .body("[0].data.Capacity", equalTo("64 GB"))
                .body("[1].data.Capacity", equalTo("64 GB"));
    }

    // 3. Для вызова GET SINGLE OBJECT с любым id сделайте "контрактный" тест
    @Test
    public void postSchemeValidationTest() {
        String postId = "10";
        given()
                .pathParam("id", postId)
                .when()
                .get("/objects/{id}")
                .then().assertThat()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .log().all()
                .body(matchesJsonSchemaInClasspath("post-scheme2.json"));

    }

    // 4. Для вызова POST ADD OBJECT сделайте добавление новой сущности
// с использованием класса сущности (можете использовать пример вызова с сайта)
// и проверьте, что переданные данные присутствуют в ответе (также используя класс)
//    @Test
//    public void entityPostRequestTest() {
//        data.setYear(2019);
//        data.setPrice(1849.99);
//        data.setCpuModel("Intel Core i9");
//        data.setHardDiskSize("1 TB");
//        postEntity.setName("Apple MacBook Pro 16");
//        postEntity.setData(data);
//
//        NewPost actualResponce = given()
//                .contentType(ContentType.JSON)
//                .body(postEntity)
//                .log().all()
//                .when()
//                .post("/objects")
//                .then()
//                .assertThat()
//                .statusCode(200)
//                .contentType(ContentType.JSON)
//                .log().all()
//                .extract().as(NewPost.class);
//
//        id = actualResponce.getId();
//
//        assertEquals(actualResponce, postEntity);
//
//    }
//
//    // 5. Для вызова PUT UPDATE OBJECT сделайте класс телефона,
//// обновите сущность с id 1 или 3 и проверьте ответ
//    @Test
//    public void putRequestTest() {
//        entityPostRequestTest();
//        data.setYear(2023);
//        data.setPrice(1800.50);
//        data.setCpuModel("Intel Core i9");
//        data.setHardDiskSize("3 TB");
//        postEntity.setName("Apple iphone Pro 16");
//        postEntity.setData(data);
//
//        NewPost actualResponse = given()
//                .pathParam("id", id)
//                .body(postEntity)
//                .log().all()
//                .contentType(ContentType.JSON)
//                .when()
//                .put("/objects/{id}")
//                .then().assertThat()
//                .statusCode(200)
//                .contentType(ContentType.JSON)
//                .log().all()
//                .extract().as(NewPost.class);
//
//        assertEquals(postEntity, actualResponse);
//    }
//
//    // 6. Для вызова PATCH PARTIALLY UPDATE OBJECT проверьте
//// обновление любого поля любого понравившегося вам объекта
//    @Test
//    public void patchRequestTest() {
//        entityPostRequestTest();
//        data.setPrice(1200.7);
//        postEntity.setName("Nokia777");
//        postEntity.setData(data);
//
//        NewPost actualResponse = given()
//                .pathParam("id", id)
//                .body(postEntity)
//                .contentType(ContentType.JSON)
//                .log().all()
//                .when()
//                .patch("/objects/{id}")
//                .then().assertThat()
//                .statusCode(200)
//                .contentType(ContentType.JSON)
//                .log().all()
//                .extract().as(NewPost.class);
//
//        assertEquals(postEntity, actualResponse);
//    }
//
//// 7. Для вызова DELETE OBJECT проверьте статус ответа,
//// сообщение и что в сообщении содержится переданный id объекта
//    @Test
//    public void deleteTest() {
//        patchRequestTest();
//        String responseBody = "Object with id = " + id + " has been deleted.";
//        given().pathParam("id", id)
//                .when()
//                .log().all()
//                .delete("/objects/{id}")
//                .then().assertThat()
//                .statusCode(200)
//                .contentType(ContentType.JSON)
//                .log().ifValidationFails()
//                .body(containsString(id))
//                .body(containsString(responseBody));
//    }
}
