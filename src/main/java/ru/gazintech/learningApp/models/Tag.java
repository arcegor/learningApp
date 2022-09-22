package ru.gazintech.learningApp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"manual", "questions"})
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    private String name;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {
                    CascadeType.MERGE
            }, mappedBy = "tags")
    private List<Question> questions;

    @ManyToOne(fetch = FetchType.EAGER)
    private Manual manual;

    public void addQuestion(Question question) {
        this.questions.add(question);
        question.getTags().add(this);
    }

    public void removeQuestion(long questionId) {
        Question question = this.questions.stream().filter(t -> t.getId() == questionId).findFirst().orElse(null);
        if (question != null) {
            this.questions.remove(question);
            question.getTags().remove(this);
        }
    }

}
