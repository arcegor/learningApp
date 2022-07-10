package ru.gazintech.learningApp.models.wrappers;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import ru.gazintech.learningApp.models.TestQuestion;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Component
@SessionScope
public class TestWrapper {
    private String name;
    private List<TestQuestion> testQuestionList = new ArrayList<>();
    public void addTestQuestion(TestQuestion testQuestion){
        testQuestionList.add(testQuestion);
    }

}
