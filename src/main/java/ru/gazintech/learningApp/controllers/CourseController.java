package ru.gazintech.learningApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.gazintech.learningApp.models.Course;
import ru.gazintech.learningApp.models.Lesson;
import ru.gazintech.learningApp.models.Manual;
import ru.gazintech.learningApp.repository.CourseRepository;
import ru.gazintech.learningApp.repository.LessonRepository;
import ru.gazintech.learningApp.repository.ManualRepository;

import javax.validation.Valid;
import java.util.List;

@Controller
public class CourseController {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private ManualRepository manualRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @GetMapping("/course/{id}")
    public String showCourse(@PathVariable(value = "id") long id,
                             Model model){
        List<Lesson> lessonList = lessonRepository.findByCourse_id(id);
        Course course =  courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        model.addAttribute("course", course);
        model.addAttribute("lessonList", lessonList);
        return "course";
    }

    @GetMapping("/{id}/newcourse")
    public String createCourse(@PathVariable(value = "id") long id, Model model){
        model.addAttribute("course", new Course());
        model.addAttribute("manual_id", id);
        return "newcourse";
    }

    @PostMapping("/savecourse")
    public String saveCourse(Model model, @Valid Course course,
                             @RequestParam(value = "manual_id") long id,
                             RedirectAttributes redirectAttributes){
        course.setManual(manualRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id)));
        courseRepository.save(course);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/{id}/courses";
    }

    @GetMapping("/{manual_id}/deletecourse/{id}")
    public String deleteCourse(@PathVariable("id") long id,
                               @PathVariable(value = "manual_id") long manual_id,
                               Model model, RedirectAttributes redirectAttributes
                               ) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        courseRepository.delete(course);
        redirectAttributes.addAttribute("manual_id", manual_id);
        return "redirect:/{manual_id}/courses";
    }

    @GetMapping("/{manual_id}/updatecourse/{id}")
    public String updateCourse(@PathVariable(value = "id") long id,
                               @PathVariable(value = "manual_id") long manual_id,
                               Model model){
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        model.addAttribute("manual_id", manual_id);
        model.addAttribute("course", course);
        return "updatecourse";
    }
}
