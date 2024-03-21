package com.hnu.heshequ.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.hnu.heshequ.R;

public class QuestionAnswerItem extends LinearLayout {

    private TextView question;
    private TextView answer;

    public QuestionAnswerItem(Context context) {
        this(context, null);
    }

    public QuestionAnswerItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuestionAnswerItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public QuestionAnswerItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        LayoutInflater.from(context).inflate(R.layout.question_answer_item, this);

        question = findViewById(R.id.question);
        answer = findViewById(R.id.answer);
    }

    public void setQuestion(String question) {
        this.question.setText(question);
    }

    public void setAnswer(String answer) {
        this.answer.setText(answer);
    }
}
