package ru.gazintech.learningApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.gazintech.learningApp.models.Test;
import ru.gazintech.learningApp.models.TestAnswer;
import ru.gazintech.learningApp.models.TestQuestion;
import ru.gazintech.learningApp.models.wrappers.TestWrapper;
import ru.gazintech.learningApp.repository.TestRepository;

import javax.validation.Valid;
import java.util.List;

@Controller("/tests")
public class TestController {
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private TestWrapper testWrapper;

    @GetMapping("/newtest")
    public String showAddingTestForm(Model model, Test test) {
        model.addAttribute("testQuestionList", testWrapper.getTestQuestionList());
        model.addAttribute("testName", testWrapper.getName());
        model.addAttribute("testQuestion", new TestQuestion());
        model.addAttribute("testAnswer", new TestAnswer());
        return "newtest";
    }

    @GetMapping("/backtotests")
    public String backToTests(){
        testWrapper = new TestWrapper();
        return "redirect:/tests";
    }

    @PostMapping("/newtest")
    public String addTest(@RequestParam(value = "name") String testName, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "newtest";
        }
        testWrapper.setName(testName);
        return "redirect:/newtest";
    }

    @PostMapping("/testname")
    public String addTestName(@RequestParam String name) {
        testWrapper.setName(name);
        return "redirect:/newtest";
    }

    @GetMapping("/savetest")
    public String saveTest(Model model) {
        Test test = testWrapper.unwrapToTest(testWrapper);
        testRepository.save(test);
        testWrapper = new TestWrapper();
        return "redirect:/tests";
    }

    @GetMapping("/deletetest/{id}")
    public String deleteTest(@PathVariable("id") long id, Model model) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid test Id:" + id));
        testRepository.delete(test);
        return "redirect:/tests";
    }

    @PostMapping("/testquestion")
    public String addTestQuestion(@Valid TestQuestion testQuestion){
        testWrapper.addTestQuestion(testQuestion);
        return "redirect:/newtest";
    }

    @PostMapping("/testquestionanswer")
    public String addTestQuestionAnswer(@Valid TestAnswer testAnswer,
                                        @RequestParam(value = "index") int index){
        testWrapper.getTestQuestionList().get(index).getTestAnswers().add(testAnswer);
        return "redirect:/newtest";
    }

    @GetMapping("/edittestname")
    public String editTestName(){
        testWrapper.setName(null);
        return "redirect:/newtest";
    }

    @GetMapping("/deletetestquestion/{id}")
    public String editTestQuestion(@PathVariable(value = "id") int id, Model model){
        testWrapper.getTestQuestionList().remove(id);
        return "redirect:/newtest";
    }

    @GetMapping("/deletetestanswer/{id_question}/{id_answer}")
    public String editTestAnswer(@PathVariable(value = "id_question") String id_question,
                                 @PathVariable(value = "id_answer") String id_answer,
                                 Model model){
        testWrapper.getTestQuestionList().get(Integer.parseInt(id_question)).
                getTestAnswers().remove(Integer.parseInt(id_answer));
        return "redirect:/newtest";
    }

    @GetMapping("/showtest/{id}")
    public String showTest(@PathVariable(value = "id") long id, Model model){
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid test Id:" + id));
        model.addAttribute("test", test);
        return "showtest";
    }

    @GetMapping("/checktest/{id}")
    public String checkTest(@PathVariable(value = "id") long id, Model model) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid test Id:" + id));
        model.addAttribute("test", test);
        return "checktest";
    }

    @PostMapping("/checktest/{id}")
    public String checkTest(Model model, @Valid Test test) {
        return "checktest";
    }

    @GetMapping("/updatetest/{id}")
    public String updateTest(@PathVariable(value = "id") long id, Model model){
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid test Id:" + id));
        testWrapper = testWrapper.wrapTest(test);
        model.addAttribute("testQuestionList", testWrapper.getTestQuestionList());
        model.addAttribute("testName", testWrapper.getName());
        model.addAttribute("testQuestion", new TestQuestion());
        model.addAttribute("testAnswer", new TestAnswer());
        return "updatetest";
    }
}
