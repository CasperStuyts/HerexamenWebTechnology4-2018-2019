package edu.ap.casper.model;



public class EightBallAnswer {
    private String answer;
    private String question;
    public EightBallAnswer(String answer) {
        this.answer=answer;
        this.question="";
    }

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
