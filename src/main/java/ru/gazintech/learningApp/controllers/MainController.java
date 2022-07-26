package ru.gazintech.learningApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.gazintech.learningApp.models.Manual;
import ru.gazintech.learningApp.models.Test;
import ru.gazintech.learningApp.repository.ManualRepository;

import javax.validation.Valid;

@Controller
public class MainController
{
    @Autowired
    private ManualRepository manualRepository;
    @GetMapping("/")
    public String showManualList(Model model){
        model.addAttribute("manualList", manualRepository.findAll());
        return "main";
    }

    @GetMapping("/manual/{id}")
    public String showManual(@PathVariable(value = "id") long id, Model model){
        Manual manual =  manualRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid test Id:" + id));
        model.addAttribute("manual", manual);
        return "manual";
    }

    @GetMapping("/manual/{id}/update")
    public String updateManual(@PathVariable(value = "id") long id, Model model){
        Manual manual =  manualRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid test Id:" + id));
        model.addAttribute("manual", manual);
        return "updatemanual";
    }

    @GetMapping("/newmanual")
    public String createManual(Model model){
        model.addAttribute("manual", new Manual());
        return "newmanual";
    }

    @PostMapping("/savemanual")
    public String saveManual(Model model, @Valid Manual manual){
        manualRepository.save(manual);
        return "redirect:/";
    }

    @GetMapping("/deletemanual/{id}")
    public String deleteManual(@PathVariable("id") long id, Model model) {
        Manual manual = manualRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid manual Id:" + id));
        manualRepository.delete(manual);
        return "redirect:/";
    }
}
