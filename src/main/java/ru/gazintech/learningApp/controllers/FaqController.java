package ru.gazintech.learningApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.gazintech.learningApp.models.*;
import ru.gazintech.learningApp.models.wrappers.QuestionWrapper;

import ru.gazintech.learningApp.repository.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FaqController {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private ManualRepository manualRepository;
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private QuestionWrapper questionWrapper;

    @GetMapping("/newquestion")
    public String showAddingQuestionForm(
            Model model, Question question) {
        model.addAttribute("tagList", questionWrapper.getTagNames());
        model.addAttribute("questionName", questionWrapper.getName());
        model.addAttribute("answer", questionWrapper.getAnswer());
        model.addAttribute("tags", tagRepository.findAll());
        return "newquestion";
    }

    @PostMapping("/initquestion")
    public String addManualToQuestion(@RequestParam(value = "id") long id){
        Manual manual =  manualRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid manual Id:" + id));
        questionWrapper = new QuestionWrapper();
        questionWrapper.setManual(manual);
        return "redirect:/newquestion";
    }

    @GetMapping("/backtoquestions")
    public String backToQuestions(RedirectAttributes redirectAttributes){
        long id = questionWrapper.getManual().getId();
        redirectAttributes.addAttribute("id", id);
        questionWrapper = new QuestionWrapper();
        return "redirect:/{id}/questions";
    }

    @PostMapping("/questionname")
    public String addQuestionName(@RequestParam String name,
                                  RedirectAttributes redirectAttributes) {
        questionWrapper.setName(name);
        return "redirect:/newquestion";

    }

    @GetMapping("/editquestionname")
    public String editQuestionName(Model model) {
        questionWrapper.setName(null);
        return "redirect:/newquestion";
    }

    @PostMapping("/answer")
    public String addAnswer(@RequestParam String answer) {
        questionWrapper.setAnswer(answer);

        return "redirect:/newquestion";
    }

    @GetMapping("/editanswer")
    public String editAnswer(Model model) {
        questionWrapper.setAnswer(null);
        return "redirect:/newquestion";
    }

    @GetMapping("/savequestion")
    public String saveQuestion(Model model,
                           RedirectAttributes redirectAttributes) {
        Question question = questionWrapper.unwrapToQuestion(questionWrapper);
        //tagRepository.saveAll(question.getTags());
        questionRepository.save(question);
        questionWrapper = new QuestionWrapper();
        long id = question.getManual().getId();
        redirectAttributes.addAttribute("id", id);
        return "redirect:/{id}/questions";
    }

    @GetMapping("/deletequestion/{id}")
    public String deleteQuestion(@PathVariable("id") long id,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid question Id:" + id));
        questionRepository.delete(question);
        redirectAttributes.addAttribute("id", question.getManual().getId());
        return "redirect:/{id}/questions";
    }

    @GetMapping("/{manual_id}/deletetag/{name}")
    public String deleteTagFromManual(@PathVariable(value = "name") String name,
                                      @PathVariable(value = "manual_id") long manual_id,
                                      Model model,
                                      RedirectAttributes redirectAttributes){
        Tag tag = tagRepository.findByName(name);
        tagRepository.delete(tag);
        redirectAttributes.addAttribute("id", manual_id);
        return "redirect:/{id}/questions";
    }

    @PostMapping("/newtag")
    public String newTag(@Valid Tag tag,
                         @RequestParam(value = "id") long id,
                         RedirectAttributes redirectAttributes){
        tag.setManual(manualRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid manual Id:" + id)));
        tag.setQuestions(new ArrayList<>());
        tagRepository.save(tag);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/{id}/questions";
    }

    @GetMapping("/detachtag/{id}")
    public String detachTag(@PathVariable(value = "id") long id){
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tag Id:" + id));
        questionWrapper.detach(tag.getId());
        return "redirect:/newquestion";
    }

    @GetMapping("/{q_id}/detachtagupdate/{id}")
    public String detachTagUpdate(@PathVariable(value = "id") long id,
                                  @PathVariable(value = "q_id") long q_id,
                                  RedirectAttributes redirectAttributes){
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tag Id:" + id));
        tag.removeQuestion(q_id);
        questionWrapper.detach(tag.getId());
        redirectAttributes.addAttribute("q_id", q_id);
        return "redirect:/updatequestion/{q_id}";
    }

    @GetMapping("/tachtag/{id}")
    public String tachTag(@PathVariable(value = "id") long id){
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tag Id:" + id));
        questionWrapper.addTag(tag);
        return "redirect:/newquestion";
    }

    @GetMapping("/{q_id}/tachtagupdate/{id}")
    public String tachTagUpdate(@PathVariable(value = "id") long id,
                                @PathVariable(value = "q_id") long q_id,
                                RedirectAttributes redirectAttributes){
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid tag Id:" + id));
        Question question = questionRepository.findById(q_id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid question Id:" + id));
        tag.addQuestion(question);
        questionRepository.saveAndFlush(question);
        questionWrapper.addTag(tag);
        redirectAttributes.addAttribute("q_id", q_id);
        return "redirect:/updatequestion/{q_id}";
    }

    @GetMapping("/question/{id}")
    public String showQuestion(@PathVariable(value = "id") long id, Model model){
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid question Id:" + id));
        model.addAttribute("question", question);
        model.addAttribute("manual_id", question.getManual().getId());
        return "question";
    }

    @GetMapping("/updatequestion/{id}")
    public String updateQuestion(@PathVariable(value = "id") long id, Model model, Question question){
        if (questionWrapper.isEmpty()){
            question = questionRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid question Id:" + id));
            questionWrapper = questionWrapper.wrapQuestion(question);
            questionRepository.saveAndFlush(question);
        }
        model.addAttribute("manual", questionWrapper.getManual());
        model.addAttribute("q_id", questionWrapper.getId());
        model.addAttribute("tagList", questionWrapper.getTagNames());
        model.addAttribute("questionName", questionWrapper.getName());
        model.addAttribute("answer", questionWrapper.getAnswer());
        model.addAttribute("tags", tagRepository.findAll());
        return "updatequestion";
    }

}
