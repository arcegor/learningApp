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
import java.util.Set;

@Data
@Entity
@Table(name = "lesson")
@ToString(exclude = {"course"})
@NoArgsConstructor
@AllArgsConstructor
public class Lesson implements Comparable<Lesson>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    private String name;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String data;

    private String description;

    private String video;

    @Value(value = "100")
    private Integer number;

    @ManyToOne
    private Course course;

    @Override
    public int compareTo(Lesson l) {
        if (getNumber() == null || l.getNumber() == null) {
            return 0;
        }
        return getNumber().compareTo(l.getNumber());
    }
}

