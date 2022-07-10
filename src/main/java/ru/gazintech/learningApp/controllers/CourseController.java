package ru.gazintech.learningApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.gazintech.learningApp.models.Course;
import ru.gazintech.learningApp.repository.CourseRepository;

import javax.validation.Valid;

@Controller("/courses")
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/newcourse")
    public String showAddingCourseForm(Course course) {
        return "addcourse";
    }

    @GetMapping("/backtomain")
    public String backToMain(Course course) {
        return "main";
    }


    @PostMapping("/addcourse")
    public String addCourse(@Valid Course course, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "addcourse";
        }
        courseRepository.save(course);
        return "redirect:/courses";
    }

    @GetMapping("/deletecourse/{id}")
    public String deleteCourse(@PathVariable("id") long id, Model model) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        courseRepository.delete(course);
        return "redirect:/courses";
    }


}
