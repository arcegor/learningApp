package ru.gazintech.learningApp.models.wrappers;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import ru.gazintech.learningApp.models.Manual;
import ru.gazintech.learningApp.models.Test;
import ru.gazintech.learningApp.models.TestAnswer;
import ru.gazintech.learningApp.models.TestQuestion;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Component
public class TestWrapper {
    private String name;

    private long id;

    private String description;

    @Value(value = "100")
    private Integer number;
    private List<TestQuestion> testQuestionList = new ArrayList<>();

    private Manual manual;
    public void addTestQuestion(TestQuestion testQuestion){
        testQuestionList.add(testQuestion);
    }

    public Test unwrapToTest(TestWrapper testWrapper){
        Test test = new Test();
        test.setId(testWrapper.getId());
        for (TestQuestion testQuestion: testWrapper.getTestQuestionList()
        ) {
            testQuestion.setTest(test);
            for (TestAnswer testAnswer: testQuestion.getTestAnswers()
            ) {
                testAnswer.setTestQuestion(testQuestion);
            }
        }
        test.setName(testWrapper.getName());
        test.setNumber(testWrapper.getNumber());
        test.setTestQuestions(testWrapper.getTestQuestionList());
        test.setManual(testWrapper.getManual());
        test.setDescription(testWrapper.getDescription());
        return test;
    }

    public TestWrapper wrapTest(Test test){
        TestWrapper testWrapper = new TestWrapper();
        testWrapper.setName(test.getName());
        testWrapper.setId(test.getId());
        testWrapper.setTestQuestionList(test.getTestQuestions());
        testWrapper.setManual(test.getManual());
        testWrapper.setDescription(test.getDescription());
        testWrapper.setNumber(test.getNumber());
        return testWrapper;
    }

}
