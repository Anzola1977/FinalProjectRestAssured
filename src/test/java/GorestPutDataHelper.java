import com.github.javafaker.Faker;
import org.example.CommentData;
import org.example.PostData;
import org.example.TodoData;
import org.example.UserData;

public class GorestPutDataHelper {

    public static UserData updateUserData() {
        Faker faker = new Faker();
        GorestUsersTests.userData.setName(faker.name().fullName());
        GorestUsersTests.userData.setEmail(faker.internet().emailAddress());
        GorestUsersTests.userData.setGender(faker.demographic().sex().toLowerCase());
        GorestUsersTests.userData.setStatus("active");
        return GorestUsersTests.userData;
    }

    public static PostData updatePostData() {
        Faker faker = new Faker();
        GorestPostTests.postData.setTitle(faker.book().title());
        GorestPostTests.postData.setBody(faker.lorem().paragraph());
        return GorestPostTests.postData;
    }

    public static CommentData updateCommentData() {
        Faker faker = new Faker();
        GorestCommentsTests.commentData.setEmail(faker.internet().emailAddress());
        GorestCommentsTests.commentData.setBody(faker.lorem().paragraph());
        return GorestCommentsTests.commentData;
    }

    public static TodoData updateTodoData() {
        GorestTodosTests.todoData.setTitle(new Faker().book().title());
        GorestTodosTests.todoData.setDue_on("2024-03-01T00:00:00.000+05:30");
        GorestTodosTests.todoData.setStatus("pending");
        return GorestTodosTests.todoData;
    }
}
