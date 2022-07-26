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
public class TagController {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private TagRepository tagRepository;

    @GetMapping("/{question_id}/tag/{id}")
    public String showTag(@PathVariable(value = "id") long id,
                             @PathVariable(value = "question_id") long question_id,
                             Model model){
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tag Id:" + id));
        model.addAttribute("question_id", question_id);
        model.addAttribute("tag", tag);
        return "tag";
    }

    @GetMapping("/{id}/newtag")
    public String createTag(@PathVariable(value = "id") long id, Model model){
        model.addAttribute("tag", new Tag());
        model.addAttribute("question_id", id);
        return "newtag";
    }

    @PostMapping("/savetag")
    public String saveTag(Model model, @Valid Tag tag,
                             @RequestParam(value = "question_id") long id,
                             RedirectAttributes redirectAttributes){
        tag.setQuestion(questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid question Id:" + id)));
        tagRepository.save(tag);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/question/{id}";
    }

    @GetMapping("/{question_id}/deletetag/{id}")
    public String deleteTag(@PathVariable("id") long id,
                               @PathVariable(value = "question_id") long question_id,
                               Model model, RedirectAttributes redirectAttributes
    ) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tag Id:" + id));
        tagRepository.delete(tag);
        redirectAttributes.addAttribute("question_id", question_id);
        return "redirect:/question/{question_id}";
    }

    @GetMapping("/{question_id}/updatetag/{id}")
    public String updateTag(@PathVariable(value = "id") long id,
                               @PathVariable(value = "question_id") long question_id,
                               Model model){
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tag Id:" + id));
        model.addAttribute("question_id", question_id);
        model.addAttribute("tag", tag);
        return "updatetag";
    }
}

