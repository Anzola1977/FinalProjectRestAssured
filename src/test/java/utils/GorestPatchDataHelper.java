package utils;

import com.github.javafaker.Faker;
import org.example.CommentData;
import org.example.PostData;
import org.example.TodoData;
import org.example.UserData;
import tests.GorestCommentsTests;
import tests.GorestPostTests;
import tests.GorestTodosTests;
import tests.GorestUsersTests;

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

    public static PostData patchPostData() {
        GorestPostTests.postData.setTitle(new Faker().book().title());
        return GorestPostTests.postData;
    }

    public static CommentData patchCommentData() {
        GorestCommentsTests.commentData.setBody(new Faker().lorem().paragraph());
        return GorestCommentsTests.commentData;
    }

    public static TodoData patchTodoData() {
        GorestTodosTests.todoData.setDue_on("2024-03-12T00:00:00.000+05:30");
        if (GorestTodosTests.todoData.getStatus().contains("completed")) {
            GorestTodosTests.todoData.setStatus("pending");
        }
        return GorestTodosTests.todoData;
    }
}
