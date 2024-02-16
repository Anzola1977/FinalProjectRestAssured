package org.example;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
public class CommentsData {

    private int id;
    private int post_id;
    @Setter
    private String name;
    @Setter
    private String email;
    @Setter
    private String body;
    @Setter
    private String updatedAt;
    @Setter
    private String createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentsData that)) return false;
        return Objects.equals(getName(), that.getName()) && Objects.equals(getEmail(), that.getEmail()) && Objects.equals(getBody(), that.getBody());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getEmail(), getBody());
    }
}
