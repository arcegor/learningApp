package ru.gazintech.learningApp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"test"})
@Table(name = "testQuestion")
public class TestQuestion implements Comparable<TestQuestion> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @NotBlank
    private String name;

    @Value(value = "100")
    private Integer number;

    @ManyToOne
    private Test test;

    @OneToMany(mappedBy = "testQuestion", orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<TestAnswer> testAnswers = new ArrayList<>();

    @Override
    public int compareTo(TestQuestion tq) {
        if (getNumber() == null || tq.getNumber() == null) {
            return 0;
        }
        return getNumber().compareTo(tq.getNumber());
    }
}