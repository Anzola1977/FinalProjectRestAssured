package org.example;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
public class TodosData {

    private int id;
    private int user_id;
    @Setter
    private String title;
    @Setter
    private String due_on;
    @Setter
    private String status;
    @Setter
    private String updatedAt;
    @Setter
    private String createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodosData todosData)) return false;
        return Objects.equals(getTitle(), todosData.getTitle()) && Objects.equals(getDue_on(), todosData.getDue_on()) && Objects.equals(getStatus(), todosData.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getDue_on(), getStatus());
    }
}
