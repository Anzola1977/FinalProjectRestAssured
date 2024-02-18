package org.example;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
public class PostData {

    private int id;
    @Setter
    private int user_id;
    @Setter
    private String title;
    @Setter
    private String body;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostData postData)) return false;
        return getUser_id() == postData.getUser_id() && Objects.equals(getTitle(), postData.getTitle()) && Objects.equals(getBody(), postData.getBody());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser_id(), getTitle(), getBody());
    }

    @Override
    public String toString() {
        return "PostData{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
