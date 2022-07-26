package ru.gazintech.learningApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.gazintech.learningApp.models.*;
import ru.gazintech.learningApp.repository.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class FaqController {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ManualRepository manualRepository;
    @Autowired
    private TagRepository tagRepository;

    @GetMapping("/question/{id}")
    public String showQuestion(@PathVariable(value = "id") long id,
                             Model model){
        List<Tag> tagList = tagRepository.findByQuestion_id(id);
        Question question =  questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid question Id:" + id));
        model.addAttribute("question", question);
        model.addAttribute("lessonList", tagList);
        return "question";
    }

    @GetMapping("/{id}/newquestion")
    public String createQuestion(@PathVariable(value = "id") long id, Model model){
        model.addAttribute("question", new Question());
        model.addAttribute("manual_id", id);
        return "newquestion";
    }

    @PostMapping("/savequestion")
    public String saveQuestion(Model model, @Valid Question question,
                             @RequestParam(value = "manual_id") long id,
                             RedirectAttributes redirectAttributes){
        question.setManual(manualRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid question Id:" + id)));
        questionRepository.save(question);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/{id}/questions";
    }

    @GetMapping("/{manual_id}/deletequestion/{id}")
    public String deleteQuestion(@PathVariable("id") long id,
                               @PathVariable(value = "manual_id") long manual_id,
                               Model model, RedirectAttributes redirectAttributes
    ) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid question Id:" + id));
        questionRepository.delete(question);
        redirectAttributes.addAttribute("manual_id", manual_id);
        return "redirect:/{manual_id}/questions";
    }

    @GetMapping("/{manual_id}/updatequestion/{id}")
    public String updateQuestion(@PathVariable(value = "id") long id,
                               @PathVariable(value = "manual_id") long manual_id,
                               Model model){
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid question Id:" + id));
        model.addAttribute("manual_id", manual_id);
        model.addAttribute("question", question);
        return "updatequestion";
    }
}
