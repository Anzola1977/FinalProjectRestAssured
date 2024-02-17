import com.github.javafaker.Faker;
import org.example.UserData;

public class GorestDataHelper {

    public static UserData createUserData() {
        UserData userData = new UserData();
        Faker faker = new Faker();
        userData.setName(faker.name().fullName());
        userData.setEmail(faker.internet().emailAddress());
        userData.setGender(faker.demographic().sex().toLowerCase());
        userData.setStatus("active");
        return userData;
    }
}
