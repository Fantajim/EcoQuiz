package com.example.ecoquiz;

public class Question {

    private static int counter = 0;
    private int id;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String solution;
    private String questionImage;
    private String questionText;


    // Constructor for default 4 answer multiple choice
    public Question(String answer1, String answer2, String answer3, String answer4, String solution, String questionImage, String questionText) {
        this.id = getNextId();
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.solution = solution;
        this.questionImage = questionImage;
        this.questionText = questionText;
    }

    // Constructor for True/False questions
    public Question(String answer1, String answer2, String solution, String questionText){
        this.id = getNextId();
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.solution = solution;
        this.questionText = questionText;
    }

    private int getNextId() {
        counter++;
        return counter;
    }

    public String getAnswer1() {
        return answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public String getSolution() {
        return solution;
    }

    public String getQuestionImage() {
        return questionImage;
    }

    public String getQuestionText() {
        return questionText;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return "id: "+id+", answer1: "+answer1+", answer2: " +answer2+", answer3: "+answer3+",answer4: "+answer4+", solution: "+solution+", image: "+questionImage+", questionText: "+questionText;
    }
}
