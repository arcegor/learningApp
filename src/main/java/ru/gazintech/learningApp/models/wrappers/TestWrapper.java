package ru.gazintech.learningApp.models.wrappers;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
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
    private List<TestQuestion> testQuestionList = new ArrayList<>();
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
        test.setTestQuestions(testWrapper.getTestQuestionList());
        return test;
    }

    public TestWrapper wrapTest(Test test){
        TestWrapper testWrapper = new TestWrapper();
        testWrapper.setName(test.getName());
        testWrapper.setId(test.getId());
        testWrapper.setTestQuestionList(test.getTestQuestions());
        return testWrapper;
    }

}
