package com.doyatama.university.model;

import java.util.ArrayList;
import java.util.List;
/**
 * @author alfa
 */
public class TodoQuestion {
    private String id;
    private String message;
    private String status;
    private List<RPS> rps;
    private List<Exam> exams;
    private List<Quiz> quizzes;
    private List<String> devLecturerIds;

    public TodoQuestion() {
    }

    public TodoQuestion( List<RPS> rps,List <Quiz> quizzes  ,List <Exam> exams, String status) {
        this.rps = rps;
        this.quizzes = quizzes;
        this.exams = exams;
        this.status = status;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String getMessage() {
        return message;
    }

    private void setMessage(String message) {
        this.message = message;
    }

    private String getStatus() {
        return status;
    }

    private void setStatus(String status) {
        this.status = status;
    }


    public List<RPS> getRps() {
        return rps;
    }

    public void setRps(List<RPS> rps) {
        this.rps = rps;
    }



    public List<Exam> getExams() {
        return exams;
    }

    public void setExams(List<Exam> exams) {
        this.exams = exams;
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }
//
//    public List<String> getDevLecturerIds() {
//        List<String> devLecturerIds = new ArrayList<>();
//        for (RPS rps : this.rps) {
//            for (Lecture lecture : rps.getDevLecturers()) {
//                devLecturerIds.add(lecture.getId());
//            }
//        }
//        return devLecturerIds;
//    }
    
}
