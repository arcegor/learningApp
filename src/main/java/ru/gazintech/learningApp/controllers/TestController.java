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

    @GetMapping("/backtomainpage")
    public String backToMainTest(Test test) {
        return "main";
    }

    @GetMapping("/newtest")
    public String showAddingTestForm(Model model, Test test) {
        List<TestQuestion> testQuestionList = testWrapper.getTestQuestionList();
        String testName = testWrapper.getName();
        model.addAttribute("testQuestionList", testQuestionList);
        model.addAttribute("testName", testName);
        model.addAttribute("testQuestion", new TestQuestion());
        model.addAttribute("testAnswer", new TestAnswer());
        return "newtest";
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
        Test test = new Test();
        for (TestQuestion testQuestion: testWrapper.getTestQuestionList()
             ) {
            testQuestion.setTest(test);
            for (TestAnswer testAnswer: testQuestion.getTestAnswers()
                 ) {
                testAnswer.setTestQuestion(testQuestion);
            }
        }
        test.setName(testWrapper.getName());
        test.setTestQuestions(testWrapper.getTestQuestionList());
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
}
