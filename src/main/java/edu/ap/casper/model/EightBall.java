package edu.ap.casper.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EightBall {
    public String answer;
    public  String question;

    public EightBall(String answer, String question) {
        this.answer=answer;
        this.question= question;
    }
    public EightBall(){}

    public String getQuestion() {
        return question;
    }
    public String getAnswer(){
        return answer;
    }
    public void setQuestion(String question){
        this.question = question;

    }
}
