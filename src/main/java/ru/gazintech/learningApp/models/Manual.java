package ru.gazintech.learningApp.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "manual")
public class Manual {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank
    @Length(min = 1, max = 255)
    private String name;

    private String description;

    @OneToMany(mappedBy = "manual", orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<Material> materials;
    @OneToMany(mappedBy = "manual", orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<Course> courses;
    @OneToMany(mappedBy = "manual", orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<Test> tests;
    @OneToMany(mappedBy = "manual", orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<Question> questions;
}
