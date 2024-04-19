package com.dhian.quiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {
    TextView questionTv,option1,option2,option3,questionsAttempted,userSelected;
    ProgressBar progressBar;
    TextView details;
    long userScore = 0;
    int index = 0;
    int wrongAnswers = 0;
    int rightAnswers = 0;
    MaterialButton nextBtn;
    ArrayList<Questions> questionsArrayList;
    Questions questions;
    RelativeLayout adLayout;
    Boolean isSubmitted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        initializeViews();
        questionsArrayList = new ArrayList<>();
        loadQuestionsFromXml();
        goToNextQuestion();
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    public void answerChecker(TextView textView){
        String correctAnswer = questions.getAnswer();

        option1.setEnabled(false);
        option1.setBackground(
                getResources().getDrawable(option1.getText().toString().equals(correctAnswer) ? R.drawable.right_option_bg : R.drawable.option_bg)
        );
        option2.setEnabled(false);
        option2.setBackground(
                getResources().getDrawable(option2.getText().toString().equals(correctAnswer) ? R.drawable.right_option_bg : R.drawable.option_bg)
        );
        option3.setEnabled(false);
        option3.setBackground(
                getResources().getDrawable(option3.getText().toString().equals(correctAnswer) ? R.drawable.right_option_bg : R.drawable.option_bg)
        );

        if (textView.getText().toString().equals(questions.getAnswer())){
            userScore++;
            rightAnswers++;
        } else {
            textView.setBackground(getResources().getDrawable(R.drawable.wrong_option_bg));
            userScore--;
            wrongAnswers++;
        }
    }

    public void resetAnswers(){
        option1.setEnabled(true);
        option2.setEnabled(true);
        option3.setEnabled(true);

        option1.setBackground(getResources().getDrawable(R.drawable.option_bg));
        option2.setBackground(getResources().getDrawable(R.drawable.option_bg));
        option3.setBackground(getResources().getDrawable(R.drawable.option_bg));

    }

    public void submitAnswer() {
        answerChecker(userSelected);
    }

    public void goToNextQuestion(){
        if (index < questionsArrayList.size()){
            questionsAttempted.setText(String.format("%d/%d",(index+1), questionsArrayList.size()));
            int i = (index + 1) * 100 / questionsArrayList.size();
            progressBar.setProgress(i);
            questions = questionsArrayList.get(index);

            questionTv.setText(questions.getQuestion());
            details.setText(questions.getDetails());
            option1.setText(questions.getOptionA());
            option2.setText(questions.getOptionB());
            option3.setText(questions.getOptionC());

            option1.setEnabled(true);
            option2.setEnabled(true);
            option3.setEnabled(true);

            nextBtn.setEnabled(true);
            userSelected = null;
            isSubmitted = false;
        }

    }

    public void onClickAnswer(View view) {
        this.userSelected = (TextView) view;
        String selectedText = ((TextView) view).getText().toString();
        resetAnswers();

        Drawable selectedOptionBg = getResources().getDrawable(R.drawable.selected_option_bg);
        if (selectedText.equals(option1.getText().toString())) {
            option1.setBackground(selectedOptionBg);
        } else if (selectedText.equals(option2.getText().toString())) {
            option2.setBackground(selectedOptionBg);
        } else if (selectedText.equals(option3.getText().toString())) {
            option3.setBackground(selectedOptionBg);
        }
    }

    public void onClickSubmitOrNext(View view) {
        if (userSelected == null) {
            Toast.makeText(this, "Answer cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        resetAnswers();
        if (isSubmitted) {
            index++;

            if (index >= questionsArrayList.size()) {
                Intent intent = new Intent(QuizActivity.this,QuizResultActivity.class);
                intent.putExtra("RIGHT",String.valueOf(rightAnswers));
                intent.putExtra("QUESTIONS",String.valueOf(questionsArrayList.size()));
                startActivity(intent);
                finish();
            }

            goToNextQuestion();
            nextBtn.setText("Submit");
        } else {
            isSubmitted = true;
            nextBtn.setText("Next Question");
            answerChecker(userSelected);
        }
    }

    public void initializeViews(){
        questionTv = findViewById(R.id.question);
        details = findViewById(R.id.details);
        option1 = findViewById(R.id.option_one);
        option2 = findViewById(R.id.option_two);
        option3 = findViewById(R.id.option_three);
        questionsAttempted = findViewById(R.id.questionsAttempted);
        progressBar = findViewById(R.id.progressBar);
        nextBtn = findViewById(R.id.next_btn);
        adLayout = findViewById(R.id.ad_layout_quiz);

        nextBtn.setText("Submit");
    }
private void loadQuestionsFromXml() {
        XmlResourceParser parser = getResources().getXml(R.xml.questions);

        try {
            int eventType = parser.getEventType();
            Questions currentQuestion = null;

            while (eventType != XmlResourceParser.END_DOCUMENT) {
                if (eventType == XmlResourceParser.START_TAG) {
                    String tagName = parser.getName();

                    if (tagName.equals("question")) {
                        currentQuestion = new Questions();
                    } else if (currentQuestion != null) {
                        if (tagName.equals("text")) {
                            currentQuestion.setQuestion(parser.nextText());
                        } else if (tagName.equals("details")) {
                            currentQuestion.setDetails(parser.nextText());
                        } else if (tagName.equals("optionA")) {
                            currentQuestion.setOptionA(parser.nextText());
                        } else if (tagName.equals("optionB")) {
                            currentQuestion.setOptionB(parser.nextText());
                        } else if (tagName.equals("optionC")) {
                            currentQuestion.setOptionC(parser.nextText());
                        } else if (tagName.equals("correctAnswer")) {
                            currentQuestion.setAnswer(parser.nextText());
                        }
                    }
                } else if (eventType == XmlResourceParser.END_TAG) {
                    String tagName = parser.getName();

                    if (tagName.equals("question")) {
                        if (currentQuestion != null) {
                            questionsArrayList.add(currentQuestion);
                            currentQuestion = null;
                        }
                    }
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            parser.close();
        }
    }


}