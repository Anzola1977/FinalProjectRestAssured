package org.example;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
public class TodoData {

    private int id;
    @Setter
    private int user_id;
    @Setter
    private String title;
    @Setter
    private String due_on;
    @Setter
    private String status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoData todoData)) return false;
        return getUser_id() == todoData.getUser_id() && Objects.equals(getTitle(), todoData.getTitle()) && Objects.equals(getDue_on(), todoData.getDue_on()) && Objects.equals(getStatus(), todoData.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser_id(), getTitle(), getDue_on(), getStatus());
    }

    @Override
    public String toString() {
        return "TodosData{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", title='" + title + '\'' +
                ", due_on='" + due_on + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
