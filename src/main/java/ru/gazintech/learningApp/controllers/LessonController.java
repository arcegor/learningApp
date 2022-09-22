package ru.gazintech.learningApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.gazintech.learningApp.models.Course;
import ru.gazintech.learningApp.models.Lesson;
import ru.gazintech.learningApp.models.Manual;
import ru.gazintech.learningApp.repository.CourseRepository;
import ru.gazintech.learningApp.repository.LessonRepository;
import ru.gazintech.learningApp.repository.ManualRepository;
import ru.gazintech.learningApp.service.LessonService;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class LessonController {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private LessonService lessonService;

    @GetMapping("/{manual_id}/{course_id}/lesson/{id}")
    public String showLesson(@PathVariable(value = "id") long id,
                             @PathVariable(value = "course_id") long course_id,
                             @PathVariable(value = "manual_id") long manual_id,
                             Model model){
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid lesson Id:" + id));
        Course course =  courseRepository.findById(course_id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id));
        
        List<Lesson> lessonList = course.getLessons();
        lessonList.sort(Lesson::compareTo);
        int flag = -1;
        for (Lesson les : lessonList
             ) {
            if (les.getId() == lesson.getId()){
                flag = (int) les.getId();
            }
        }
        
        model.addAttribute("course_id", course_id);
        model.addAttribute("manual_id", manual_id);
        model.addAttribute("lesson", lesson);
        model.addAttribute("course", course);
        model.addAttribute("lessonList", lessonList);
        model.addAttribute("flag", flag);
        return "lesson";
    }

    @GetMapping("{manual_id}/{id}/newlesson")
    public String createLesson(@PathVariable(value = "id") long id,
                               @PathVariable(value = "manual_id") long manual_id,
                               Model model){
        model.addAttribute("lesson", new Lesson());
        model.addAttribute("course_id", id);
        model.addAttribute("manual_id", manual_id);
        return "newlesson";
    }

    @PostMapping("/savelesson")
    public String saveLesson(Model model,
                             @Valid Lesson lesson,
                             @RequestParam(value = "course_id") long id,
                             @RequestParam(value = "manual_id") long manual_id,
                             @RequestParam("file") MultipartFile file,
                             RedirectAttributes redirectAttributes){
        lesson.setCourse(courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course Id:" + id)));
        lesson.setVideo(lessonService.uploadVideo(file, lessonService.getLastLessonId()));
        lessonRepository.save(lesson);
        redirectAttributes.addAttribute("id", id);
        redirectAttributes.addAttribute("manual_id", manual_id);
        return "redirect:/{manual_id}/course/{id}";
    }

    @GetMapping("/{manual_id}/{course_id}/deletelesson/{id}")
    public String deleteLesson(@PathVariable("id") long id,
                               @PathVariable(value = "course_id") long course_id,
                               @PathVariable(value = "manual_id") long manual_id,
                               Model model, RedirectAttributes redirectAttributes
    ) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid lesson Id:" + id));
        lessonRepository.delete(lesson);

        redirectAttributes.addAttribute("course_id", course_id);
        redirectAttributes.addAttribute("manual_id", manual_id);
        return "redirect:/{manual_id}/course/{course_id}";
    }

    @GetMapping("/{manual_id}/{course_id}/updatelesson/{id}")
    public String updateCourse(@PathVariable(value = "id") long id,
                               @PathVariable(value = "course_id") long course_id,
                               @PathVariable(value = "manual_id") long manual_id,
                               Model model){
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid lessin Id:" + id));
        model.addAttribute("course_id", course_id);
        model.addAttribute("manual_id", manual_id);
        model.addAttribute("lesson", lesson);
        return "updatelesson";
    }
}
