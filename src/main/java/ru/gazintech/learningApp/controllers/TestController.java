package ru.gazintech.learningApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.gazintech.learningApp.models.Manual;
import ru.gazintech.learningApp.models.Test;
import ru.gazintech.learningApp.models.TestAnswer;
import ru.gazintech.learningApp.models.TestQuestion;
import ru.gazintech.learningApp.models.wrappers.TestWrapper;
import ru.gazintech.learningApp.repository.ManualRepository;
import ru.gazintech.learningApp.repository.TestRepository;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Controller
public class TestController {
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private ManualRepository manualRepository;
    @Autowired
    private TestWrapper testWrapper;

    @GetMapping("/newtest")
    public String showAddingTestForm(
            Model model, Test test) {
        testWrapper.getTestQuestionList().sort(TestQuestion::compareTo);
        model.addAttribute("testQuestionList", testWrapper.getTestQuestionList());
        model.addAttribute("testName", testWrapper.getName());
        model.addAttribute("testDescription", testWrapper.getDescription());
        model.addAttribute("testQuestion", new TestQuestion());
        model.addAttribute("testAnswer", new TestAnswer());
        model.addAttribute("number", testWrapper.getNumber());
        return "newtest";
    }

    @GetMapping("/backtotests")
    public String backToTests(RedirectAttributes redirectAttributes){
        long id = testWrapper.getManual().getId();
        redirectAttributes.addAttribute("id", id);
        testWrapper = new TestWrapper();
        return "redirect:/{id}/tests";
    }

    @PostMapping("/inittest")
    public String addManualToTest(@RequestParam(value = "id") long id){
        Manual manual =  manualRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid test Id:" + id));
        testWrapper = new TestWrapper();
        testWrapper.setManual(manual);
        return "redirect:/newtest";
    }

    @PostMapping("/testname")
    public String addTestName(@RequestParam String name) {
        testWrapper.setName(name);
        return "redirect:/newtest";
    }

    @GetMapping("/edittestname")
    public String editTestName(Model model) {
        testWrapper.setName(null);
        return "redirect:/newtest";
    }

    @GetMapping("/edittestdescription")
    public String editTestDescription(){
        testWrapper.setDescription(null);
        return "redirect:/newtest";
    }

    @PostMapping("/testdescription")
    public String addTestDescription(@RequestParam String description) {
        testWrapper.setDescription(description);
        return "redirect:/newtest";
    }

    @GetMapping("/savetest")
    public String saveTest(Model model,
                           RedirectAttributes redirectAttributes) {
        Test test = testWrapper.unwrapToTest(testWrapper);
        testRepository.save(test);
        testWrapper = new TestWrapper();
        long id = test.getManual().getId();
        redirectAttributes.addAttribute("id", id);
        return "redirect:/{id}/tests";
    }

    @GetMapping("/deletetest/{id}")
    public String deleteTest(@PathVariable("id") long id,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid test Id:" + id));
        testRepository.delete(test);
        redirectAttributes.addAttribute("id", test.getManual().getId());
        return "redirect:/{id}/tests";
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

    @PostMapping("/edittestdescription")
    public String editTestDescription(@RequestParam(value = "description") String description){
        testWrapper.setDescription(description);
        return "redirect:/newtest";
    }

    @PostMapping("/addtestnumber")
    public String addTestNumber(@RequestParam(value = "number") int number){
        testWrapper.setNumber(number);
        return "redirect:/newtest";
    }

    @GetMapping("/edittestnumber")
    public String editTestNumber(){
        testWrapper.setNumber(null);
        return "redirect:/newtest";
    }

    @GetMapping("/deletetestquestion/{id}")
    public String deleteTestQuestion(@PathVariable(value = "id") int id, Model model){
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
        model.addAttribute("manualId", test.getManual().getId());
        return "showtest";
    }

    @GetMapping("/updatetest/{id}")
    public String updateTest(@PathVariable(value = "id") long id, Model model, Test test){
        test = testRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid test Id:" + id));
        testWrapper = testWrapper.wrapTest(test);
        testWrapper.getTestQuestionList().sort(TestQuestion::compareTo);
        model.addAttribute("testQuestionList", testWrapper.getTestQuestionList());
        model.addAttribute("testName", testWrapper.getName());
        model.addAttribute("testDescription", testWrapper.getDescription());
        model.addAttribute("testQuestion", new TestQuestion());
        model.addAttribute("testAnswer", new TestAnswer());
        model.addAttribute("number", testWrapper.getNumber());
        return "updatetest";
    }

    @GetMapping("/edittestquestion/{id}")
    public String editTestQuestion(@PathVariable(value = "id") int id, Model model){
        TestQuestion testQuestion = testWrapper.getTestQuestionList().get(id);
        model.addAttribute("editingQuestion", testQuestion);
        model.addAttribute("answers", testQuestion.getTestAnswers());
        model.addAttribute("question_id", id);
        return "edittestquestion";
    }

    @PostMapping("/edittestquestion")
    public String updateTestQuestion(@Valid TestQuestion editingQuestion,
                                     Model model, @RequestParam(value = "question_id") int id){
        testWrapper.getTestQuestionList().set(id, editingQuestion);
        return "redirect:/newtest";
    }

    @GetMapping("/playtest/{id}")
    public String playTest(@PathVariable(value = "id") long id,
                           Model model){
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid test Id:" + id));
        model.addAttribute("test", test);
        model.addAttribute("manualId", test.getManual().getId());
        return "playtest";
    }

    @PostMapping("/playtest")
    public String checkAnswers(@Valid Test test, Model model){
        return "redirect:/playtest";
    }

}
