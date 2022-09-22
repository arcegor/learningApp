package ru.gazintech.learningApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.gazintech.learningApp.models.*;
import ru.gazintech.learningApp.repository.*;

import java.util.List;

@Controller("/manual")
public class ManualController {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ManualRepository manualRepository;
    @Autowired
    private TestRepository testRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TagRepository tagRepository;

    @GetMapping("/{id}/tests")
    public String showTests(@PathVariable(value = "id") long id, Model model){
        List<Test> testList = testRepository.findByManual_IdOrderByNumber(id);
        Manual manual =  manualRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid manual Id:" + id));
        model.addAttribute("manual", manual);
        model.addAttribute("testList", testList);
        return "tests";
    }

    @GetMapping("/{id}/courses")
    public String showCourses(@PathVariable(value = "id") long id, Model model){
        List<Course> courseList = courseRepository.findByManual_Id(id);
        Manual manual =  manualRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid manual Id:" + id));
        model.addAttribute("manual", manual);
        model.addAttribute("courseList", courseList);
        return "courses";
    }

    @GetMapping("/{id}/questions")
    public String showQuestions(@PathVariable(value = "id") long id, Model model){
        List<Question> questionList = questionRepository.findByManual_Id(id);
        List<Tag> tagList = tagRepository.findAll();
        Manual manual =  manualRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid manual Id:" + id));
        model.addAttribute("manual", manual);
        model.addAttribute("questionList", questionList);
        model.addAttribute("tagList", tagList);
        model.addAttribute("tag", new Tag());
        return "questions";
    }

    @GetMapping("/{id}/materials")
    public String showMaterials(@PathVariable(value = "id") long id,
                                Model model){
        List<Material> materialList = materialRepository.findByManual_Id(id);
        Manual manual =  manualRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid manual Id:" + id));
        model.addAttribute("manual", manual);
        model.addAttribute("materialList", materialList);
        return "materials";
    }

}
