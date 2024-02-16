package utils;

import com.github.javafaker.Faker;
import org.example.UserData;

import static java.time.LocalTime.now;

public class GorestDataHelper {
    public static UserData createUserData() {
        UserData userData = new UserData();
        Faker faker = new Faker();
        userData.setName(faker.name().fullName());
        userData.setEmail(faker.internet().emailAddress());
        userData.setGender(faker.demographic().sex());
        userData.setStatus("active");
        return userData;
    }

//    public static UserData createUserData() {
//        return createUserData("Jack Nikolson");
//    }
}
