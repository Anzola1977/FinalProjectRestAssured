package org.example;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
public class CommentData {

    private int id;
    @Setter
    private int post_id;
    @Setter
    private String name;
    @Setter
    private String email;
    @Setter
    private String body;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentData that)) return false;
        return getPost_id() == that.getPost_id() && Objects.equals(getName(), that.getName()) && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getBody(), that.getBody());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPost_id(), getName(), getEmail(), getBody());
    }

    @Override
    public String toString() {
        return "CommentsData{" +
                "id=" + id +
                ", post_id=" + post_id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
