package Utils;

import com.github.javafaker.Faker;
import org.example.Data;
import org.example.GadgetPost;
import org.example.UserData;

import static java.time.LocalTime.now;

public class GorestDataHelper {
    public static UserData createUserData(String name) {
        UserData userData = new UserData();
        Faker faker = new Faker();
        userData.setName(name + now());
        userData.setEmail(faker.address().fullAddress().);
        userData.setGender();
        userData.setStatus();
        return userData;
    }

    public static UserData createUserData() {
        return createUserData("Jack Nikolson");
    }
}
