package org.example;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;


@Getter
public class UserData {

    private int id;
    @Setter
    private String name;
    @Setter
    private String email;
    @Setter
    private String gender;
    @Setter
    private String status;
//    @Setter
//    private PostData postData;
//    @Setter
//    private TodosData todosData;
//    @Setter
//    private String updatedAt;
//    @Setter
//    private String createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserData userData)) return false;
        return Objects.equals(getName(), userData.getName()) && Objects.equals(getEmail(), userData.getEmail()) && Objects.equals(getGender(), userData.getGender()) && Objects.equals(getStatus(), userData.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getEmail(), getGender(), getStatus());
    }

    @Override
    public String toString() {
        return "UserData{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
