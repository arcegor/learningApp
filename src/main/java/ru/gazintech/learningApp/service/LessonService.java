package ru.gazintech.learningApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.gazintech.learningApp.models.Lesson;
import ru.gazintech.learningApp.models.Material;
import ru.gazintech.learningApp.repository.LessonRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;

@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Value("${app.upload.video.dir}")
    public String uploadVideoDir;

    public long getLastLessonId(){
        List<Lesson> lessons = lessonRepository.findAll();
        return lessons.size() + 1;
    }

    public String uploadVideo(MultipartFile file, long id) {
        String addToFileName = "lesson_id_" + String.valueOf(id) + "_filename_";
        try {
            Path copyLocation = Paths
                    .get(uploadVideoDir + File.separator + addToFileName + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename())));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
            return "/video/" + addToFileName + file.getOriginalFilename();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    public void deleteLesson(Lesson lesson){
        try {
            String name = lesson.getVideo().replace("/video/", "");
            Files.delete(Paths.get(name).toAbsolutePath());
            lessonRepository.delete(lesson);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void deleteOldVideo(Lesson lesson){
        try {
            String name = lesson.getVideo().replace("/video/", "");
            Files.delete(Paths.get(name).toAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
