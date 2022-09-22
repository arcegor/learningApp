package ru.gazintech.learningApp.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.gazintech.learningApp.models.Material;
import ru.gazintech.learningApp.repository.MaterialRepository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Service
public class MaterialService{

    @Autowired
    private MaterialRepository materialRepository;

    @Value("${app.upload.dir}")
    public String uploadDir;

    public void save(Material material) {
        materialRepository.saveAndFlush(material);
    }


    public void delete(Material material) {
        materialRepository.delete(material);
    }

    public Material findById(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid material Id:" + id));
    }


    public List<Material> getAll() {
        return materialRepository.findAll();
    }

    public String uploadFile(MultipartFile file, long id) {
        String addToFileName = "material_id_" + id + "_filename_";
        try {
            Path copyLocation = Paths
                    .get(uploadDir + File.separator + addToFileName + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
            return String.valueOf(copyLocation);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    public long getLastMaterialId(){
        List<Material> materials = materialRepository.findAll();
        return materials.size() + 1;
    }

    public String getMaterialName(long id){
        Material material = findById(id);
        return material.getLink();
    }

    public void deleteMaterial(Material material){
        try {
            Files.delete(Paths.get(material.getLink()));
            materialRepository.delete(material);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
