package ru.gazintech.learningApp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.gazintech.learningApp.models.Course;
import ru.gazintech.learningApp.models.Lesson;
import ru.gazintech.learningApp.models.Manual;
import ru.gazintech.learningApp.models.Material;
import ru.gazintech.learningApp.models.wrappers.TestWrapper;
import ru.gazintech.learningApp.repository.CourseRepository;
import ru.gazintech.learningApp.repository.LessonRepository;
import ru.gazintech.learningApp.repository.ManualRepository;
import ru.gazintech.learningApp.repository.MaterialRepository;
import ru.gazintech.learningApp.service.MaterialService;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class MaterialController {

    @Autowired
    private MaterialService materialService;
    @Autowired
    private ManualRepository manualRepository;

    @GetMapping("/{manual_id}/material/{id}")
    public String showMaterial(@PathVariable(value = "id") long id,
                             @PathVariable(value = "manual_id") long manual_id,
                             Model model){
        Material material =  materialService.findById(id);
        model.addAttribute("material", material);
        model.addAttribute("manual_id", manual_id);
        return "material";
    }

    @GetMapping("/{id}/newmaterial")
    public String createMaterial(@PathVariable(value = "id") long id, Model model){
        model.addAttribute("material", new Material());
        model.addAttribute("manual_id", id);
        return "newmaterial";
    }

    @PostMapping("/savematerial")
    public String saveMaterial(Model model,
                             @Valid Material material,
                             @RequestParam(value = "manual_id") long id,
                             @RequestParam("file") MultipartFile file,
                             RedirectAttributes redirectAttributes){
        material.setManual(manualRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid manual Id:" + id)));
        material.setLink(materialService.uploadFile(file, materialService.getLastMaterialId()));
        materialService.save(material);
        redirectAttributes.addAttribute("id", id);
        return "redirect:/{id}/materials";
    }

    @GetMapping("/{manual_id}/deletematerial/{id}")
    public String deleteMaterial(@PathVariable("id") long id,
                               @PathVariable(value = "manual_id") long manual_id,
                               Model model,
                               RedirectAttributes redirectAttributes
    ) {
        Material material = materialService.findById(id);
        materialService.deleteMaterial(material);
        redirectAttributes.addAttribute("manual_id", manual_id);
        return "redirect:/{manual_id}/materials";
    }

    @GetMapping("/{manual_id}/updatematerial/{id}")
    public String updateMaterial(@PathVariable(value = "id") long id,
                               @PathVariable(value = "manual_id") long manual_id,
                               Model model){
        Material material = materialService.findById(id);
        model.addAttribute("manual_id", manual_id);
        model.addAttribute("material", material);
        return "updatematerial";
    }

    @GetMapping("/downloadmaterial/{id}")
    public ResponseEntity<Resource> download(@PathVariable(value = "id") long id)
            throws IOException {

        File file = new File(materialService.getMaterialName(id));
        Path path = Paths.get(file.getAbsolutePath());

        String name = file.getName().substring(file.getName().indexOf("filename_") + 9);


        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + name);
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");


        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}

