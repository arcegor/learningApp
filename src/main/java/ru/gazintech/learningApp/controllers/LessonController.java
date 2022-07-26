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
public class LessonController {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private LessonRepository lessonRepository;

    @GetMapping("/{course_id}/lesson/{id}")
    public String showLesson(@PathVariable(value = "id") long id,
                             @PathVariable(value = "course_id") long course_id,
                             Model model){
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid lesson Id:" + id));
        model.addAttribute("course_id", course_id);
        model.addAttribute("lesson", lesson);
        return "lesson";
    }

    @GetMapping("/{id}/newlesson")
    public String createLesson(@PathVariable(value = "id") long id, Model model){
        model.addAttribute("lesson", new Lesson());
        model.addAttribute("course_id", id);
        return "newlesson";
    }

    @PostMapping("/savelesson")
    public String saveLesson(Model model, @Valid Lesson lesson,
                             @RequestParam(value = "course_id") long id,
                             RedirectAttributes redirectAttributes){
        lesson.setCourse(courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id)));
        lessonRepository.save(lesson);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/course/{id}";
    }

    @GetMapping("/{course_id}/deletelesson/{id}")
    public String deleteLesson(@PathVariable("id") long id,
                               @PathVariable(value = "course_id") long course_id,
                               Model model, RedirectAttributes redirectAttributes
    ) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid lesson Id:" + id));
        lessonRepository.delete(lesson);
        redirectAttributes.addAttribute("course_id", course_id);
        return "redirect:/course/{course_id}";
    }

    @GetMapping("/{course_id}/updatelesson/{id}")
    public String updateCourse(@PathVariable(value = "id") long id,
                               @PathVariable(value = "course_id") long course_id,
                               Model model){
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid lessin Id:" + id));
        model.addAttribute("course_id", course_id);
        model.addAttribute("lesson", lesson);
        return "updatelesson";
    }
}
