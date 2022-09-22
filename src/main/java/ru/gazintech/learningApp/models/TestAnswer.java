package ru.gazintech.learningApp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"testQuestion"})
@Table(name = "testAnswer")
public class TestAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @NotBlank
    private String name;

    @ManyToOne
    private TestQuestion testQuestion;

    private Boolean flag;
}
