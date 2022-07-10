package ru.gazintech.learningApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.gazintech.learningApp.models.Course;
import ru.gazintech.learningApp.models.Test;
import ru.gazintech.learningApp.repository.CourseRepository;
import ru.gazintech.learningApp.repository.TestRepository;

import java.util.List;

@Controller("/main")
public class MainController {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TestRepository testRepository;

    @GetMapping("/")
    public String main(Model model){
        return "main";
    }

    @GetMapping("/tests")
    public String showTests(Model model){
        List<Test> testList = testRepository.findAll();
        model.addAttribute("testList", testList);
        return "tests";
    }

    @GetMapping("/courses")
    public String showCourses(Model model){
        List<Course> courseList = courseRepository.findAll();
        model.addAttribute("courseList", courseList);
        return "courses";
    }

    @GetMapping("/faq")
    public String showFaq(Model model){
        return "faq";
    }

    @GetMapping("/materials")
    public String showMaterials(Model model){
        return "materials";
    }

}
