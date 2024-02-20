import com.github.javafaker.Faker;
import org.example.UserData;

public class GorestPatchDataHelper {

    public static UserData patchUserData() {
        GorestUsersTests.userData.setEmail(new Faker().internet().emailAddress());
        return GorestUsersTests.userData;
    }

    public static UserData patchUserData(String name, String gender) {
        GorestUsersTests.userData.setName(name);
        GorestUsersTests.userData.setGender(gender);
        return GorestUsersTests.userData;
    }
}
