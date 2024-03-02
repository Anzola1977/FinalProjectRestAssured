package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.example.ConfigMap.BASE_URI;

public class BaseTest {
    protected static Properties properties;

    @BeforeAll
    @Order(1)
    public static void globalSetUp() {
        properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream("src/main/resources/config.properties");
            properties.load(fileInputStream);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        RestAssured.baseURI = getConfig(BASE_URI);
    }

    public static String getConfig(String key) {
        return properties.getProperty(key);
    }

    @AfterEach
    public void tearDown() {
        RestAssured.reset();
    }
}
