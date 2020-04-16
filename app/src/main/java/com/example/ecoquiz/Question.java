package com.example.ecoquiz;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Question {

    private static int counter = 0;
    private int id;
    private ArrayList<String> answerList = new ArrayList<>();
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String solution;
    private String questionImage;
    private String questionText;


    // Constructor for default 4 answer multiple choice
    public Question(String id, String answer1, String answer2, String answer3, String answer4, String solution, String questionImage, String questionText) {
        this.id = Integer.parseInt(id);
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        answerList.add(answer1);
        answerList.add(answer2);
        answerList.add(answer3);
        answerList.add(answer4);
        this.solution = solution;
        this.questionImage = questionImage;
        this.questionText = questionText;
        Collections.shuffle(answerList);
    }

    // Constructor for True/False questions
    public Question(String id, String answer1, String answer2, String solution, String questionText){
        this.id = Integer.parseInt(id);
        this.answer1 = answer1;
        this.answer2 = answer2;
        answerList.add(answer1);
        answerList.add(answer2);
        this.solution = solution;
        this.questionText = questionText;
        Collections.shuffle(answerList);

    }

    private int getNextId() {
        counter++;
        return counter;
    }

    public void setId(int id) {
        this.id = id;
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

    public ArrayList<String> getAnswerList() {
        return answerList;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return "id: "+id+", answer1: "+answer1+", answer2: " +answer2+", answer3: "+answer3+",answer4: "+answer4+", solution: "+solution+", image: "+questionImage+", questionText: "+questionText;
    }
}
