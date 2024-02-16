package org.example;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
public class PostData {

    private int id;
    private int user_id;
    @Setter
    private String title;
    @Setter
    private String body;
    @Setter
    private CommentsData commentsData;
    @Setter
    private String updatedAt;
    @Setter
    private String createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostData postData)) return false;
        return Objects.equals(getTitle(), postData.getTitle()) && Objects.equals(getBody(), postData.getBody());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getBody());
    }
}
