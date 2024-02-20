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
        new GorestUsersTests().userGetTest();
        PostData postData = new PostData();
        Faker faker = new Faker();
        postData.setUser_id(GorestUsersTests.userID);
        postData.setTitle(faker.book().title());
        postData.setBody(faker.lorem().paragraph());
        return postData;
    }

    public static CommentData updateCommentData() {
        new GorestPostTests().postGetTest();
        CommentData commentData = new CommentData();
        Faker faker = new Faker();
        commentData.setPost_id(GorestPostTests.postID);
        commentData.setName(faker.name().fullName());
        commentData.setEmail(faker.internet().emailAddress());
        commentData.setBody(faker.lorem().paragraph());
        return commentData;
    }

    public static TodoData updateTodoData() {
        new GorestUsersTests().userGetTest();
        TodoData todoData = new TodoData();
        todoData.setUser_id(GorestUsersTests.userID);
        todoData.setTitle(new Faker().book().title());
        todoData.setDue_on("2024-03-01T00:00:00.000+05:30");
        todoData.setStatus("pending");
        return todoData;
    }
}
