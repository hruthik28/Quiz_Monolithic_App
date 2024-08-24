package com.app.quiz.service;

import com.app.quiz.dao.QuestionDao;
import com.app.quiz.dao.QuizDao;
import com.app.quiz.model.Question;
import com.app.quiz.model.QuestionWrapper;
import com.app.quiz.model.Quiz;
import com.app.quiz.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;

    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<String> createQuiz(String category, int noOfQuestions, String title) {
        List<Question> questions = questionDao.findRandomQuestionsByCategory(category, noOfQuestions);

        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quizDao.save(quiz);

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Optional<Quiz> quiz = quizDao.findById(id); // will return the result based on title
        List<Question> questionsFromDB = quiz.get().getQuestions(); // will return all the questions that are part of the title with question ids
        List<QuestionWrapper> questionsForUser = new ArrayList<>();
        for(Question q: questionsFromDB){
            QuestionWrapper qw =new QuestionWrapper(
                    q.getId(), q.getQuestionTitle(), q.getOption1(), q.getOption2(),
                    q.getOption3(), q.getOption4()); //iterate over the questions and set only required values to wrapper class
            questionsForUser.add(qw);
        }
        return  new ResponseEntity<>(questionsForUser, HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responseList) {
        Quiz quiz = quizDao.findById(id).get(); // get all questions for the id
        List<Question> questions = quiz.getQuestions();
        int rightAns = 0;
        int i = 0;
        for(Response resp: responseList){
            if(resp.getResponse().equalsIgnoreCase(questions.get(i).getRightAnswer())){
                rightAns++;
            }
            i++;
        }
        return new ResponseEntity<>(rightAns, HttpStatus.OK);
    }
}
