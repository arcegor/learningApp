package ru.gazintech.learningApp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
public class TestQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String name;

    @ManyToOne
    private Test test;

    @OneToMany(mappedBy = "testQuestion", orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<TestAnswer> testAnswers = new ArrayList<>();

}
