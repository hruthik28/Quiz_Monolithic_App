package com.app.quiz.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Quiz {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Integer id;
    private String title;

    @ManyToMany // will create 2 tables quiz and quiz_questions, where quiz will have the title of quiz and quiz_questions will have questions for that
    private List<Question> questions;
}
