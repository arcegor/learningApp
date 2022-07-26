package ru.gazintech.learningApp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "test")
@ToString(exclude = {"manual"})
@NoArgsConstructor
@AllArgsConstructor
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    @Length(min = 1, max = 255)
    private String name;

    private String description;

    @Value(value = "100")
    private Integer number;

    @OneToMany(mappedBy = "test", orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<TestQuestion> testQuestions;

    @ManyToOne
    private Manual manual;



}
