package ru.gazintech.learningApp.models.wrappers;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import ru.gazintech.learningApp.models.*;
import ru.gazintech.learningApp.models.Question;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Component
public class QuestionWrapper {

    private String name;

    private long id;

    private List<Tag> tags = new ArrayList<>();

    private Manual manual;

    private String answer;


    public void addTag(Tag tag){
        this.tags.add(tag);
    }

    public Boolean isEmpty(){
        return this.name == null;
    }

    public void detach(long tag_id) {
        this.tags.stream().filter(t -> t.getId() == tag_id).findFirst().ifPresent(tag -> this.tags.remove(tag));
    }
    public Question unwrapToQuestion(QuestionWrapper questionWrapper){
        Question question = new Question();
        question.setId(questionWrapper.getId());
        for (Tag tag: questionWrapper.getTags()) {
            tag.setManual(questionWrapper.getManual());
        }
        question.setName(questionWrapper.getName());
        question.setManual(questionWrapper.getManual());
        question.setAnswer(questionWrapper.getAnswer());
        question.setTags(questionWrapper.getTags());
        return question;
    }

    public ArrayList<String> getTagNames(){
        ArrayList<String> tagName = new ArrayList<>();
        for (Tag tag:this.tags
             ) {
            tagName.add(tag.getName());
        }
        return tagName;
    }

    public QuestionWrapper wrapQuestion(Question question) {
        QuestionWrapper questionWrapper = new QuestionWrapper();
        questionWrapper.setName(question.getName());
        questionWrapper.setId(question.getId());
        questionWrapper.setManual(question.getManual());
        questionWrapper.setAnswer(question.getAnswer());
        questionWrapper.setTags(question.getTags());
        return questionWrapper;
    }

}

